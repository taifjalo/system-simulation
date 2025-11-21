package simu.backend.entity;

import jakarta.persistence.*;

/**
 * Entity representing the counter_statistics table.
 * Contains all ServicePointStatistics fields from the current schema for counter performance reporting and persistence.
 */
@Entity
@Table(name = "counter_statistics")
public class CounterStatistics {
    /** Unique identifier for the counter statistics record. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    /** Foreign key to the overview statistics record. */
    @Column(name = "overview_id")
    private int overviewId;
    /** Number of customers that arrived at the counter. */
    @Column(name = "arrived_customers")
    private int arrivedCustomers;
    /** Number of customers serviced by the counter. */
    @Column(name = "serviced_customers")
    private int servicedCustomers;
    /** Total busy time for counter service. */
    @Column(name = "service_busy_time")
    private float serviceBusyTime;
    /** Utilization percentage of the counter service. */
    @Column(name = "service_utilization")
    private float serviceUtilization;
    /** Throughput of the counter service (orders per unit time). */
    @Column(name = "service_throughput")
    private float serviceThroughput;
    /** Average service time per customer at the counter. */
    @Column(name = "average_service_time")
    private float averageServiceTime;
    /** Total waiting time for all counter customers. */
    @Column(name = "waiting_time")
    private float waitingTime;
    /** Average queue length at the counter. */
    @Column(name = "average_queue_length")
    private float averageQueueLength;
    /** Mean value for counter service time distribution. */
    @Column(name = "mean_value")
    private float meanValue;
    /** Variance value for counter service time distribution. */
    @Column(name = "variance_value")
    private float varianceValue;
    /** Reference to the associated overview statistics entity. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "overview_id", insertable = false, updatable = false)
    private OverviewStatistics overviewStatistics;

    /**
     * Default constructor required by JPA.
     */
    public CounterStatistics() {}

    /**
     * Constructor for creating new counter statistics with all fields.
     * @param overviewId foreign key to the overview statistics record
     * @param arrivedCustomers number of customers that arrived at the counter
     * @param servicedCustomers number of customers serviced by the counter
     * @param serviceBusyTime total busy time for counter service
     * @param serviceUtilization utilization percentage of the counter service
     * @param serviceThroughput throughput of the counter service
     * @param averageServiceTime average service time per customer
     * @param waitingTime total waiting time for all counter customers
     * @param averageQueueLength average queue length at the counter
     * @param meanValue mean value for counter service time distribution
     * @param varianceValue variance value for counter service time distribution
     */
    public CounterStatistics(int overviewId, int arrivedCustomers, int servicedCustomers,
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
     * @return the id
     */
    public int getId() { return id; }
    /**
     * @param id the id to set
     */
    public void setId(int id) { this.id = id; }
    /**
     * @return the overviewId
     */
    public int getOverviewId() { return overviewId; }
    /**
     * @param overviewId the overviewId to set
     */
    public void setOverviewId(int overviewId) { this.overviewId = overviewId; }
    /**
     * @return the arrivedCustomers
     */
    public int getArrivedCustomers() { return arrivedCustomers; }
    /**
     * @param arrivedCustomers the arrivedCustomers to set
     */
    public void setArrivedCustomers(int arrivedCustomers) { this.arrivedCustomers = arrivedCustomers; }
    /**
     * @return the servicedCustomers
     */
    public int getServicedCustomers() { return servicedCustomers; }
    /**
     * @param servicedCustomers the servicedCustomers to set
     */
    public void setServicedCustomers(int servicedCustomers) { this.servicedCustomers = servicedCustomers; }
    /**
     * @return the serviceBusyTime
     */
    public float getServiceBusyTime() { return serviceBusyTime; }
    /**
     * @param serviceBusyTime the serviceBusyTime to set
     */
    public void setServiceBusyTime(float serviceBusyTime) { this.serviceBusyTime = serviceBusyTime; }
    /**
     * @return the serviceUtilization
     */
    public float getServiceUtilization() { return serviceUtilization; }
    /**
     * @param serviceUtilization the serviceUtilization to set
     */
    public void setServiceUtilization(float serviceUtilization) { this.serviceUtilization = serviceUtilization; }
    /**
     * @return the serviceThroughput
     */
    public float getServiceThroughput() { return serviceThroughput; }
    /**
     * @param serviceThroughput the serviceThroughput to set
     */
    public void setServiceThroughput(float serviceThroughput) { this.serviceThroughput = serviceThroughput; }
    /**
     * @return the averageServiceTime
     */
    public float getAverageServiceTime() { return averageServiceTime; }
    /**
     * @param averageServiceTime the averageServiceTime to set
     */
    public void setAverageServiceTime(float averageServiceTime) { this.averageServiceTime = averageServiceTime; }
    /**
     * @return the waitingTime
     */
    public float getWaitingTime() { return waitingTime; }
    /**
     * @param waitingTime the waitingTime to set
     */
    public void setWaitingTime(float waitingTime) { this.waitingTime = waitingTime; }
    /**
     * @return the averageQueueLength
     */
    public float getAverageQueueLength() { return averageQueueLength; }
    /**
     * @param averageQueueLength the averageQueueLength to set
     */
    public void setAverageQueueLength(float averageQueueLength) { this.averageQueueLength = averageQueueLength; }
    /**
     * @return the meanValue
     */
    public float getMeanValue() { return meanValue; }
    /**
     * @param meanValue the meanValue to set
     */
    public void setMeanValue(float meanValue) { this.meanValue = meanValue; }
    /**
     * @return the varianceValue
     */
    public float getVarianceValue() { return varianceValue; }
    /**
     * @param varianceValue the varianceValue to set
     */
    public void setVarianceValue(float varianceValue) { this.varianceValue = varianceValue; }
    /**
     * @return the overviewStatistics
     */
    public OverviewStatistics getOverviewStatistics() { return overviewStatistics; }
    /**
     * @param overviewStatistics the overviewStatistics to set
     */
    public void setOverviewStatistics(OverviewStatistics overviewStatistics) { 
        this.overviewStatistics = overviewStatistics; 
    }

    /**
     * Compatibility method for old code that uses total customers served terminology.
     * @return number of serviced customers
     */
    public int getTotalCustomersServed() { return servicedCustomers; }
    public void setTotalCustomersServed(int totalCustomersServed) { 
        this.servicedCustomers = totalCustomersServed; 
    }

    /**
     * Compatibility method for old code that uses total service time terminology.
     * @return total busy time
     */
    public float getTotalServiceTime() { return serviceBusyTime; }
    public void setTotalServiceTime(float totalServiceTime) { 
        this.serviceBusyTime = totalServiceTime; 
    }

    /**
     * Compatibility method for old code that uses average service time terminology.
     * @return average service time
     */
    public float getAvgServiceTime() { return averageServiceTime; }
    public void setAvgServiceTime(float avgServiceTime) { this.averageServiceTime = avgServiceTime; }

    @Override
    public String toString() {
        return "CounterStatistics{" +
                "id=" + id +
                ", overviewId=" + overviewId +
                ", arrivedCustomers=" + arrivedCustomers +
                ", servicedCustomers=" + servicedCustomers +
                ", serviceBusyTime=" + serviceBusyTime +
                '}';
    }
}