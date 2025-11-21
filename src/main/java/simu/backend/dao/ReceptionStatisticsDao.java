package simu.backend.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import simu.backend.datasource.MariaDbJpaConnection;
import simu.backend.entity.ReceptionStatistics;
import simu.framework.Trace;

/**
 * Data Access Object (DAO) for {@link simu.backend.entity.ReceptionStatistics} entity.
 * <p>
 * Provides CRUD operations for reception statistics data, including persist, update, and find by ID.
 */
public class ReceptionStatisticsDao {
    
    /**
     * Persists a new reception statistics record to the database.
     *
     * @param receptionStats The reception statistics entity to persist
     * @return The persisted entity with generated ID
     * @throws RuntimeException if persistence fails
     */
    public ReceptionStatistics persist(ReceptionStatistics receptionStats) {
        EntityManager em = MariaDbJpaConnection.getInstance();
        EntityTransaction transaction = em.getTransaction();
        
        try {
            transaction.begin();
            em.persist(receptionStats);
            transaction.commit();
            Trace.out(Trace.Level.INFO, "Reception statistics saved with ID: " + receptionStats.getId());
            return receptionStats;
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            Trace.out(Trace.Level.ERR, "Error persisting reception statistics: " + e.getMessage());
            java.io.StringWriter sw = new java.io.StringWriter();
            e.printStackTrace(new java.io.PrintWriter(sw));
            Trace.out(Trace.Level.WAR, sw.toString());
            throw e;
        }
    }
    
    /**
     * Updates an existing reception statistics record.
     *
     * @param receptionStats The reception statistics entity to update
     * @return The updated entity
     * @throws RuntimeException if update fails
     */
    public ReceptionStatistics update(ReceptionStatistics receptionStats) {
        EntityManager em = MariaDbJpaConnection.getInstance();
        EntityTransaction transaction = em.getTransaction();
        
        try {
            transaction.begin();
            ReceptionStatistics updated = em.merge(receptionStats);
            transaction.commit();
            Trace.out(Trace.Level.INFO, "Reception statistics updated for ID: " + updated.getId());
            return updated;
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            Trace.out(Trace.Level.ERR, "Error updating reception statistics: " + e.getMessage());
            java.io.StringWriter sw2 = new java.io.StringWriter();
            e.printStackTrace(new java.io.PrintWriter(sw2));
            Trace.out(Trace.Level.WAR, sw2.toString());
            throw e;
        }
    }
    
    /**
     * Finds a reception statistics record by its ID.
     *
     * @param id The ID to search for
     * @return The found entity or null if not found
     */
    public ReceptionStatistics findById(int id) {
        EntityManager em = MariaDbJpaConnection.getInstance();
        try {
            return em.find(ReceptionStatistics.class, id);
        } catch (Exception e) {
            Trace.out(Trace.Level.ERR, "Error finding reception statistics by ID: " + e.getMessage());
            java.io.StringWriter sw3 = new java.io.StringWriter();
            e.printStackTrace(new java.io.PrintWriter(sw3));
            Trace.out(Trace.Level.WAR, sw3.toString());
            return null;
        }
    }
}