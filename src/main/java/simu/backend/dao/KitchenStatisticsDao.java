package simu.backend.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import simu.backend.datasource.MariaDbJpaConnection;
import simu.backend.entity.KitchenStatistics;

/**
 * Data Access Object (DAO) for {@link simu.backend.entity.KitchenStatistics} entity.
 * <p>
 * Provides CRUD operations for kitchen statistics data, including persist, update, and find by ID.
 */
public class KitchenStatisticsDao {
    
    /**
     * Persists a new kitchen statistics record to the database.
     *
     * @param kitchenStats The kitchen statistics entity to persist
     * @return The persisted entity with generated ID
     * @throws RuntimeException if persistence fails
     */
    public KitchenStatistics persist(KitchenStatistics kitchenStats) {
        EntityManager em = MariaDbJpaConnection.getInstance();
        EntityTransaction transaction = em.getTransaction();
        
        try {
            transaction.begin();
            em.persist(kitchenStats);
            transaction.commit();
            System.out.println("Kitchen statistics saved with ID: " + kitchenStats.getId());
            return kitchenStats;
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            System.err.println("Error persisting kitchen statistics: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
    
    /**
     * Updates an existing kitchen statistics record.
     *
     * @param kitchenStats The kitchen statistics entity to update
     * @return The updated entity
     * @throws RuntimeException if update fails
     */
    public KitchenStatistics update(KitchenStatistics kitchenStats) {
        EntityManager em = MariaDbJpaConnection.getInstance();
        EntityTransaction transaction = em.getTransaction();
        
        try {
            transaction.begin();
            KitchenStatistics updated = em.merge(kitchenStats);
            transaction.commit();
            System.out.println("Kitchen statistics updated for ID: " + updated.getId());
            return updated;
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            System.err.println("Error updating kitchen statistics: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
    
    /**
     * Finds a kitchen statistics record by its ID.
     *
     * @param id The ID to search for
     * @return The found entity or null if not found
     */
    public KitchenStatistics findById(int id) {
        EntityManager em = MariaDbJpaConnection.getInstance();
        try {
            return em.find(KitchenStatistics.class, id);
        } catch (Exception e) {
            System.err.println("Error finding kitchen statistics by ID: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}