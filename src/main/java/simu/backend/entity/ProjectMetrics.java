package simu.backend.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entity representing simulation run metrics for a project.
 * Stores statistics such as arrivals, completions, service times, success rates, and more.
 * Used for database persistence and reporting of simulation results.
 */
@Entity
@Table(name="project_metrics")
public class ProjectMetrics {
    /** Unique identifier for the metrics record. */
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;
    /** Unique run identifier for this simulation run. */
    @Column(name="run_id", nullable=false, unique=true, length=36)
    private String runId;
    /** Timestamp when the simulation run was recorded. */
    @Column(name="run_timestamp")
    private LocalDateTime runTimestamp;
    /** Duration of the simulation run. */
    @Column(name="simulation_duration")
    private double simulationDuration;
    /** Number of customers that arrived. */
    @Column(name="arrival_count")
    private int arrivalCount;
    /** Number of customers that completed the process. */
    @Column(name="completed_count")
    private int completedCount;
    /** Number of walk-in customers. */
    @Column(name="walk_in_customers")
    private int walkInCustomers;
    /** Number of call-in customers. */
    @Column(name="call_in_customers")
    private int callInCustomers;
    /** Number of payment failures. */
    @Column(name="payment_failures")
    private int paymentFailures;
    /** Number of successful deliveries. */
    @Column(name="successful_deliveries")
    private int successfulDeliveries;
    /** Number of failed deliveries. */
    @Column(name="failed_deliveries")
    private int failedDeliveries;
    /** Total busy time of the system. */
    @Column(name="busy_time")
    private double busyTime;
    /** Total time of the simulation. */
    @Column(name="total_time")
    private double totalTime;
    /** Cumulative response time for all customers. */
    @Column(name="cumulative_response_time")
    private double cumulativeResponseTime;
    /** Utilization percentage of the system. */
    @Column(name="utilization")
    private double utilization;
    /** Throughput of the system (customers per unit time). */
    @Column(name="throughput")
    private double throughput;
    /** Customers served per hour. */
    @Column(name="customers_per_hour")
    private double customersPerHour;
    /** Average service time per customer. */
    @Column(name="avg_service_time")
    private double avgServiceTime;
    /** Average response time per customer. */
    @Column(name="avg_response_time")
    private double avgResponseTime;
    /** Average queue length during the simulation. */
    @Column(name="avg_queue_length")
    private double avgQueueLength;
    /** Payment success rate (percentage). */
    @Column(name="payment_success_rate")
    private double paymentSuccessRate;
    /** Delivery success rate (percentage). */
    @Column(name="delivery_success_rate")
    private double deliverySuccessRate;

    /**
     * Default constructor required by JPA. Initializes runId and timestamp.
     */
    public ProjectMetrics() {
        this.runId = java.util.UUID.randomUUID().toString();
        this.runTimestamp = LocalDateTime.now();
    }

    /**
     * Constructor for creating new metrics with run identification and basic statistics.
     * @param arrivalCount number of arrivals
     * @param completedCount number of completions
     * @param busyTime total busy time
     * @param totalTime total simulation time
     * @param cumulativeResponseTime cumulative response time
     */
    public ProjectMetrics(int arrivalCount, int completedCount, double busyTime, 
                         double totalTime, double cumulativeResponseTime) {
        this(); // Call default constructor to set runId and timestamp
        this.arrivalCount = arrivalCount;
        this.completedCount = completedCount;
        this.busyTime = busyTime;
        this.totalTime = totalTime;
        this.cumulativeResponseTime = cumulativeResponseTime;
        
        // Calculate derived metrics
        calculateDerivedMetrics();
    }

    /**
     * Constructor with full parameters for detailed metrics.
     * @param arrivalCount number of arrivals
     * @param completedCount number of completions
     * @param walkInCustomers number of walk-in customers
     * @param callInCustomers number of call-in customers
     * @param paymentFailures number of payment failures
     * @param successfulDeliveries number of successful deliveries
     * @param failedDeliveries number of failed deliveries
     * @param busyTime total busy time
     * @param totalTime total simulation time
     * @param cumulativeResponseTime cumulative response time
     * @param simulationDuration duration of the simulation
     */
    public ProjectMetrics(int arrivalCount, int completedCount, int walkInCustomers, 
                         int callInCustomers, int paymentFailures, int successfulDeliveries,
                         int failedDeliveries, double busyTime, double totalTime, 
                         double cumulativeResponseTime, double simulationDuration) {
        this(); // Call default constructor to set runId and timestamp
        this.arrivalCount = arrivalCount;
        this.completedCount = completedCount;
        this.walkInCustomers = walkInCustomers;
        this.callInCustomers = callInCustomers;
        this.paymentFailures = paymentFailures;
        this.successfulDeliveries = successfulDeliveries;
        this.failedDeliveries = failedDeliveries;
        this.busyTime = busyTime;
        this.totalTime = totalTime;
        this.cumulativeResponseTime = cumulativeResponseTime;
        this.simulationDuration = simulationDuration;
        
        // Calculate derived metrics
        calculateDerivedMetrics();
    }

