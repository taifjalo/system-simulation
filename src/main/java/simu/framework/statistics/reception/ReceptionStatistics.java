
/**
 * ReceptionStatistics is a singleton class that extends ServicePointStatistics to provide
 * statistical tracking and database persistence for reception operations in the simulation.
 * It manages statistics such as utilization, throughput, queue length, and provides
 * methods to save and update these statistics in the database.
 *
 * @author (your name)
 */
package simu.framework.statistics.reception;

import simu.framework.Trace;
import simu.framework.statistics.ServicePointStatistics;
import simu.backend.dao.ReceptionStatisticsDao;

public class ReceptionStatistics extends ServicePointStatistics {

    /**
     * Data access object for persisting ReceptionStatistics entities.
     */
    private ReceptionStatisticsDao dao = new ReceptionStatisticsDao();

    /**
     * Singleton instance of ReceptionStatistics.
     */
    private static ReceptionStatistics INSTANCE;

    /**
     * Returns the singleton instance of ReceptionStatistics. If it does not exist, creates a new one.
     *
     * @return the singleton instance of ReceptionStatistics
     */
    public static ReceptionStatistics getInstance(){
        if (INSTANCE == null){
            INSTANCE = new ReceptionStatistics();
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
     * Displays the service point statistics for the reception at the given simulation time.
     *
     * @param time the current simulation time
     */
    @Override
    public void showServicePointStatistics(double time){
        Trace.out(Trace.Level.INFO, "\n=== RECEPTION STATISTICS ===");
        super.showServicePointStatistics(time);
    }

    /**
     * Creates and saves the current reception statistics to the database.
     * Calculates values such as utilization, throughput, and average queue length based on the total simulation time.
     *
     * @param overviewId the ID of the overview to associate with these statistics
     * @return the persisted ReceptionStatistics entity
     */
    public simu.backend.entity.ReceptionStatistics saveToDatabase(int overviewId) {
        double simulationTime = simu.framework.statistics.SimulationStatistics.getInstance().getTotalSimulationTime();
        simu.backend.entity.ReceptionStatistics entity = new simu.backend.entity.ReceptionStatistics(
            overviewId,
            getArrivedCustomers(),              // arrived_customers
            getServicedCustomers(),             // serviced_customers  
            (float) getServiceBusyTime(),       // service_busy_time
            (float) countServiceUtilization(simulationTime),  // service_utilization
            (float) countServiceThroughput(simulationTime),   // service_throughput
            (float) countAverageServiceTime(),  // average_service_time
            (float) getServiceWaitingTime(),    // waiting_time
            (float) countAverageQueueLength(simulationTime), // average_queue_length
            (float) getMean(),                  // mean_value
            (float) getVariance()               // variance_value
        );
        return dao.persist(entity);
    }

    /**
     * Updates an existing ReceptionStatistics entity in the database with the current statistics.
     *
     * @param entity the ReceptionStatistics entity to update
     * @return the updated ReceptionStatistics entity
     */
    public simu.backend.entity.ReceptionStatistics updateInDatabase(simu.backend.entity.ReceptionStatistics entity) {
        double simulationTime = simu.framework.statistics.SimulationStatistics.getInstance().getTotalSimulationTime();
        entity.setArrivedCustomers(getArrivedCustomers());
        entity.setServicedCustomers(getServicedCustomers());
        entity.setServiceBusyTime((float) getServiceBusyTime());
        entity.setServiceUtilization((float) countServiceUtilization(simulationTime));
        entity.setServiceThroughput((float) countServiceThroughput(simulationTime));
        entity.setAverageServiceTime((float) countAverageServiceTime());
        entity.setWaitingTime((float) getServiceWaitingTime());
        entity.setAverageQueueLength((float) countAverageQueueLength(simulationTime));
        entity.setMeanValue((float) getMean());
        entity.setVarianceValue((float) getVariance());
        return dao.update(entity);
    }
}
