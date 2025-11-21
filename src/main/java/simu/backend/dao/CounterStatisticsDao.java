package simu.backend.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import simu.backend.datasource.MariaDbJpaConnection;
import simu.backend.entity.CounterStatistics;

/**
 * Data Access Object (DAO) for {@link simu.backend.entity.CounterStatistics} entity.
 * <p>
 * Provides CRUD operations for counter statistics in the database.
 */
public class CounterStatisticsDao {
    
    /**
     * Persists a new CounterStatistics entity to the database.
     * @param counterStats the entity to persist
     * @return the persisted entity
     */
    public CounterStatistics persist(CounterStatistics counterStats) {
        EntityManager em = MariaDbJpaConnection.getInstance();
        EntityTransaction transaction = em.getTransaction();
        
        try {
            transaction.begin();
            em.persist(counterStats);
            transaction.commit();
            System.out.println("Counter statistics saved with ID: " + counterStats.getId());
            return counterStats;
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            System.err.println("Error persisting counter statistics: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
    
    /**
     * Updates an existing CounterStatistics entity in the database.
     * @param counterStats the entity to update
     * @return the updated entity
     */
    public CounterStatistics update(CounterStatistics counterStats) {
        EntityManager em = MariaDbJpaConnection.getInstance();
        EntityTransaction transaction = em.getTransaction();
        
        try {
            transaction.begin();
            CounterStatistics updated = em.merge(counterStats);
            transaction.commit();
            System.out.println("Counter statistics updated for ID: " + updated.getId());
            return updated;
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            System.err.println("Error updating counter statistics: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
    
    /**
     * Finds a CounterStatistics entity by its ID.
     * @param id the ID of the entity
     * @return the found entity, or null if not found
     */
    public CounterStatistics findById(int id) {
        EntityManager em = MariaDbJpaConnection.getInstance();
        try {
            return em.find(CounterStatistics.class, id);
        } catch (Exception e) {
            System.err.println("Error finding counter statistics by ID: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}