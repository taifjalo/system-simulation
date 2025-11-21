package simu.backend.datasource;

import jakarta.persistence.*;
import simu.framework.Trace;

/**
 * Utility class for managing JPA EntityManager and database connections to MariaDB.
 * <p>
 * Provides singleton access to EntityManager, connection testing, and cleanup methods.
 */
public class MariaDbJpaConnection {


    /** Singleton EntityManagerFactory for JPA. */
    private static EntityManagerFactory emf = null;
    /** Singleton EntityManager for JPA. */
    private static EntityManager em = null;


    /**
     * Gets the singleton EntityManager instance for database operations.
     * @return the EntityManager instance, or null if creation fails
     */
    public static EntityManager getInstance() {
        // Note: Add synchronization if running in multi-threaded environment
        if (em == null) {
            if (emf == null) {
                try {
                    emf = Persistence.createEntityManagerFactory("ProjectMariaDbUnit");
                } catch (Exception e) {
                    Trace.out(Trace.Level.ERR, "Failed to create EntityManagerFactory: " + e.getMessage());
                    java.io.StringWriter sw = new java.io.StringWriter();
                    e.printStackTrace(new java.io.PrintWriter(sw));
                    Trace.out(Trace.Level.WAR, sw.toString());
                    return null;
                }
            }
            try {
                em = emf.createEntityManager();
            } catch (Exception e) {
                Trace.out(Trace.Level.ERR, "Failed to create EntityManager: " + e.getMessage());
                java.io.StringWriter sw = new java.io.StringWriter();
                e.printStackTrace(new java.io.PrintWriter(sw));
                Trace.out(Trace.Level.WAR, sw.toString());
                return null;
            }
        }
        return em;
    }
    
    /**
     * Tests the database connection by executing a simple query.
     * @return true if the connection is successful, false otherwise
     */
    public static boolean testConnection() {
        try {
            EntityManager testEm = getInstance();
            if (testEm != null) {
                // Try a simple query to test the connection using the new schema
                testEm.createNativeQuery("SELECT COUNT(*) FROM overview_statistics").getSingleResult();
                return true;
            }
        } catch (Exception e) {
            Trace.out(Trace.Level.ERR, "Database connection test failed: " + e.getMessage());
            java.io.StringWriter sw = new java.io.StringWriter();
            e.printStackTrace(new java.io.PrintWriter(sw));
            Trace.out(Trace.Level.WAR, sw.toString());
        }
        return false;
    }
    
    /**
     * Closes the EntityManager and EntityManagerFactory, cleaning up resources.
     */
    public static void close() {
        if (em != null) {
            try {
                em.close();
                em = null;
            } catch (Exception e) {
                Trace.out(Trace.Level.ERR, "Error closing EntityManager: " + e.getMessage());
            }
        }
        if (emf != null) {
            try {
                emf.close();
                emf = null;
            } catch (Exception e) {
                Trace.out(Trace.Level.ERR, "Error closing EntityManagerFactory: " + e.getMessage());
            }
        }
    }
}