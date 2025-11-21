
/**
 * SimulationStatistics is a singleton class that tracks and manages overall simulation statistics.
 * It provides methods for incrementing and retrieving various metrics, resetting statistics for new simulations,
 * and saving results to the database. It also coordinates the reset and persistence of all service point statistics.
 *
 * @author (your name)
 */
package simu.framework.statistics;

import simu.framework.Trace;
import simu.backend.dao.OverviewStatisticsDao;


/**
 * Tracks and manages overall simulation statistics, including customer counts, times, and system metrics.
 * Provides singleton access and coordinates persistence and reset of all service point statistics.
 */
public class SimulationStatistics {


    /** Data access object for persisting overview statistics. */
    private OverviewStatisticsDao dao = new OverviewStatisticsDao();

    /** Current simulation time (configuration value). */
    private double simulationTime;

    /** Total simulation time for the run. */
    private double totalSimulationTime;

    /** Mean time for call-in arrivals (user configuration). */
    private double callInMeanTime;

    /** Mean time for walk-in arrivals (user configuration). */
    private double walkInMeanTime;

    /** Total number of customers that arrived during the simulation. */
    private int totalArrivedCustomers;

    /** Total number of customers that were serviced during the simulation. */
    private int totalServicedCustomers;

    /** Number of customers whose delivery was refused. */
    private int refusedDeliveryCustomers;

    /** Number of customers who received a return of money. */
    private int returnMoneyCustomers;

    /** Number of orders that were remade. */
    private int remakeOrdersCustomers;

    /** Total waiting time for all customers in the system. */
    private double totalWaitingTime;

    /** System throughput (serviced customers / total simulation time). */
    private double systemThrougput;

    /** Average response time for all serviced customers. */
    private double averageResponseTime;

    /** Singleton instance of SimulationStatistics. */
    private static SimulationStatistics INSTANCE;


    /**
     * Constructs a new SimulationStatistics object with default values.
     * Default mean times are set for call-in and walk-in arrivals.
     */
    public SimulationStatistics() {
        simulationTime = 0.0;
        totalSimulationTime = 0.0;
        callInMeanTime = 10.0;  // Default positive value for Negexp distribution
        walkInMeanTime = 15.0;  // Default positive value for Negexp distribution
        totalArrivedCustomers = 0;
        totalServicedCustomers = 0;
        refusedDeliveryCustomers = 0;
        returnMoneyCustomers = 0;
        remakeOrdersCustomers = 0;
        totalWaitingTime = 0.0;
        systemThrougput = 0.0;
        averageResponseTime = 0.0;
    }


    /**
     * Returns the current simulation time (configuration value).
     * @return the simulation time
     */
    public double getSimulationTime() {
        return simulationTime;
    }


    /**
     * Sets the current simulation time (configuration value).
     * @param simulationTime the simulation time to set
     */
    public void setSimulationTime(double simulationTime) {
        this.simulationTime = simulationTime;
    }


    /**
     * Sets the total simulation time for the run.
     * @param simulationTime the total simulation time to set
     */
    public void setTotalSimulationTime(double simulationTime) {
        this.totalSimulationTime = simulationTime;
    }


    /**
     * Returns the total simulation time for the run.
     * @return the total simulation time
     */
    public double getTotalSimulationTime() {
        return totalSimulationTime;
    }


    /**
     * Sets the mean time for walk-in arrivals.
     * @param walkInMeanTime the mean time to set
     */
    public void setWalkInMeanTime(double walkInMeanTime) {
        this.walkInMeanTime = walkInMeanTime;
    }


    /**
     * Returns the mean time for walk-in arrivals.
     * @return the walk-in mean time
     */
    public double getWalkInMeanTime() {
        return walkInMeanTime;
    }


    /**
     * Sets the mean time for call-in arrivals.
     * @param callInMeanTime the mean time to set
     */
    public void setCallInMeanTime(double callInMeanTime) {
        this.callInMeanTime = callInMeanTime;
    }


    /**
     * Returns the mean time for call-in arrivals.
     * @return the call-in mean time
     */
    public double getCallInMeanTime() {
        return callInMeanTime;
    }


    /**
     * Increments the total number of arrived customers by one.
     */
    public void incrementTotalArrivedCustomers() {
        totalArrivedCustomers++;
    }


    /**
     * Returns the total number of customers that arrived during the simulation.
     * @return the total arrived customers
     */
    public int getTotalArrivedCustomers() {
        return totalArrivedCustomers;
    }


    /**
     * Increments the total number of serviced customers by one.
     */
    public void incrementTotalServicedCustomers() {
        totalServicedCustomers++;
    }


