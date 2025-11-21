package simu.backend.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import simu.backend.datasource.MariaDbJpaConnection;
import simu.backend.entity.OverviewStatistics;
import simu.framework.Trace;

import java.util.List;

/**
 * Data Access Object (DAO) for the OverviewStatistics entity.
 * Provides CRUD operations and database interaction for overview statistics records.
 */
public class OverviewStatisticsDao {
    
    /**
     * Persists a new overview statistics record to the database.
     * @param overviewStats The overview statistics to persist
     * @return The persisted entity with generated ID
     */
    public OverviewStatistics persist(OverviewStatistics overviewStats) {
        EntityManager em = MariaDbJpaConnection.getInstance();
        EntityTransaction transaction = em.getTransaction();
        
        try {
            transaction.begin();
            em.persist(overviewStats);
            transaction.commit();
            Trace.out(Trace.Level.INFO, "Overview statistics saved with ID: " + overviewStats.getId());
            return overviewStats;
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            Trace.out(Trace.Level.ERR, "Error persisting overview statistics: " + e.getMessage());
            java.io.StringWriter sw = new java.io.StringWriter();
            e.printStackTrace(new java.io.PrintWriter(sw));
            Trace.out(Trace.Level.WAR, sw.toString());
            throw e;
        }
    }
    
    /**
     * Updates an existing overview statistics record.
     * @param overviewStats The overview statistics to update
     * @return The updated entity
     */
    public OverviewStatistics update(OverviewStatistics overviewStats) {
        EntityManager em = MariaDbJpaConnection.getInstance();
        EntityTransaction transaction = em.getTransaction();
        
        try {
            transaction.begin();
            OverviewStatistics updated = em.merge(overviewStats);
            transaction.commit();
            Trace.out(Trace.Level.INFO, "Overview statistics updated for ID: " + updated.getId());
            return updated;
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            Trace.out(Trace.Level.ERR, "Error updating overview statistics: " + e.getMessage());
            java.io.StringWriter sw2 = new java.io.StringWriter();
            e.printStackTrace(new java.io.PrintWriter(sw2));
            Trace.out(Trace.Level.WAR, sw2.toString());
            throw e;
        }
    }
    
    /**
     * Finds an overview statistics record by ID.
     * @param id The ID to search for
     * @return The found entity or null if not found
     */
    public OverviewStatistics findById(int id) {
        EntityManager em = MariaDbJpaConnection.getInstance();
        
        try {
            return em.find(OverviewStatistics.class, id);
        } catch (Exception e) {
            System.err.println("Error finding overview statistics by ID: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Finds all overview statistics records.
     * @return List of all overview statistics
     */
    public List<OverviewStatistics> findAll() {
        EntityManager em = MariaDbJpaConnection.getInstance();
        
        try {
            Trace.out(Trace.Level.WAR, "DAO findAll invoked. EntityManager: " + em + ", open=" + (em != null ? em.isOpen() : "null"));
            
            TypedQuery<OverviewStatistics> query = em.createQuery(
                "SELECT o FROM OverviewStatistics o ORDER BY o.id", OverviewStatistics.class);
            List<OverviewStatistics> results = query.getResultList();
            
            Trace.out(Trace.Level.INFO, "Query executed. Results count: " + (results != null ? results.size() : 0));
            
            return results;
        } catch (Exception e) {
            Trace.out(Trace.Level.ERR, "DAO findAll() error: " + e.getMessage());
            java.io.StringWriter sw3 = new java.io.StringWriter();
            e.printStackTrace(new java.io.PrintWriter(sw3));
            Trace.out(Trace.Level.WAR, sw3.toString());
            return null;
        }
    }
    
    /**
     * Deletes an overview statistics record.
     * @param overviewStats The entity to delete
     * @return true if deletion was successful, false otherwise
     */
    public boolean delete(OverviewStatistics overviewStats) {
        EntityManager em = MariaDbJpaConnection.getInstance();
        EntityTransaction transaction = em.getTransaction();
        
        try {
            transaction.begin();
            
            // Make sure the entity is managed
            if (!em.contains(overviewStats)) {
                overviewStats = em.merge(overviewStats);
            }
            
            em.remove(overviewStats);
            transaction.commit();
            Trace.out(Trace.Level.INFO, "Overview statistics deleted for ID: " + overviewStats.getId());
            return true;
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            Trace.out(Trace.Level.ERR, "Error deleting overview statistics: " + e.getMessage());
            java.io.StringWriter sw4 = new java.io.StringWriter();
            e.printStackTrace(new java.io.PrintWriter(sw4));
            Trace.out(Trace.Level.WAR, sw4.toString());
            return false;
        }
    }
    
    /**
     * Deletes all records where total_simulation_time is 0 or null
     * @return Number of records deleted
     */
    public int deleteInvalidRecords() {
        EntityManager em = MariaDbJpaConnection.getInstance();
        EntityTransaction transaction = em.getTransaction();
        
        try {
            transaction.begin();
            
            // Delete records where total_simulation_time is 0
            int deletedCount = em.createQuery(
                "DELETE FROM OverviewStatistics o WHERE o.totalSimulationTime = 0")
                .executeUpdate();
                
            transaction.commit();
            Trace.out(Trace.Level.INFO, "Deleted " + deletedCount + " invalid records (total_simulation_time = 0)");
            return deletedCount;
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            Trace.out(Trace.Level.ERR, "Error deleting invalid records: " + e.getMessage());
            java.io.StringWriter sw5 = new java.io.StringWriter();
            e.printStackTrace(new java.io.PrintWriter(sw5));
            Trace.out(Trace.Level.WAR, sw5.toString());
            return 0;
        }
    }
}