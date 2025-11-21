package simu.backend.entity;

import jakarta.persistence.*;

/**
 * Entity representing the <b>reception_statistics</b> table.
 * <p>
 * Contains all ServicePointStatistics fields from the current schema.
 * Provides statistics for reception operations in the simulation.
 */
@Entity
@Table(name = "reception_statistics")
public class ReceptionStatistics {
    
    /** Unique identifier for the reception statistics record. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    /** Foreign key to the overview statistics record. */
    @Column(name = "overview_id")
    private int overviewId;
    
    // ServicePointStatistics fields matching current schema
    /** Number of customers that arrived at the reception. */
    @Column(name = "arrived_customers")
    private int arrivedCustomers;
    
    /** Number of customers serviced at the reception. */
    @Column(name = "serviced_customers")
    private int servicedCustomers;
    
    /** Total time the reception was busy servicing customers. */
    @Column(name = "service_busy_time")
    private float serviceBusyTime;
    
    /** Utilization rate of the reception (0-1). */
    @Column(name = "service_utilization")
    private float serviceUtilization;
    
    /** Throughput of the reception (customers per unit time). */
    @Column(name = "service_throughput")
    private float serviceThroughput;
    
    /** Average service time per customer at the reception. */
    @Column(name = "average_service_time")
    private float averageServiceTime;
    
    /** Total waiting time for all customers at the reception. */
    @Column(name = "waiting_time")
    private float waitingTime;
    
    /** Average queue length at the reception. */
    @Column(name = "average_queue_length")
    private float averageQueueLength;
    
    /** Mean value of a measured statistic (for reporting). */
    @Column(name = "mean_value")
    private float meanValue;
    
    /** Variance value of a measured statistic (for reporting). */
    @Column(name = "variance_value")
    private float varianceValue;
    
    // JPA relationship
    /**
     * Many-to-one relationship to the overview statistics entity.
     * Used for JPA mapping.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "overview_id", insertable = false, updatable = false)
    private OverviewStatistics overviewStatistics;
    
    /**
     * Default constructor required by JPA.
     */
    public ReceptionStatistics() {}
    
    /**
     * Constructs a new ReceptionStatistics entity with all fields.
     *
     * @param overviewId Foreign key to overview statistics
     * @param arrivedCustomers Number of customers arrived
     * @param servicedCustomers Number of customers serviced
     * @param serviceBusyTime Total busy time
     * @param serviceUtilization Utilization rate
     * @param serviceThroughput Throughput
     * @param averageServiceTime Average service time
     * @param waitingTime Total waiting time
     * @param averageQueueLength Average queue length
     * @param meanValue Mean value
     * @param varianceValue Variance value
     */
    public ReceptionStatistics(int overviewId, int arrivedCustomers, int servicedCustomers,
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
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public int getOverviewId() { return overviewId; }
    public void setOverviewId(int overviewId) { this.overviewId = overviewId; }
    
    public int getArrivedCustomers() { return arrivedCustomers; }
    public void setArrivedCustomers(int arrivedCustomers) { this.arrivedCustomers = arrivedCustomers; }
    
    public int getServicedCustomers() { return servicedCustomers; }
    public void setServicedCustomers(int servicedCustomers) { this.servicedCustomers = servicedCustomers; }
    
    public float getServiceBusyTime() { return serviceBusyTime; }
    public void setServiceBusyTime(float serviceBusyTime) { this.serviceBusyTime = serviceBusyTime; }
    
    public float getServiceUtilization() { return serviceUtilization; }
    public void setServiceUtilization(float serviceUtilization) { this.serviceUtilization = serviceUtilization; }
    
    public float getServiceThroughput() { return serviceThroughput; }
    public void setServiceThroughput(float serviceThroughput) { this.serviceThroughput = serviceThroughput; }
    
    public float getAverageServiceTime() { return averageServiceTime; }
    public void setAverageServiceTime(float averageServiceTime) { this.averageServiceTime = averageServiceTime; }
    
    public float getWaitingTime() { return waitingTime; }
    public void setWaitingTime(float waitingTime) { this.waitingTime = waitingTime; }
    
    public float getAverageQueueLength() { return averageQueueLength; }
    public void setAverageQueueLength(float averageQueueLength) { this.averageQueueLength = averageQueueLength; }
    
    public float getMeanValue() { return meanValue; }
    public void setMeanValue(float meanValue) { this.meanValue = meanValue; }
    
    public float getVarianceValue() { return varianceValue; }
    public void setVarianceValue(float varianceValue) { this.varianceValue = varianceValue; }
    
    public OverviewStatistics getOverviewStatistics() { return overviewStatistics; }
    public void setOverviewStatistics(OverviewStatistics overviewStatistics) { 
        this.overviewStatistics = overviewStatistics; 
    }
    
    // Compatibility methods for old code that uses different method names
    public int getTotalArrivals() { return arrivedCustomers; }
    public void setTotalArrivals(int totalArrivals) { this.arrivedCustomers = totalArrivals; }
    
    public float getTotalWaitTime() { return waitingTime; }
    public void setTotalWaitTime(float totalWaitTime) { this.waitingTime = totalWaitTime; }
    
    public float getAvgWaitTime() {
        // Calculate average wait time
        return arrivedCustomers > 0 ? waitingTime / arrivedCustomers : 0;
    }
    public void setAvgWaitTime(float avgWaitTime) {
        // If setting average, calculate total
        this.waitingTime = avgWaitTime * arrivedCustomers;
    }
    
    @Override
    public String toString() {
        return "ReceptionStatistics{" +
                "id=" + id +
                ", overviewId=" + overviewId +
                ", arrivedCustomers=" + arrivedCustomers +
                ", servicedCustomers=" + servicedCustomers +
                ", serviceBusyTime=" + serviceBusyTime +
                '}';
    }
}