    /**
     * Increments the number of remade orders by one and logs the event.
     */
    public void incrementRemakeOrdersCustomers() {
        remakeOrdersCustomers++;
        // Tracing kept at INFO level if enabled via Trace
        Trace.out(Trace.Level.INFO, "Order remade. Total remake orders: " + remakeOrdersCustomers);
    }



    /**
     * Returns the total number of customers that were serviced during the simulation.
     * @return the total serviced customers
     */
    public int getTotalServicedCustomers() {
        return totalServicedCustomers;
    }


    /**
     * Increments the number of refused delivery customers by one and logs the event.
     */
    public void incrementRefusedDeliveryCustomers() {
        refusedDeliveryCustomers++;
        Trace.out(Trace.Level.INFO, "Delivery refused. Total refused deliveries: " + refusedDeliveryCustomers);
    }


    /**
     * Returns the number of customers whose delivery was refused.
     * @return the number of refused delivery customers
     */
    public int getRefusedDeliveryCustomers() {
        return refusedDeliveryCustomers;
    }


    /**
     * Increments the number of return money customers by one and logs the event.
     */
    public void incrementReturnMoneyCustomers() {
        returnMoneyCustomers++;
        Trace.out(Trace.Level.INFO, "Money returned. Total return money events: " + returnMoneyCustomers);
    }


    /**
     * Returns the number of customers who received a return of money.
     * @return the number of return money customers
     */
    public int getReturnMoneyCustomers() {
        return returnMoneyCustomers;
    }


    /**
     * Returns the number of orders that were remade.
     * @return the number of remade orders
     */
    public int getRemakeOrdersCustomers() {
        return remakeOrdersCustomers;
    }


    /**
     * Adds the given time to the total waiting time for all customers.
     * @param time the waiting time to add
     */
    public void addTotalWaitingTime(double time) {
        totalWaitingTime += time;
    }


    /**
     * Calculates and returns the system throughput (serviced customers / total simulation time).
     * @return the system throughput
     */
    public double countSystemThroughput() {
        systemThrougput = totalSimulationTime > 0 ? totalServicedCustomers / totalSimulationTime : 0.0;
        return systemThrougput;
    }


    /**
     * Calculates and returns the average response time for all serviced customers.
     * @return the average response time
     */
    public double countSystemAverageResponseTime() {
        averageResponseTime = totalServicedCustomers > 0 ? totalWaitingTime / totalServicedCustomers : 0.0;
        return averageResponseTime;
    }


    /**
     * Displays the current simulation statistics using the Trace utility.
     * @param time the total simulation time
     */
    public void showSimulationStatistics(Double time){
        totalSimulationTime = time;
        Trace.out(Trace.Level.INFO, "\n=== SIMULATION STATISTICS ===");
        Trace.out(Trace.Level.INFO, "Total simulation time " + totalSimulationTime);
        Trace.out(Trace.Level.INFO, "Total arrived customers: " + totalArrivedCustomers);
        Trace.out(Trace.Level.INFO, "Total serviced customers: " + totalServicedCustomers);
        Trace.out(Trace.Level.INFO, "Refused delivery customers: " + refusedDeliveryCustomers);
        Trace.out(Trace.Level.INFO, "Return money customers: " + returnMoneyCustomers);
        Trace.out(Trace.Level.INFO, "Remake order customers: " + remakeOrdersCustomers);
        Trace.out(Trace.Level.INFO, "Total waiting time: " + totalWaitingTime);
        Trace.out(Trace.Level.INFO, "System throughput: " + countSystemThroughput());
        Trace.out(Trace.Level.INFO, "Average response time: " + countSystemAverageResponseTime());
    }


    /**
     * Returns the singleton instance of SimulationStatistics. If it does not exist, creates a new one.
     * @return the singleton instance of SimulationStatistics
     */
    public static SimulationStatistics getInstance(){
        if (INSTANCE == null){
            INSTANCE = new SimulationStatistics();
        }
        return INSTANCE;
    }
    

    /**
     * Resets all statistics to initial values for a new simulation.
     * Note: simulationTime is NOT reset here as it's a configuration value, not a runtime statistic.
     */
    public void resetStatistics() {
        totalSimulationTime = 0.0;
        totalArrivedCustomers = 0;
        totalServicedCustomers = 0;
        refusedDeliveryCustomers = 0;
        returnMoneyCustomers = 0;
        remakeOrdersCustomers = 0;
        totalWaitingTime = 0.0;
        systemThrougput = 0.0;
        averageResponseTime = 0.0;
    }
    

