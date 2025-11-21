
/**
 * ServicePointStatistics is a base class for all service point statistics in the simulation.
 * It provides common statistical fields and methods for tracking and calculating metrics such as
 * arrived customers, serviced customers, busy time, utilization, throughput, average service time,
 * waiting time, queue length, mean, and variance. Subclasses can extend this class to implement
 * specific service point statistics and database persistence.
 *
 * @author (your name)
 */
package simu.framework.statistics;

import simu.framework.Trace;


/**
 * Superclass for all service point statistics with methods for tracking and calculating
 * simulation metrics. Intended to be extended by specific service point statistics classes.
 */
public class ServicePointStatistics {


    /** Number of customers that have arrived at the service point. */
    private int arrivedCustomers;

    /** Number of customers that have been serviced at the service point. */
    private int servicedCustomers;

    /** Total time the service point has been busy. */
    private double serviceBusyTime;

    /** Service utilization ratio (busy time / simulation time). */
    private double serviceUtilization;

    /** Service throughput (serviced customers / simulation time). */
    private double serviceThroughput;

    /** Average service time per customer. */
    private double averageServiceTime;

    /** Total waiting time for all customers. */
    private double waitingTime;

    /** Average queue length (waiting time / simulation time). */
    public double averageQueueLength;

    /** Mean value for service time distribution (user configuration). */
    public double mean;

    /** Variance for service time distribution (user configuration). */
    public double variance;


    /**
     * Constructs a new ServicePointStatistics object with default values.
     * Default mean and variance are set to prevent ParameterException in Normal distribution.
     * These values should be overridden by user configuration before simulation starts.
     */
    public ServicePointStatistics() {
        arrivedCustomers = 0;
        servicedCustomers = 0;
        serviceBusyTime = 0.0;
        serviceUtilization = 0.0;
        serviceThroughput = 0.0;
        averageServiceTime = 0.0;
        waitingTime = 0.0;
        // Initialize with default values to prevent ParameterException in Normal distribution
        // These will be overridden by user configuration before simulation starts
        mean = 5.0;      // Default mean service time
        variance = 1.0;  // Default variance (must be > 0)
    }


    /**
     * Resets only runtime statistics data, preserving configuration values (mean, variance).
     * This method is used to prepare the statistics for a new simulation run.
     */
    public void resetRuntimeData() {
        arrivedCustomers = 0;
        servicedCustomers = 0;
        serviceBusyTime = 0.0;
        serviceUtilization = 0.0;
        serviceThroughput = 0.0;
        averageServiceTime = 0.0;
        waitingTime = 0.0;
        // Note: mean and variance are NOT reset here - they're user configuration
    }


    /**
     * Increments the number of arrived customers by one.
     */
    public void incrementArrivedCustomers() {
        arrivedCustomers++;
    }


    /**
     * Increments the number of serviced customers by one.
     */
    public void incrementServicedCustomers() {
        servicedCustomers++;
    }


    /**
     * Adds the given time to the total service busy time.
     * @param time the amount of time to add
     */
    public void addServiceBusyTime(double time) {
        serviceBusyTime += time;
    }


    /**
     * Calculates and returns the service utilization (busy time / simulation time).
     * @param simulationTime the total simulation time
     * @return the service utilization ratio
     */
    public double countServiceUtilization(double simulationTime) {
        serviceUtilization = simulationTime > 0 ? serviceBusyTime / simulationTime : 0.0;
        return serviceUtilization;
    }


    /**
     * Calculates and returns the service throughput (serviced customers / simulation time).
     * @param simulationTime the total simulation time
     * @return the service throughput
     */
    public double countServiceThroughput(double simulationTime) {
        serviceThroughput = simulationTime > 0 ? servicedCustomers / simulationTime : 0.0;
        return serviceThroughput;
    }


    /**
     * Calculates and returns the average service time per customer.
     * @return the average service time
     */
    public double countAverageServiceTime() {
        averageServiceTime = servicedCustomers > 0 ? serviceBusyTime / servicedCustomers : 0.0;
        return averageServiceTime;
    }


    /**
     * Calculates and returns the average queue length (waiting time / simulation time).
     * @param simulationTime the total simulation time
     * @return the average queue length
     */
    public double countAverageQueueLength(double simulationTime){
        averageQueueLength = simulationTime > 0 ? waitingTime / simulationTime : 0.0;
        return averageQueueLength;
    }


    /**
     * Returns the number of customers that have arrived at the service point.
     * @return the number of arrived customers
     */
    public int getArrivedCustomers() {
        return arrivedCustomers;
    }


    /**
     * Returns the number of customers that have been serviced at the service point.
     * @return the number of serviced customers
     */
    public int getServicedCustomers() {
        return servicedCustomers;
    }


    /**
     * Returns the total time the service point has been busy.
     * @return the service busy time
     */
    public double getServiceBusyTime() {
        return serviceBusyTime;
    }


    /**
     * Adds the given time to the total waiting time for all customers.
     * @param time the amount of waiting time to add
     */
    public void addServiceWaitingTime(double time){
        waitingTime += time;
    }


    /**
     * Returns the total waiting time for all customers.
     * @return the total waiting time
     */
    public double getServiceWaitingTime(){
        return waitingTime;
    }


    /**
     * Returns the mean value for the service time distribution.
     * @return the mean value
     */
    public double getMean(){
        return mean;
    }


    /**
     * Returns the variance for the service time distribution.
     * @return the variance value
     */
    public double getVariance(){
        return variance;
    }


    /**
     * Sets the mean value for the service time distribution.
     * @param mean the mean value to set
     */
    public void setMean(double mean){
        this.mean = mean;
    }


    /**
     * Sets the variance for the service time distribution.
     * @param variance the variance value to set
     */
    public void setVariance(double variance){
        this.variance = variance;
    }

    /**
     * Displays the current service point statistics using the Trace utility.
     * @param time the current simulation time
     */
    public void showServicePointStatistics(double time){
        Trace.out(Trace.Level.INFO, "Arrived customers: " + getArrivedCustomers());
        Trace.out(Trace.Level.INFO, "Serviced customers: " + getServicedCustomers());
        Trace.out(Trace.Level.INFO, "Service busy time " + getServiceBusyTime());
        Trace.out(Trace.Level.INFO, "Service utilization: " + countServiceUtilization(time));
        Trace.out(Trace.Level.INFO, "Service throughput: " + countServiceThroughput(time) );
        Trace.out(Trace.Level.INFO, "Average service time: " + countAverageServiceTime());
        Trace.out(Trace.Level.INFO, "Average queue length: " + countAverageQueueLength(time));
        Trace.out(Trace.Level.INFO, "Waiting time: " + getServiceWaitingTime());
    }
}




