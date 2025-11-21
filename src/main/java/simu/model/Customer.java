package simu.model;

import simu.framework.Clock;
import simu.framework.Trace;

/**
 * Represents a customer in the simulation model.
 * Tracks arrival, service, and removal times, customer type, and various states for simulation logic and statistics.
 */
public class Customer {
    /** The time the customer arrived in the system. */
    private double arrivalTime;
    /** The time the customer was removed from the system. */
    private double removalTime;
    /** The time the customer started being served. */
    private double serviceStartTime;
    /** The time the customer arrived at a service point. */
    private double servicePointArrival;
    /** Unique identifier for the customer. */
    private int id;
    /** Static counter for assigning unique IDs. */
    private static int i = 1;
    /** Sum of all response times for all customers. */
    private static long sum = 0;
    /** Total response time for all customers. */
    private static double totalResponseTime = 0.0;
    /** Whether the customer is a walk-in. */
    private boolean walkIn;
    /** Whether the customer's order is faulty. */
    private boolean isFaulty;
    /** Whether the customer is currently in the kitchen. */
    private boolean onKitchen;
    /** The type of the customer (walk-in, call-in, remake). */
    private CustomerType customerType;
    /** Whether the customer is returning (e.g., due to payment issue). */
    private boolean customerBack;

    /**
     * Enum representing the type of customer for event tracking.
     */
    public enum CustomerType {
        /** Walk-in customer. */
        WALK_IN,
        /** Call-in customer. */
        CALL_IN,
        /** Remake customer (order remake). */
        REMAKE
    }

    /**
     * Constructs a new Customer, assigning a unique ID and setting type based on walk-in status.
     * @param walkIn true if the customer is a walk-in, false if call-in
     */
    public Customer(boolean walkIn) {
        id = i++;
        this.walkIn = walkIn;
        this.customerType = walkIn ? CustomerType.WALK_IN : CustomerType.CALL_IN;
        isFaulty = false;
        customerBack = false;
        onKitchen = false;
        arrivalTime = Clock.getInstance().getTime();
        
        Trace.out(Trace.Level.INFO, "New customer #" + id + " arrived at  " + arrivalTime);
    }
    
    /**
     * Constructs a new Customer with a specific type (used for remakes).
     * @param type the type of the customer
     */
    public Customer(CustomerType type) {
        id = i++;
        this.customerType = type;
        this.walkIn = (type == CustomerType.WALK_IN);
        isFaulty = false;
        arrivalTime = Clock.getInstance().getTime();
        Trace.out(Trace.Level.INFO, "New " + type + " customer #" + id + " arrived at  " + arrivalTime);
    }

    /**
     * Gets the time the customer was removed from the system.
     * @return the removal time
     */
    public double getRemovalTime() {
        return removalTime;
    }

    /**
     * Sets the time the customer was removed from the system.
     * @param removalTime the removal time
     */
    public void setRemovalTime(double removalTime) {
        this.removalTime = removalTime;
    }

    /**
     * Gets the time the customer arrived in the system.
     * @return the arrival time
     */
    public double getArrivalTime() {
        return arrivalTime;
    }

