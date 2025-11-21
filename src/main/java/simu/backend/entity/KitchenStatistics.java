package simu.backend.entity;

import jakarta.persistence.*;

/**
 * Entity representing the kitchen_statistics table.
 * Contains all ServicePointStatistics fields from the current schema for kitchen performance reporting and persistence.
 */
@Entity
@Table(name = "kitchen_statistics")
public class KitchenStatistics {
    /** Unique identifier for the kitchen statistics record. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    /** Foreign key to the overview statistics record. */
    @Column(name = "overview_id")
    private int overviewId;
    /** Number of customers that arrived at the kitchen. */
    @Column(name = "arrived_customers")
    private int arrivedCustomers;
    /** Number of customers serviced by the kitchen. */
    @Column(name = "serviced_customers")
    private int servicedCustomers;
    /** Total busy time for kitchen service. */
    @Column(name = "service_busy_time")
    private float serviceBusyTime;
    /** Utilization percentage of the kitchen service. */
    @Column(name = "service_utilization")
    private float serviceUtilization;
    /** Throughput of the kitchen service (orders per unit time). */
    @Column(name = "service_throughput")
    private float serviceThroughput;
    /** Average service time per order in the kitchen. */
    @Column(name = "average_service_time")
    private float averageServiceTime;
    /** Total waiting time for all kitchen orders. */
    @Column(name = "waiting_time")
    private float waitingTime;
    /** Average queue length in the kitchen. */
    @Column(name = "average_queue_length")
    private float averageQueueLength;
    /** Mean value for kitchen service time distribution. */
    @Column(name = "mean_value")
    private float meanValue;
    /** Variance value for kitchen service time distribution. */
    @Column(name = "variance_value")
    private float varianceValue;
    /** Reference to the associated overview statistics entity. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "overview_id", insertable = false, updatable = false)
    private OverviewStatistics overviewStatistics;

    /**
     * Default constructor required by JPA.
     */
    public KitchenStatistics() {}

    /**
     * Constructor for creating new kitchen statistics with all fields.
     * @param overviewId foreign key to the overview statistics record
     * @param arrivedCustomers number of customers that arrived at the kitchen
     * @param servicedCustomers number of customers serviced by the kitchen
     * @param serviceBusyTime total busy time for kitchen service
     * @param serviceUtilization utilization percentage of the kitchen service
     * @param serviceThroughput throughput of the kitchen service
     * @param averageServiceTime average service time per order
     * @param waitingTime total waiting time for all kitchen orders
     * @param averageQueueLength average queue length in the kitchen
     * @param meanValue mean value for kitchen service time distribution
     * @param varianceValue variance value for kitchen service time distribution
     */
    public KitchenStatistics(int overviewId, int arrivedCustomers, int servicedCustomers,
                            float serviceBusyTime, float serviceUtilization, float serviceThroughput,
                            float averageServiceTime, float waitingTime, float averageQueueLength,
                            float meanValue, float varianceValue) {
        this.overviewId = overviewId;
        this.arrivedCustomers = arrivedCustomers;
        this.servicedCustomers = servicedCustomers;
        this.serviceBusyTime = serviceBusyTime;
        this.serviceUtilization = serviceUtilization;
        this.serviceThroughput = serviceThroughput;
        this.averageServiceTime = averageServiceTime;
        this.waitingTime = waitingTime;
        this.averageQueueLength = averageQueueLength;
        this.meanValue = meanValue;
        this.varianceValue = varianceValue;
    }

    // Getters and setters
    /**
     * Gets the unique identifier for the kitchen statistics record.
     * @return the id of the kitchen statistics record
     */
    public int getId() { return id; }
    /**
     * Sets the unique identifier for the kitchen statistics record.
     * @param id the new id for the kitchen statistics record
     */
    public void setId(int id) { this.id = id; }

    /**
     * Gets the foreign key to the overview statistics record.
     * @return the overviewId
     */
    public int getOverviewId() { return overviewId; }
    /**
     * Sets the foreign key to the overview statistics record.
     * @param overviewId the new overviewId
     */
    public void setOverviewId(int overviewId) { this.overviewId = overviewId; }

    /**
     * Gets the number of customers that arrived at the kitchen.
     * @return the arrivedCustomers
     */
    public int getArrivedCustomers() { return arrivedCustomers; }
    /**
     * Sets the number of customers that arrived at the kitchen.
     * @param arrivedCustomers the new arrivedCustomers count
     */
    public void setArrivedCustomers(int arrivedCustomers) { this.arrivedCustomers = arrivedCustomers; }

    /**
     * Gets the number of customers serviced by the kitchen.
     * @return the servicedCustomers
     */
    public int getServicedCustomers() { return servicedCustomers; }
    /**
     * Sets the number of customers serviced by the kitchen.
     * @param servicedCustomers the new servicedCustomers count
     */
    public void setServicedCustomers(int servicedCustomers) { this.servicedCustomers = servicedCustomers; }

