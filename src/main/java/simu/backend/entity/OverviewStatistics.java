package simu.backend.entity;

import jakarta.persistence.*;

/**
 * Entity representing the overview_statistics table.
 * Contains all SimulationStatistics fields from the current schema for summary reporting and persistence.
 */
@Entity
@Table(name = "overview_statistics")
public class OverviewStatistics {
    /** Unique identifier for the overview statistics record. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    /** Total simulation time for this run. */
    @Column(name = "simulation_time")
    private float simulationTime;
    /** Cumulative simulation time across all runs. */
    @Column(name = "total_simulation_time")
    private float totalSimulationTime;
    /** Mean time between call-in arrivals. */
    @Column(name = "call_in_mean_time")
    private float callInMeanTime;
    /** Mean time between walk-in arrivals. */
    @Column(name = "walk_in_mean_time")
    private float walkInMeanTime;
    /** Total number of customers that arrived. */
    @Column(name = "total_arrived_customers")
    private int totalArrivedCustomers;
    /** Total number of customers that were serviced. */
    @Column(name = "total_serviced_customers")
    private int totalServicedCustomers;
    /** Number of customers who refused delivery. */
    @Column(name = "refused_delivery_customers")
    private int refusedDeliveryCustomers;
    /** Number of customers who returned for money. */
    @Column(name = "return_money_customers")
    private int returnMoneyCustomers;
    /** Number of customers whose orders were remade. */
    @Column(name = "remake_orders_customers")
    private int remakeOrdersCustomers;
    /** Total waiting time for all customers. */
    @Column(name = "total_waiting_time")
    private float totalWaitingTime;
    /** System throughput (customers per unit time). */
    @Column(name = "system_throughput")
    private float systemThroughput;
    /** Average response time for customers. */
    @Column(name = "average_response_time")
    private float averageResponseTime;
    /** Total busy time of the system. */
    @Column(name = "busy_time")
    private Float busyTime; // Use wrapper class to allow null values

    /**
     * Default constructor required by JPA.
     */
    public OverviewStatistics() {}

    /**
     * Constructor for creating new statistics with all fields.
     * @param simulationTime total simulation time for this run
     * @param totalSimulationTime cumulative simulation time across all runs
     * @param callInMeanTime mean time between call-in arrivals
     * @param walkInMeanTime mean time between walk-in arrivals
     * @param totalArrivedCustomers total number of customers that arrived
     * @param totalServicedCustomers total number of customers that were serviced
     * @param refusedDeliveryCustomers number of customers who refused delivery
     * @param returnMoneyCustomers number of customers who returned for money
     * @param remakeOrdersCustomers number of customers whose orders were remade
     * @param totalWaitingTime total waiting time for all customers
     * @param systemThroughput system throughput (customers per unit time)
     * @param averageResponseTime average response time for customers
     * @param busyTime total busy time of the system
     */
    public OverviewStatistics(float simulationTime, float totalSimulationTime, 
                            float callInMeanTime, float walkInMeanTime,
                            int totalArrivedCustomers, int totalServicedCustomers,
                            int refusedDeliveryCustomers, int returnMoneyCustomers,
                            int remakeOrdersCustomers, float totalWaitingTime, 
                            float systemThroughput, float averageResponseTime, Float busyTime) {
        this.simulationTime = simulationTime;
        this.totalSimulationTime = totalSimulationTime;
        this.callInMeanTime = callInMeanTime;
        this.walkInMeanTime = walkInMeanTime;
        this.totalArrivedCustomers = totalArrivedCustomers;
        this.totalServicedCustomers = totalServicedCustomers;
        this.refusedDeliveryCustomers = refusedDeliveryCustomers;
        this.returnMoneyCustomers = returnMoneyCustomers;
        this.remakeOrdersCustomers = remakeOrdersCustomers;
        this.totalWaitingTime = totalWaitingTime;
        this.systemThroughput = systemThroughput;
        this.averageResponseTime = averageResponseTime;
        this.busyTime = busyTime;
    }

    // Getters and setters
    /**
     * Gets the unique identifier for the overview statistics record.
     * @return the id of the overview statistics record
     */
    public int getId() { return id; }
    /**
     * Sets the unique identifier for the overview statistics record.
     * @param id the new id for the overview statistics record
     */
    public void setId(int id) { this.id = id; }
    
