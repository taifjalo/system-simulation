package simu.backend.application;

import simu.backend.datasource.MariaDbJpaConnection;

// New normalized schema imports
import simu.backend.entity.OverviewStatistics;
import simu.backend.entity.ReceptionStatistics;
import simu.backend.entity.KitchenStatistics;
import simu.backend.entity.CounterStatistics;
import simu.backend.entity.DeliveryStatistics;
import simu.backend.dao.OverviewStatisticsDao;
import simu.backend.dao.ReceptionStatisticsDao;
import simu.backend.dao.KitchenStatisticsDao;
import simu.backend.dao.CounterStatisticsDao;
import simu.backend.dao.DeliveryStatisticsDao;

/**
 * Standalone application for testing and demonstrating the simulation database schema.
 * <p>
 * Connects to MariaDB, tests connection, and performs CRUD operations on normalized schema entities.
 * Used for development and debugging of the backend database layer.
 */
public class DatabaseApp {


    /**
     * Main entry point for the database application.
     * Connects to the database, tests the normalized schema, and closes the connection.
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        System.out.println("=== Simulation Database Application (JPA) ===");
        // Test connection first
        if (!MariaDbJpaConnection.testConnection()) {
            System.err.println("Cannot connect to database. Exiting.");
            return;
        }
        System.out.println("Database connection successful!");
        // Test only the new normalized schema system
        testNormalizedSchema();
        // Clean up
        MariaDbJpaConnection.close();
        System.out.println("\nDatabase connection closed.");
    }
    

    /**
     * Tests the new normalized schema by performing direct database operations.
     * Catches and prints any exceptions.
     */
    private static void testNormalizedSchema() {
        System.out.println("\n=== Testing New Normalized Schema ===");
        try {
            // Test direct database operations
            testDirectDatabaseOperations();
        } catch (Exception e) {
            System.err.println("Error testing normalized schema: " + e.getMessage());
            e.printStackTrace();
        }
    }
    

    /**
     * Performs direct CRUD operations on all normalized schema entities for testing.
     * Persists and retrieves statistics for overview, reception, kitchen, counter, and delivery.
     * Prints calculated metrics if available.
     */
    private static void testDirectDatabaseOperations() {
        System.out.println("\n1. Testing direct database operations...");
        // Test Overview Statistics
        OverviewStatisticsDao overviewDao = new OverviewStatisticsDao();
        OverviewStatistics overview = new OverviewStatistics();
        overview.setArrivalCount(100);
        overview.setCompletedCount(95);
        overview.setTotalTime(1500.0f);
        overview.setCumulativeResponseTime(2800.0f);
        OverviewStatistics savedOverview = overviewDao.persist(overview);
        int overviewId = savedOverview.getId();
        System.out.println("Saved overview statistics with ID: " + overviewId);
        // Test Reception Statistics
        ReceptionStatisticsDao receptionDao = new ReceptionStatisticsDao();
        ReceptionStatistics reception = new ReceptionStatistics();
        reception.setOverviewId(overviewId);
        reception.setTotalArrivals(50);
        reception.setTotalWaitTime(150.0f);
        reception.setAvgWaitTime(3.0f);
        receptionDao.persist(reception);
        System.out.println("Saved reception statistics");
        // Test Kitchen Statistics  
        KitchenStatisticsDao kitchenDao = new KitchenStatisticsDao();
        KitchenStatistics kitchen = new KitchenStatistics();
        kitchen.setOverviewId(overviewId);
        kitchen.setTotalOrders(80);
        kitchen.setTotalPrepTime(400.0f);
        kitchen.setAvgPrepTime(5.0f);
        kitchenDao.persist(kitchen);
        System.out.println("Saved kitchen statistics");
        // Test Counter Statistics
        CounterStatisticsDao counterDao = new CounterStatisticsDao();
        CounterStatistics counter = new CounterStatistics();
        counter.setOverviewId(overviewId);
        counter.setTotalCustomersServed(75);
        counter.setTotalServiceTime(225.0f);
        counter.setAvgServiceTime(3.0f);
        counterDao.persist(counter);
        System.out.println("Saved counter statistics");
        // Test Delivery Statistics
        DeliveryStatisticsDao deliveryDao = new DeliveryStatisticsDao();
        DeliveryStatistics delivery = new DeliveryStatistics();
        delivery.setOverviewId(overviewId);
        delivery.setTotalDeliveries(25);
        delivery.setTotalDeliveryDuration(500.0f);
        delivery.setAvgDeliveryDuration(20.0f);
        deliveryDao.persist(delivery);
        System.out.println("Saved delivery statistics");
        // Retrieve and display the overview with calculated metrics
        OverviewStatistics retrievedOverview = overviewDao.findById(overviewId);
        if (retrievedOverview != null) {
            System.out.println("\nRetrieved Overview Statistics:");
            // Check if derived metrics are available (they should be calculated by database)
            if (retrievedOverview.getUtilization() > 0) {
                System.out.println("  Utilization: " + String.format("%.2f%%", retrievedOverview.getUtilization()));
            } else {
                System.out.println("  Utilization: Not calculated yet");
            }
            if (retrievedOverview.getThroughput() > 0) {
                System.out.println("  Throughput: " + String.format("%.4f", retrievedOverview.getThroughput()));
            } else {
                System.out.println("  Throughput: Not calculated yet");
            }
            if (retrievedOverview.getAvgServiceTime() > 0) {
                System.out.println("  Avg Service Time: " + String.format("%.2f", retrievedOverview.getAvgServiceTime()));
            } else {
                System.out.println("  Avg Service Time: Not calculated yet");
            }
            if (retrievedOverview.getAvgResponseTime() > 0) {
                System.out.println("  Avg Response Time: " + String.format("%.2f", retrievedOverview.getAvgResponseTime()));
            } else {
                System.out.println("  Avg Response Time: Not calculated yet");
            }
            if (retrievedOverview.getAvgQueueLength() > 0) {
                System.out.println("  Avg Queue Length: " + String.format("%.2f", retrievedOverview.getAvgQueueLength()));
            } else {
                System.out.println("  Avg Queue Length: Not calculated yet");
            }
        }
    }
}