package simu.backend.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import simu.backend.datasource.MariaDbJpaConnection;
import simu.backend.entity.DeliveryStatistics;

/**
 * Data Access Object (DAO) for {@link simu.backend.entity.DeliveryStatistics} entity.
 * <p>
 * Provides CRUD operations for delivery statistics data, including persist, update, and find by ID.
 */
public class DeliveryStatisticsDao {
    
    /**
     * Persists a new delivery statistics record to the database.
     *
     * @param deliveryStats The delivery statistics entity to persist
     * @return The persisted entity with generated ID
     * @throws RuntimeException if persistence fails
     */
    public DeliveryStatistics persist(DeliveryStatistics deliveryStats) {
        EntityManager em = MariaDbJpaConnection.getInstance();
        EntityTransaction transaction = em.getTransaction();
        
        try {
            transaction.begin();
            em.persist(deliveryStats);
            transaction.commit();
            System.out.println("Delivery statistics saved with ID: " + deliveryStats.getId());
            return deliveryStats;
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            System.err.println("Error persisting delivery statistics: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
    
    /**
     * Updates an existing delivery statistics record.
     *
     * @param deliveryStats The delivery statistics entity to update
     * @return The updated entity
     * @throws RuntimeException if update fails
     */
    public DeliveryStatistics update(DeliveryStatistics deliveryStats) {
        EntityManager em = MariaDbJpaConnection.getInstance();
        EntityTransaction transaction = em.getTransaction();
        
        try {
            transaction.begin();
            DeliveryStatistics updated = em.merge(deliveryStats);
            transaction.commit();
            System.out.println("Delivery statistics updated for ID: " + updated.getId());
            return updated;
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            System.err.println("Error updating delivery statistics: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
    
    /**
     * Finds a delivery statistics record by its ID.
     *
     * @param id The ID to search for
     * @return The found entity or null if not found
     */
    public DeliveryStatistics findById(int id) {
        EntityManager em = MariaDbJpaConnection.getInstance();
        try {
            return em.find(DeliveryStatistics.class, id);
        } catch (Exception e) {
            System.err.println("Error finding delivery statistics by ID: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}