    /**
     * Gets the total simulation time for this run.
     * @return the simulation time for this run
     */
    public float getSimulationTime() { return simulationTime; }
    /**
     * Sets the total simulation time for this run.
     * @param simulationTime the new simulation time for this run
     */
    public void setSimulationTime(float simulationTime) { this.simulationTime = simulationTime; }
    
    /**
     * Gets the cumulative simulation time across all runs.
     * @return the total simulation time across all runs
     */
    public float getTotalSimulationTime() { return totalSimulationTime; }
    /**
     * Sets the cumulative simulation time across all runs.
     * @param totalSimulationTime the new total simulation time across all runs
     */
    public void setTotalSimulationTime(float totalSimulationTime) { this.totalSimulationTime = totalSimulationTime; }
    
    /**
     * Gets the mean time between call-in arrivals.
     * @return the mean time between call-in arrivals
     */
    public float getCallInMeanTime() { return callInMeanTime; }
    /**
     * Sets the mean time between call-in arrivals.
     * @param callInMeanTime the new mean time between call-in arrivals
     */
    public void setCallInMeanTime(float callInMeanTime) { this.callInMeanTime = callInMeanTime; }
    
    /**
     * Gets the mean time between walk-in arrivals.
     * @return the mean time between walk-in arrivals
     */
    public float getWalkInMeanTime() { return walkInMeanTime; }
    /**
     * Sets the mean time between walk-in arrivals.
     * @param walkInMeanTime the new mean time between walk-in arrivals
     */
    public void setWalkInMeanTime(float walkInMeanTime) { this.walkInMeanTime = walkInMeanTime; }
    
    /**
     * Gets the total number of customers that arrived.
     * @return the total number of arrived customers
     */
    public int getTotalArrivedCustomers() { return totalArrivedCustomers; }
    /**
     * Sets the total number of customers that arrived.
     * @param totalArrivedCustomers the new total number of arrived customers
     */
    public void setTotalArrivedCustomers(int totalArrivedCustomers) { this.totalArrivedCustomers = totalArrivedCustomers; }
    
    /**
     * Gets the total number of customers that were serviced.
     * @return the total number of serviced customers
     */
    public int getTotalServicedCustomers() { return totalServicedCustomers; }
    /**
     * Sets the total number of customers that were serviced.
     * @param totalServicedCustomers the new total number of serviced customers
     */
    public void setTotalServicedCustomers(int totalServicedCustomers) { this.totalServicedCustomers = totalServicedCustomers; }
    
    /**
     * Gets the number of customers who refused delivery.
     * @return the number of customers who refused delivery
     */
    public int getRefusedDeliveryCustomers() { return refusedDeliveryCustomers; }
    /**
     * Sets the number of customers who refused delivery.
     * @param refusedDeliveryCustomers the new number of customers who refused delivery
     */
    public void setRefusedDeliveryCustomers(int refusedDeliveryCustomers) { this.refusedDeliveryCustomers = refusedDeliveryCustomers; }
    
    /**
     * Gets the number of customers who returned for money.
     * @return the number of customers who returned for money
     */
    public int getReturnMoneyCustomers() { return returnMoneyCustomers; }
    /**
     * Sets the number of customers who returned for money.
     * @param returnMoneyCustomers the new number of customers who returned for money
     */
    public void setReturnMoneyCustomers(int returnMoneyCustomers) { this.returnMoneyCustomers = returnMoneyCustomers; }
    
    /**
     * Gets the number of customers whose orders were remade.
     * @return the number of customers whose orders were remade
     */
    public int getRemakeOrdersCustomers() { return remakeOrdersCustomers; }
    /**
     * Sets the number of customers whose orders were remade.
     * @param remakeOrdersCustomers the new number of customers whose orders were remade
     */
    public void setRemakeOrdersCustomers(int remakeOrdersCustomers) { this.remakeOrdersCustomers = remakeOrdersCustomers; }
    
    /**
     * Gets the total waiting time for all customers.
     * @return the total waiting time for all customers
     */
    public float getTotalWaitingTime() { return totalWaitingTime; }
    /**
     * Sets the total waiting time for all customers.
     * @param totalWaitingTime the new total waiting time for all customers
     */
    public void setTotalWaitingTime(float totalWaitingTime) { this.totalWaitingTime = totalWaitingTime; }
    