    /**
     * Gets the total busy time for kitchen service.
     * @return the serviceBusyTime
     */
    public float getServiceBusyTime() { return serviceBusyTime; }
    /**
     * Sets the total busy time for kitchen service.
     * @param serviceBusyTime the new serviceBusyTime
     */
    public void setServiceBusyTime(float serviceBusyTime) { this.serviceBusyTime = serviceBusyTime; }

    /**
     * Gets the utilization percentage of the kitchen service.
     * @return the serviceUtilization
     */
    public float getServiceUtilization() { return serviceUtilization; }
    /**
     * Sets the utilization percentage of the kitchen service.
     * @param serviceUtilization the new serviceUtilization
     */
    public void setServiceUtilization(float serviceUtilization) { this.serviceUtilization = serviceUtilization; }

    /**
     * Gets the throughput of the kitchen service (orders per unit time).
     * @return the serviceThroughput
     */
    public float getServiceThroughput() { return serviceThroughput; }
    /**
     * Sets the throughput of the kitchen service (orders per unit time).
     * @param serviceThroughput the new serviceThroughput
     */
    public void setServiceThroughput(float serviceThroughput) { this.serviceThroughput = serviceThroughput; }

    /**
     * Gets the average service time per order in the kitchen.
     * @return the averageServiceTime
     */
    public float getAverageServiceTime() { return averageServiceTime; }
    /**
     * Sets the average service time per order in the kitchen.
     * @param averageServiceTime the new averageServiceTime
     */
    public void setAverageServiceTime(float averageServiceTime) { this.averageServiceTime = averageServiceTime; }

    /**
     * Gets the total waiting time for all kitchen orders.
     * @return the waitingTime
     */
    public float getWaitingTime() { return waitingTime; }
    /**
     * Sets the total waiting time for all kitchen orders.
     * @param waitingTime the new waitingTime
     */
    public void setWaitingTime(float waitingTime) { this.waitingTime = waitingTime; }

    /**
     * Gets the average queue length in the kitchen.
     * @return the averageQueueLength
     */
    public float getAverageQueueLength() { return averageQueueLength; }
    /**
     * Sets the average queue length in the kitchen.
     * @param averageQueueLength the new averageQueueLength
     */
    public void setAverageQueueLength(float averageQueueLength) { this.averageQueueLength = averageQueueLength; }

    /**
     * Gets the mean value for kitchen service time distribution.
     * @return the meanValue
     */
    public float getMeanValue() { return meanValue; }
    /**
     * Sets the mean value for kitchen service time distribution.
     * @param meanValue the new meanValue
     */
    public void setMeanValue(float meanValue) { this.meanValue = meanValue; }

    /**
     * Gets the variance value for kitchen service time distribution.
     * @return the varianceValue
     */
    public float getVarianceValue() { return varianceValue; }
    /**
     * Sets the variance value for kitchen service time distribution.
     * @param varianceValue the new varianceValue
     */
    public void setVarianceValue(float varianceValue) { this.varianceValue = varianceValue; }

    /**
     * Gets the reference to the associated overview statistics entity.
     * @return the overviewStatistics
     */
    public OverviewStatistics getOverviewStatistics() { return overviewStatistics; }
    /**
     * Sets the reference to the associated overview statistics entity.
     * @param overviewStatistics the new overviewStatistics
     */
    public void setOverviewStatistics(OverviewStatistics overviewStatistics) { 
        this.overviewStatistics = overviewStatistics; 
    }

    /**
     * Compatibility method for old code that uses total orders terminology.
     * @return number of serviced customers
     */
    public int getTotalOrders() { return servicedCustomers; }
    public void setTotalOrders(int totalOrders) { this.servicedCustomers = totalOrders; }

    /**
     * Compatibility method for old code that uses total preparation time terminology.
     * @return total busy time
     */
    public float getTotalPrepTime() { return serviceBusyTime; }
    public void setTotalPrepTime(float totalPrepTime) { this.serviceBusyTime = totalPrepTime; }

    /**
     * Compatibility method for old code that uses average preparation time terminology.
     * @return average service time
     */
    public float getAvgPrepTime() { return averageServiceTime; }
    public void setAvgPrepTime(float avgPrepTime) { this.averageServiceTime = avgPrepTime; }

    @Override
    public String toString() {
        return "KitchenStatistics{" +
                "id=" + id +
                ", overviewId=" + overviewId +
                ", arrivedCustomers=" + arrivedCustomers +
                ", servicedCustomers=" + servicedCustomers +
                ", serviceBusyTime=" + serviceBusyTime +
                '}';
    }
}