    /**
     * Calculates derived metrics such as utilization, throughput, averages, and success rates.
     */
    private void calculateDerivedMetrics() {
        this.utilization = (totalTime > 0) ? (busyTime / totalTime) * 100.0 : 0.0;
        this.throughput = (totalTime > 0) ? completedCount / totalTime : 0.0;
        this.customersPerHour = (simulationDuration > 0) ? (completedCount / simulationDuration) * 3600.0 : 0.0;
        this.avgServiceTime = (completedCount > 0) ? busyTime / completedCount : 0.0;
        this.avgResponseTime = (completedCount > 0) ? cumulativeResponseTime / completedCount : 0.0;
        this.avgQueueLength = (totalTime > 0) ? cumulativeResponseTime / totalTime : 0.0;
        
        // Calculate success rates
        int totalPaymentAttempts = completedCount + paymentFailures;
        this.paymentSuccessRate = (totalPaymentAttempts > 0) ? 
            ((double)(totalPaymentAttempts - paymentFailures) / totalPaymentAttempts) * 100.0 : 0.0;
            
        int totalDeliveryAttempts = successfulDeliveries + failedDeliveries;
        this.deliverySuccessRate = (totalDeliveryAttempts > 0) ? 
            ((double)successfulDeliveries / totalDeliveryAttempts) * 100.0 : 0.0;
    }

    /**
     * Public method to recalculate derived metrics after setting fields.
     */
    public void recalculateMetrics() {
        calculateDerivedMetrics();
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    public String getRunId() {
        return runId;
    }

    public void setRunId(String runId) {
        this.runId = runId;
    }

    public LocalDateTime getRunTimestamp() {
        return runTimestamp;
    }

    public void setRunTimestamp(LocalDateTime runTimestamp) {
        this.runTimestamp = runTimestamp;
    }

    public double getSimulationDuration() {
        return simulationDuration;
    }

    public void setSimulationDuration(double simulationDuration) {
        this.simulationDuration = simulationDuration;
    }

    public int getWalkInCustomers() {
        return walkInCustomers;
    }

    public void setWalkInCustomers(int walkInCustomers) {
        this.walkInCustomers = walkInCustomers;
    }

    public int getCallInCustomers() {
        return callInCustomers;
    }

    public void setCallInCustomers(int callInCustomers) {
        this.callInCustomers = callInCustomers;
    }

    public int getPaymentFailures() {
        return paymentFailures;
    }

    public void setPaymentFailures(int paymentFailures) {
        this.paymentFailures = paymentFailures;
    }

    public int getSuccessfulDeliveries() {
        return successfulDeliveries;
    }

    public void setSuccessfulDeliveries(int successfulDeliveries) {
        this.successfulDeliveries = successfulDeliveries;
    }

    public int getFailedDeliveries() {
        return failedDeliveries;
    }

    public void setFailedDeliveries(int failedDeliveries) {
        this.failedDeliveries = failedDeliveries;
    }

    public double getCustomersPerHour() {
        return customersPerHour;
    }

    public void setCustomersPerHour(double customersPerHour) {
        this.customersPerHour = customersPerHour;
    }

    public double getPaymentSuccessRate() {
        return paymentSuccessRate;
    }

    public void setPaymentSuccessRate(double paymentSuccessRate) {
        this.paymentSuccessRate = paymentSuccessRate;
    }

    public double getDeliverySuccessRate() {
        return deliverySuccessRate;
    }

    public void setDeliverySuccessRate(double deliverySuccessRate) {
        this.deliverySuccessRate = deliverySuccessRate;
    }

    public int getArrivalCount() {
        return arrivalCount;
    }

    public void setArrivalCount(int arrivalCount) {
        this.arrivalCount = arrivalCount;
    }

    public int getCompletedCount() {
        return completedCount;
    }

    public void setCompletedCount(int completedCount) {
        this.completedCount = completedCount;
    }

    public double getBusyTime() {
        return busyTime;
    }

    public void setBusyTime(double busyTime) {
        this.busyTime = busyTime;
    }

    public double getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(double totalTime) {
        this.totalTime = totalTime;
    }

    public double getCumulativeResponseTime() {
        return cumulativeResponseTime;
    }

    public void setCumulativeResponseTime(double cumulativeResponseTime) {
        this.cumulativeResponseTime = cumulativeResponseTime;
    }

    public double getUtilization() {
        return utilization;
    }

    public void setUtilization(double utilization) {
        this.utilization = utilization;
    }

    public double getThroughput() {
        return throughput;
    }

    public void setThroughput(double throughput) {
        this.throughput = throughput;
    }

    public double getAvgServiceTime() {
        return avgServiceTime;
    }

    public void setAvgServiceTime(double avgServiceTime) {
        this.avgServiceTime = avgServiceTime;
    }

    public double getAvgResponseTime() {
        return avgResponseTime;
    }

    public void setAvgResponseTime(double avgResponseTime) {
        this.avgResponseTime = avgResponseTime;
    }

    public double getAvgQueueLength() {
        return avgQueueLength;
    }

    public void setAvgQueueLength(double avgQueueLength) {
        this.avgQueueLength = avgQueueLength;
    }
    
    @Override
    public String toString() {
        return String.format("ProjectMetrics[id=%d, runId=%s, arrivals=%d, completed=%d, duration=%.2f, " +
                "walkIns=%d, callIns=%d, throughput=%.2f customers/hour, utilization=%.2f%%, " +
                "paymentSuccess=%.2f%%, deliverySuccess=%.2f%%]",
                id, runId, arrivalCount, completedCount, simulationDuration, 
                walkInCustomers, callInCustomers, customersPerHour, utilization, 
                paymentSuccessRate, deliverySuccessRate);
    }
}