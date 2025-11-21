package simu.backend.dao;

import simu.backend.entity.ProjectMetrics;
import simu.backend.datasource.MariaDbJpaConnection;
import simu.framework.Trace;
import jakarta.persistence.EntityManager;
import java.util.List;

/**
 * Data Access Object (DAO) for {@link simu.backend.entity.ProjectMetrics} entity.
 * <p>
 * Provides CRUD operations and metrics count for project metrics data.
 */
public class ProjectMetricsDao {

    /**
     * Persists a new project metrics record to the database.
     *
     * @param metrics The project metrics entity to persist
     */
    public void persist(ProjectMetrics metrics) {
        EntityManager em = MariaDbJpaConnection.getInstance();
        if (em == null) {
            Trace.out(Trace.Level.ERR, "Cannot get EntityManager. Data not saved.");
            return;
        }
        
        try {
            em.getTransaction().begin();
            em.persist(metrics);
            em.getTransaction().commit();
            Trace.out(Trace.Level.INFO, "ProjectMetrics saved successfully with JPA.");
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            Trace.out(Trace.Level.ERR, "Error persisting ProjectMetrics: " + e.getMessage());
            java.io.StringWriter sw = new java.io.StringWriter();
            e.printStackTrace(new java.io.PrintWriter(sw));
            Trace.out(Trace.Level.WAR, sw.toString());
        }
    }

    /**
     * Finds a project metrics record by its ID.
     *
     * @param id The ID to search for
     * @return The found entity or null if not found
     */
    public ProjectMetrics find(int id) {
        EntityManager em = MariaDbJpaConnection.getInstance();
        if (em == null) {
            Trace.out(Trace.Level.ERR, "Cannot get EntityManager. Cannot retrieve data.");
            return null;
        }
        
        try {
            return em.find(ProjectMetrics.class, id);
        } catch (Exception e) {
            Trace.out(Trace.Level.ERR, "Error finding ProjectMetrics with id " + id + ": " + e.getMessage());
            java.io.StringWriter sw = new java.io.StringWriter();
            e.printStackTrace(new java.io.PrintWriter(sw));
            Trace.out(Trace.Level.WAR, sw.toString());
            return null;
        }
    }

    /**
     * Finds all project metrics records.
     *
     * @return List of all project metrics entities
     */
    @SuppressWarnings("unchecked")
    public List<ProjectMetrics> findAll() {
        EntityManager em = MariaDbJpaConnection.getInstance();
        if (em == null) {
            Trace.out(Trace.Level.ERR, "Cannot get EntityManager. Cannot retrieve data.");
            return null;
        }
        
        try {
            return em.createQuery("SELECT p FROM ProjectMetrics p ORDER BY p.id").getResultList();
        } catch (Exception e) {
            Trace.out(Trace.Level.ERR, "Error retrieving all ProjectMetrics: " + e.getMessage());
            java.io.StringWriter sw = new java.io.StringWriter();
            e.printStackTrace(new java.io.PrintWriter(sw));
            Trace.out(Trace.Level.WAR, sw.toString());
            return null;
        }
    }
    
    /**
     * Updates an existing project metrics record.
     *
     * @param metrics The project metrics entity to update
     */
    public void update(ProjectMetrics metrics) {
        EntityManager em = MariaDbJpaConnection.getInstance();
        if (em == null) {
            Trace.out(Trace.Level.ERR, "Cannot get EntityManager. Data not updated.");
            return;
        }
        
        try {
            em.getTransaction().begin();
            em.merge(metrics);
            em.getTransaction().commit();
            Trace.out(Trace.Level.INFO, "ProjectMetrics updated successfully with JPA.");
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            Trace.out(Trace.Level.ERR, "Error updating ProjectMetrics: " + e.getMessage());
            java.io.StringWriter sw = new java.io.StringWriter();
            e.printStackTrace(new java.io.PrintWriter(sw));
            Trace.out(Trace.Level.WAR, sw.toString());
        }
    }

    /**
     * Deletes a project metrics record.
     *
     * @param metrics The project metrics entity to delete
     */
    public void delete(ProjectMetrics metrics) {
        EntityManager em = MariaDbJpaConnection.getInstance();
        if (em == null) {
            Trace.out(Trace.Level.ERR, "Cannot get EntityManager. Data not deleted.");
            return;
        }
        
        try {
            em.getTransaction().begin();
            // Ensure the entity is managed before removing
            ProjectMetrics managedMetrics = em.find(ProjectMetrics.class, metrics.getId());
            if (managedMetrics != null) {
                em.remove(managedMetrics);
                Trace.out(Trace.Level.INFO, "ProjectMetrics deleted successfully with JPA.");
            } else {
                Trace.out(Trace.Level.WAR, "ProjectMetrics with id " + metrics.getId() + " not found for deletion.");
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            Trace.out(Trace.Level.ERR, "Error deleting ProjectMetrics: " + e.getMessage());
            java.io.StringWriter sw = new java.io.StringWriter();
            e.printStackTrace(new java.io.PrintWriter(sw));
            Trace.out(Trace.Level.WAR, sw.toString());
        }
    }
    
    /**
     * Gets the total count of project metrics records.
     *
     * @return The number of project metrics records
     */
    public int getMetricsCount() {
        EntityManager em = MariaDbJpaConnection.getInstance();
        if (em == null) {
            Trace.out(Trace.Level.ERR, "Cannot get EntityManager. Cannot get count.");
            return 0;
        }
        
        try {
            return ((Number) em.createQuery("SELECT COUNT(p) FROM ProjectMetrics p").getSingleResult()).intValue();
        } catch (Exception e) {
            Trace.out(Trace.Level.ERR, "Error getting metrics count: " + e.getMessage());
            java.io.StringWriter sw = new java.io.StringWriter();
            e.printStackTrace(new java.io.PrintWriter(sw));
            Trace.out(Trace.Level.WAR, sw.toString());
            return 0;
        }
    }
}