    /**
     * Resets all statistics singleton instances for a new simulation, including all service point statistics.
     */
    public static void resetAllStatistics() {
        // Reset main statistics
        if (INSTANCE != null) {
            INSTANCE.resetStatistics();
        }
        // Reset all service point statistics
        simu.framework.statistics.reception.ReceptionStatistics.resetInstance();
        simu.framework.statistics.kitchen.KitchenStatistics.resetInstance();
        simu.framework.statistics.counter.CounterStatistics.resetInstance();
        simu.framework.statistics.delivery.DeliveryStatistics.resetInstance();
    }
    

    /**
     * Saves complete simulation statistics to the database, including overview and all service point statistics.
     * Calculates total busy time and final metrics before saving. Also persists each service point's statistics.
     *
     * @return the persisted OverviewStatistics entity
     */
    public simu.backend.entity.OverviewStatistics saveToDatabase() {
        // Calculate total busy time from all service points
        double totalBusyTime = 0.0;
        // Get instances of all service point statistics
        simu.framework.statistics.reception.ReceptionStatistics receptionStats = 
            simu.framework.statistics.reception.ReceptionStatistics.getInstance();
        simu.framework.statistics.kitchen.KitchenStatistics kitchenStats = 
            simu.framework.statistics.kitchen.KitchenStatistics.getInstance();
        simu.framework.statistics.counter.CounterStatistics counterStats = 
            simu.framework.statistics.counter.CounterStatistics.getInstance();
        simu.framework.statistics.delivery.DeliveryStatistics deliveryStats = 
            simu.framework.statistics.delivery.DeliveryStatistics.getInstance();
        // Optional tracing of service point instances and counts
        Trace.out(Trace.Level.WAR, "Instance check at save: reception=" + System.identityHashCode(receptionStats)
            + ", served=" + receptionStats.getServicedCustomers() + ", busyTime=" + receptionStats.getServiceBusyTime());
        Trace.out(Trace.Level.WAR, "Instance check at save: kitchen=" + System.identityHashCode(kitchenStats)
            + ", served=" + kitchenStats.getServicedCustomers() + ", busyTime=" + kitchenStats.getServiceBusyTime());
        Trace.out(Trace.Level.WAR, "Instance check at save: counter=" + System.identityHashCode(counterStats)
            + ", served=" + counterStats.getServicedCustomers() + ", busyTime=" + counterStats.getServiceBusyTime());
        Trace.out(Trace.Level.WAR, "Instance check at save: delivery=" + System.identityHashCode(deliveryStats)
            + ", served=" + deliveryStats.getServicedCustomers() + ", busyTime=" + deliveryStats.getServiceBusyTime());
        totalBusyTime = receptionStats.getServiceBusyTime() + 
                       kitchenStats.getServiceBusyTime() + 
                       counterStats.getServiceBusyTime() + 
                       deliveryStats.getServiceBusyTime();
        Trace.out(Trace.Level.INFO, "Reception busy time: " + receptionStats.getServiceBusyTime());
        Trace.out(Trace.Level.INFO, "Kitchen busy time: " + kitchenStats.getServiceBusyTime());
        Trace.out(Trace.Level.INFO, "Counter busy time: " + counterStats.getServiceBusyTime());
        Trace.out(Trace.Level.INFO, "Delivery busy time: " + deliveryStats.getServiceBusyTime());
        Trace.out(Trace.Level.INFO, "Total busy time: " + totalBusyTime);
        Trace.out(Trace.Level.INFO, "Total simulation time: " + totalSimulationTime);
        // Calculate final metrics before saving
        countSystemThroughput();
        countSystemAverageResponseTime();
        // Create and save overview statistics using new schema
        simu.backend.entity.OverviewStatistics overviewEntity = new simu.backend.entity.OverviewStatistics(
            (float) simulationTime,           // simulation_time
            (float) totalSimulationTime,      // total_simulation_time
            (float) callInMeanTime,           // call_in_mean_time
            (float) walkInMeanTime,           // walk_in_mean_time
            totalArrivedCustomers,            // total_arrived_customers
            totalServicedCustomers,           // total_serviced_customers
            refusedDeliveryCustomers,         // refused_delivery_customers
            returnMoneyCustomers,             // return_money_customers
            remakeOrdersCustomers,            // remake_orders_customers
            (float) totalWaitingTime,         // total_waiting_time
            (float) systemThrougput,          // system_throughput
            (float) averageResponseTime,      // average_response_time
            (float) totalBusyTime             // busy_time
        );
        simu.backend.entity.OverviewStatistics savedOverview = dao.persist(overviewEntity);
        // Save individual service point statistics
        if (savedOverview != null) {
            int overviewId = savedOverview.getId();
            receptionStats.saveToDatabase(overviewId);
            kitchenStats.saveToDatabase(overviewId);
            counterStats.saveToDatabase(overviewId);
            deliveryStats.saveToDatabase(overviewId);
        }
        return savedOverview;
    }

}