    /**
     * Sets the time the customer arrived in the system.
     * @param arrivalTime the arrival time
     */
    public void setArrivalTime(double arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    /**
     * Checks if the customer's order is faulty.
     * @return true if faulty, false otherwise
     */
    public boolean getIsFaulty(){
        return isFaulty;
    }

    /**
     * Sets whether the customer's order is faulty.
     * @param isFaulty true if faulty, false otherwise
     */
    public void setIsFaulty(boolean isFaulty){
        this.isFaulty = isFaulty;
    }

    /**
     * Reports a payment issue for the customer and logs the event.
     */
    public void reportPaymentIssue(){
        Trace.out(Trace.Level.ERR, "Customer " + id + " return to the Reception queue. Payment problem at" + Clock.getInstance().getTime());
    }

    /**
     * Reports the results for the customer, including times and statistics.
     * Also triggers visualization callbacks for successful completion.
     */
    public void reportResults() {
        Trace.out(Trace.Level.INFO, "\nCustomer " + id + " ready! ");
        Trace.out(Trace.Level.INFO, "Customer "   + id + " arrived: " + arrivalTime);
        Trace.out(Trace.Level.INFO,"Customer "    + id + " removed: " + removalTime);
        Trace.out(Trace.Level.INFO,"Customer "    + id + " stayed: "  + (removalTime - arrivalTime));

        // Call visualization hook for successful completion
        try {
            controller.MainScreenController mainController = controller.MainScreenController.getInstance();
            if (mainController != null) {
                mainController.onCustomerServed(String.valueOf(id));
            }
        } catch (Exception e) {
            // Ignore if visualization controller not available
        }

        double responseTime = removalTime - arrivalTime;
        sum += responseTime;
        totalResponseTime += responseTime;
        double mean = sum/id;
        System.out.println("Current mean of the customer service times " + mean + "\n");
    }
    
    /**
     * Gets the total response time for all customers.
     * @return the total response time
     */
    public static double getTotalResponseTime() {
        return totalResponseTime;
    }
    
    /**
     * Resets the total response time for all customers.
     */
    public static void resetTotalResponseTime() {
        totalResponseTime = 0.0;
    }

    /**
     * Gets the unique ID of the customer.
     * @return the customer ID
     */
    public int getId(){
        return id;
    }

    /**
     * Checks if the customer is a walk-in.
     * @return true if walk-in, false otherwise
     */
    public boolean isWalkIn(){
        return walkIn;
    }
    
    /**
     * Gets the type of the customer.
     * @return the customer type
     */
    public CustomerType getCustomerType() {
        return customerType;
    }
    
    /**
     * Gets the time the customer started being served.
     * @return the service start time
     */
    public double getServiceStartTime() {
        return serviceStartTime;
    }
    
    /**
     * Sets the time the customer started being served.
     * @param serviceStartTime the service start time
     */
    public void setServiceStartTime(double serviceStartTime) {
        this.serviceStartTime = serviceStartTime;
    }
    
    /**
     * Gets the response time for the customer (removal time - arrival time).
     * @return the response time, or 0 if not available
     */
    public double getResponseTime() {
        if (removalTime > 0) {
            return removalTime - arrivalTime;
        }
        return 0;
    }
    
    /**
     * Gets the service time for the customer (removal time - service start time).
     * @return the service time, or 0 if not available
     */
    public double getServiceTime() {
        if (removalTime > 0 && serviceStartTime > 0) {
            return removalTime - serviceStartTime;
        }
        return 0;
    }

    /**
     * Checks if the customer is returning (e.g., due to payment issue).
     * @return true if returning, false otherwise
     */
    public boolean getCustomerBack(){
        return customerBack;
    }

    /**
     * Sets whether the customer is returning.
     * @param customerBack true if returning, false otherwise
     */
    public void setCustomerBack(boolean customerBack){
        this.customerBack = customerBack;
    }

    /**
     * Sets the time the customer arrived at a service point.
     * @param time the service point arrival time
     */
    public void setServicePointArrivalTime(double time){
        servicePointArrival = time;
    }

    /**
     * Gets the time the customer arrived at a service point.
     * @return the service point arrival time
     */
    public double getServicePointArrival(){
        return servicePointArrival;
    }

    /**
     * Checks if the customer is currently in the kitchen.
     * @return true if in kitchen, false otherwise
     */
    public boolean getOnKitchen(){
        return onKitchen;
    }

    /**
     * Sets whether the customer is currently in the kitchen.
     * @param onKitchen true if in kitchen, false otherwise
     */
    public void setOnKitchen(boolean onKitchen){
        this.onKitchen = onKitchen;
    }
}
