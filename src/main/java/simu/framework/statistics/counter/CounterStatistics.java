
/**
 * CounterStatistics is a singleton class that extends ServicePointStatistics to provide
 * statistical tracking and database persistence for a service counter in the simulation.
 * It manages statistics such as utilization, throughput, queue length, and provides
 * methods to save and update these statistics in the database.
 *
 * @author (your name)
 */
package simu.framework.statistics.counter;

import simu.framework.Trace;
import simu.framework.statistics.ServicePointStatistics;
import simu.backend.dao.CounterStatisticsDao;

public class CounterStatistics extends ServicePointStatistics{

    /**
     * Data access object for persisting CounterStatistics entities.
     */
    private CounterStatisticsDao dao = new CounterStatisticsDao();

    /**
     * Singleton instance of CounterStatistics.
     */
    private static CounterStatistics INSTANCE;

    /**
     * Returns the singleton instance of CounterStatistics. If it does not exist, creates a new one.
     *
     * @return the singleton instance of CounterStatistics
     */
    public static CounterStatistics getInstance(){
        if (INSTANCE == null){
            INSTANCE = new CounterStatistics();
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
     * Displays the service point statistics for the counter at the given simulation time.
     *
     * @param time the current simulation time
     */
    @Override
    public void showServicePointStatistics(double time){
        Trace.out(Trace.Level.INFO, "\n=== COUNTER STATISTICS ===");
        super.showServicePointStatistics(time);
    }

    /**
     * Creates and saves the current counter statistics to the database.
     * Calculates derived values such as utilization, throughput, and average queue length.
     *
     * @param overviewId the ID of the overview to associate with these statistics
     * @return the persisted CounterStatistics entity
     */
    public simu.backend.entity.CounterStatistics saveToDatabase(int overviewId) {
        // Calculate derived values
        double utilization = getServicedCustomers() > 0 ? (getServiceBusyTime() / getServicedCustomers()) * 100 : 0;
        double throughput = getServicedCustomers() > 0 ? getServicedCustomers() / getServiceBusyTime() : 0;
        double avgQueueLength = getServicedCustomers() > 0 ? getServiceWaitingTime() / getServicedCustomers() : 0;

        simu.backend.entity.CounterStatistics entity = new simu.backend.entity.CounterStatistics(
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
     * Updates an existing CounterStatistics entity in the database with the current statistics.
     *
     * @param entity the CounterStatistics entity to update
     * @return the updated CounterStatistics entity
     */
    public simu.backend.entity.CounterStatistics updateInDatabase(simu.backend.entity.CounterStatistics entity) {
        entity.setTotalCustomersServed(getServicedCustomers());
        entity.setTotalServiceTime((float) getServiceBusyTime());
        entity.setAvgServiceTime((float) countAverageServiceTime());
        return dao.update(entity);
    }
}