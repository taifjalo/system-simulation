package simu.backend.entity;

import jakarta.persistence.*;

/**
 * Entity representing the delivery_statistics table.
 * Contains all ServicePointStatistics fields from the current schema for delivery performance reporting and persistence.
 */
@Entity
@Table(name = "delivery_statistics")
public class DeliveryStatistics {
    /** Unique identifier for the delivery statistics record. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    /** Foreign key to the overview statistics record. */
    @Column(name = "overview_id")
    private int overviewId;
    /** Number of customers that arrived at the delivery service. */
    @Column(name = "arrived_customers")
    private int arrivedCustomers;
    /** Number of customers serviced by the delivery service. */
    @Column(name = "serviced_customers")
    private int servicedCustomers;
    /** Total busy time for delivery service. */
    @Column(name = "service_busy_time")
    private float serviceBusyTime;
    /** Utilization percentage of the delivery service. */
    @Column(name = "service_utilization")
    private float serviceUtilization;
    /** Throughput of the delivery service (orders per unit time). */
    @Column(name = "service_throughput")
    private float serviceThroughput;
    /** Average service time per delivery. */
    @Column(name = "average_service_time")
    private float averageServiceTime;
    /** Total waiting time for all deliveries. */
    @Column(name = "waiting_time")
    private float waitingTime;
    /** Average queue length in the delivery service. */
    @Column(name = "average_queue_length")
    private float averageQueueLength;
    /** Mean value for delivery service time distribution. */
    @Column(name = "mean_value")
    private float meanValue;
    /** Variance value for delivery service time distribution. */
    @Column(name = "variance_value")
    private float varianceValue;
    /** Reference to the associated overview statistics entity. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "overview_id", insertable = false, updatable = false)
    private OverviewStatistics overviewStatistics;

    /**
     * Default constructor required by JPA.
     */
    public DeliveryStatistics() {}

    /**
     * Constructor for creating new delivery statistics with all fields.
     * @param overviewId foreign key to the overview statistics record
     * @param arrivedCustomers number of customers that arrived at the delivery service
     * @param servicedCustomers number of customers serviced by the delivery service
     * @param serviceBusyTime total busy time for delivery service
     * @param serviceUtilization utilization percentage of the delivery service
     * @param serviceThroughput throughput of the delivery service
     * @param averageServiceTime average service time per delivery
     * @param waitingTime total waiting time for all deliveries
     * @param averageQueueLength average queue length in the delivery service
     * @param meanValue mean value for delivery service time distribution
     * @param varianceValue variance value for delivery service time distribution
     */
    public DeliveryStatistics(int overviewId, int arrivedCustomers, int servicedCustomers,
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
    
    /**
     * Compatibility method for old code that uses total deliveries terminology.
     * @return number of serviced customers
     */
    public int getTotalDeliveries() { return servicedCustomers; }
    public void setTotalDeliveries(int totalDeliveries) { this.servicedCustomers = totalDeliveries; }

    /**
     * Compatibility method for old code that uses total delivery duration terminology.
     * @return total busy time
     */
    public float getTotalDeliveryDuration() { return serviceBusyTime; }
    public void setTotalDeliveryDuration(float totalDeliveryDuration) { 
        this.serviceBusyTime = totalDeliveryDuration; 
    }

    /**
     * Compatibility method for old code that uses average delivery duration terminology.
     * @return average service time
     */
    public float getAvgDeliveryDuration() { return averageServiceTime; }
    public void setAvgDeliveryDuration(float avgDeliveryDuration) { 
        this.averageServiceTime = avgDeliveryDuration; 
    }

    @Override
    public String toString() {
        return "DeliveryStatistics{" +
                "id=" + id +
                ", overviewId=" + overviewId +
                ", arrivedCustomers=" + arrivedCustomers +
                ", servicedCustomers=" + servicedCustomers +
                ", serviceBusyTime=" + serviceBusyTime +
                '}';
    }
}