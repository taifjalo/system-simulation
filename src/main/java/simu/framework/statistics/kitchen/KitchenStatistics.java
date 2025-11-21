
/**
 * KitchenStatistics is a singleton class that extends ServicePointStatistics to provide
 * statistical tracking and database persistence for kitchen operations in the simulation.
 * It manages statistics such as utilization, throughput, queue length, and provides
 * methods to save and update these statistics in the database.
 *
 * @author (your name)
 */
package simu.framework.statistics.kitchen;

import simu.framework.Trace;
import simu.framework.statistics.ServicePointStatistics;
import simu.backend.dao.KitchenStatisticsDao;

public class KitchenStatistics extends ServicePointStatistics {

    /**
     * Data access object for persisting KitchenStatistics entities.
     */
    private KitchenStatisticsDao dao = new KitchenStatisticsDao();

    /**
     * Singleton instance of KitchenStatistics.
     */
    private static KitchenStatistics INSTANCE;

    /**
     * Returns the singleton instance of KitchenStatistics. If it does not exist, creates a new one.
     *
     * @return the singleton instance of KitchenStatistics
     */
    public static KitchenStatistics getInstance(){
        if (INSTANCE == null){
            INSTANCE = new KitchenStatistics();
        }
        return INSTANCE;
    }

    /**
     * Resets the runtime data of the singleton instance for a new simulation.
     * If the instance does not exist, it will be created with default configuration values.
     */
    public static void resetInstance() {
        // Instead of nullifying instance, reset only runtime data
        if (INSTANCE != null) {
            INSTANCE.resetRuntimeData();
        }
        // If instance is null, it will be created with default config values
    }

    /**
     * Displays the service point statistics for the kitchen at the given simulation time.
     *
     * @param time the current simulation time
     */
    @Override
    public void showServicePointStatistics(double time){
        Trace.out(Trace.Level.INFO, "\n=== KITCHEN STATISTICS ===");
        super.showServicePointStatistics(time);
    }

    /**
     * Creates and saves the current kitchen statistics to the database.
     * Calculates derived values such as utilization, throughput, and average queue length.
     *
     * @param overviewId the ID of the overview to associate with these statistics
     * @return the persisted KitchenStatistics entity
     */
    public simu.backend.entity.KitchenStatistics saveToDatabase(int overviewId) {
        // Calculate derived values
        double utilization = getServicedCustomers() > 0 ? (getServiceBusyTime() / getServicedCustomers()) * 100 : 0;
        double throughput = getServicedCustomers() > 0 ? getServicedCustomers() / getServiceBusyTime() : 0;
        double avgQueueLength = getServicedCustomers() > 0 ? getServiceWaitingTime() / getServicedCustomers() : 0;

        simu.backend.entity.KitchenStatistics entity = new simu.backend.entity.KitchenStatistics(
            overviewId,
            getArrivedCustomers(),        // arrived_customers
            getServicedCustomers(),       // serviced_customers  
            (float) getServiceBusyTime(), // service_busy_time
            (float) utilization,          // service_utilization
            (float) throughput,           // service_throughput
            (float) countAverageServiceTime(), // average_service_time
            (float) getServiceWaitingTime(),   // waiting_time
            (float) avgQueueLength,       // average_queue_length
            (float) getMean(),            // mean_value
            (float) getVariance()         // variance_value
        );
        return dao.persist(entity);
    }

    /**
     * Updates an existing KitchenStatistics entity in the database with the current statistics.
     *
     * @param entity the KitchenStatistics entity to update
     * @return the updated KitchenStatistics entity
     */
    public simu.backend.entity.KitchenStatistics updateInDatabase(simu.backend.entity.KitchenStatistics entity) {
        entity.setTotalOrders(getServicedCustomers());
        entity.setTotalPrepTime((float) getServiceBusyTime());
        entity.setAvgPrepTime((float) countAverageServiceTime());
        return dao.update(entity);
    }
}