    /**
     * Gets the system throughput (customers per unit time).
     * @return the system throughput
     */
    public float getSystemThroughput() { return systemThroughput; }
    /**
     * Sets the system throughput (customers per unit time).
     * @param systemThroughput the new system throughput
     */
    public void setSystemThroughput(float systemThroughput) { this.systemThroughput = systemThroughput; }
    
    /**
     * Gets the average response time for customers.
     * @return the average response time for customers
     */
    public float getAverageResponseTime() { return averageResponseTime; }
    /**
     * Sets the average response time for customers.
     * @param averageResponseTime the new average response time for customers
     */
    public void setAverageResponseTime(float averageResponseTime) { this.averageResponseTime = averageResponseTime; }
    
    /**
     * Gets the total busy time of the system.
     * @return the total busy time of the system
     */
    public Float getBusyTime() { return busyTime; }
    /**
     * Sets the total busy time of the system.
     * @param busyTime the new total busy time of the system
     */
    public void setBusyTime(Float busyTime) { this.busyTime = busyTime; }
    
    /**
     * Helper method to get busy time as float with default value for null.
     * @return busy time or 0.0f if null
     */
    public float getBusyTimeOrDefault() { 
        return busyTime != null ? busyTime : 0.0f; 
    }

    /**
     * Compatibility method for old code that uses arrival count terminology.
     * @return total arrived customers
     */
    public int getArrivalCount() { return totalArrivedCustomers; }
    public void setArrivalCount(int arrivalCount) { this.totalArrivedCustomers = arrivalCount; }

    /**
     * Compatibility method for old code that uses completed count terminology.
     * @return total serviced customers
     */
    public int getCompletedCount() { return totalServicedCustomers; }
    public void setCompletedCount(int completedCount) { this.totalServicedCustomers = completedCount; }

    /**
     * Compatibility method for old code that uses total time terminology.
     * @return simulation time
     */
    public float getTotalTime() { return simulationTime; }
    public void setTotalTime(float totalTime) { this.simulationTime = totalTime; }

    /**
     * Compatibility method for old code that uses cumulative response time terminology.
     * @return total waiting time
     */
    public float getCumulativeResponseTime() { return totalWaitingTime; }
    public void setCumulativeResponseTime(float cumulativeResponseTime) { this.totalWaitingTime = cumulativeResponseTime; }

    /**
     * Calculates system utilization as percentage of time system was busy.
     * @return utilization percentage
     */
    public float getUtilization() { 
        if (simulationTime > 0 && totalServicedCustomers > 0) {
            float totalServiceTime = totalServicedCustomers * (totalWaitingTime / Math.max(totalServicedCustomers, 1));
            return Math.min(100, (totalServiceTime / simulationTime) * 100);
        }
        return 0; 
    }

    /**
     * Calculates actual throughput if not set in database.
     * @return throughput value
     */
    public float getThroughput() { 
        if (systemThroughput <= 0 && simulationTime > 0) {
            return totalServicedCustomers / simulationTime;
        }
        return systemThroughput; 
    }

    /**
     * Calculates average service time from total waiting time.
     * @return average service time
     */
    public float getAvgServiceTime() {
        return totalServicedCustomers > 0 ? totalWaitingTime / totalServicedCustomers : 0;
    }

    /**
     * Calculates actual response time if not set in database.
     * @return average response time
     */
    public float getAvgResponseTime() { 
        if (averageResponseTime <= 0 && totalServicedCustomers > 0) {
            return totalWaitingTime / totalServicedCustomers;
        }
        return averageResponseTime; 
    }

    /**
     * Calculates average queue length from waiting time and simulation time.
     * @return average queue length
     */
    public float getAvgQueueLength() {
        return simulationTime > 0 ? totalWaitingTime / simulationTime : 0;
    }

    @Override
    public String toString() {
        return "OverviewStatistics{" +
                "id=" + id +
                ", simulationTime=" + simulationTime +
                ", totalSimulationTime=" + totalSimulationTime +
                ", totalArrivedCustomers=" + totalArrivedCustomers +
                ", totalServicedCustomers=" + totalServicedCustomers +
                '}';
    }
}