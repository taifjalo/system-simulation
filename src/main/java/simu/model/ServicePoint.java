package simu.model;

import eduni.distributions.ContinuousGenerator;
import simu.framework.Clock;
import simu.framework.Event;
import simu.framework.EventList;
import simu.framework.Trace;
import controller.MainScreenController;
import java.util.LinkedList;

/**
 * Represents a service point in the simulation, such as reception, kitchen, counter, or delivery.
 * Handles queue management, service logic, event scheduling, and reporting for customers.
 */
public class ServicePoint {
    /** Queue of customers at this service point. */
    protected LinkedList<Customer> jono = new LinkedList<Customer>();
    /** Generator for service times. */
    protected ContinuousGenerator generator;
    /** Event list for scheduling events. */
    protected EventList eventList;
    /** The type of event scheduled for this service point. */
    protected EventType eventTypeScheduled;
    /** Whether the service point is currently reserved (serving a customer). */
    protected boolean reserved = false;
    /** Name of the service point for event logging and visualization. */
    protected String servicePointName;

    /**
     * Constructs a ServicePoint with the given generator, event list, and event type.
     * @param generator the generator for service times
     * @param tapahtumalista the event list for scheduling events
     * @param tyyppi the type of event this service point handles
     */
    public ServicePoint(ContinuousGenerator generator, EventList tapahtumalista, EventType tyyppi){
        this.eventList = tapahtumalista;
        this.generator = generator;
        this.eventTypeScheduled = tyyppi;
        this.servicePointName = "SERVICE_POINT"; // Default name
    }

    /**
     * Constructs a ServicePoint with the given generator, event list, event type, and name.
     * @param generator the generator for service times
     * @param tapahtumalista the event list for scheduling events
     * @param tyyppi the type of event this service point handles
     * @param name the name of the service point
     */
    public ServicePoint(ContinuousGenerator generator, EventList tapahtumalista, EventType tyyppi, String name){
        this.eventList = tapahtumalista;
        this.generator = generator;
        this.eventTypeScheduled = tyyppi;
        this.servicePointName = name;
    }

    /**
     * Adds a customer to the queue and updates queue statistics.
     * Logs the queue entry event.
     * @param a the customer to add
     */
    public void addQueue(Customer a){
        updateQueueStats();
        jono.add(a);
        // Log queue entry event
        Trace.out(Trace.Level.INFO, "Customer " + a.getId() + " added to " + servicePointName + " queue");
    }

    /**
     * Removes the serviced customer from the queue, updates statistics, and logs the service end event.
     * @return the removed customer, or null if the queue is empty
     */
    public Customer removeQueue(){
        updateQueueStats();
        reserved = false;
        Customer customer = jono.poll();
        if (customer != null) {
            // Log service end event
            Trace.out(Trace.Level.INFO, "Customer " + customer.getId() + " service ended at " + servicePointName);
        }
        return customer;
    }

    /**
     * Begins a new service for the first customer in the queue, schedules the next event, and logs the service start.
     * Notifies visualization of service beginning.
     */
    public void beginService() {
        reserved = true;
        // Log service start for the first customer in queue
        if (!jono.isEmpty()) {
            Customer customer = jono.peek();
            Trace.out(Trace.Level.INFO, "Customer " + customer.getId() + " service started at " + servicePointName);
            // Notify visualization of service beginning
            try {
                MainScreenController controller = MainScreenController.getInstance();
                if (controller != null) {
                    controller.onServiceBegin(servicePointName, String.valueOf(customer.getId()));
                }
            } catch (Exception e) {
                // Ignore visualization errors to not break simulation
            }
        }
        double serviceTime = generator.sample();
        eventList.add(new Event(eventTypeScheduled, Clock.getInstance().getTime()+serviceTime));
    }

    /**
     * Checks if the service point is currently reserved (serving a customer).
     * @return true if reserved, false otherwise
     */
    public boolean isReserved(){
        return reserved;
    }

    /**
     * Checks if there are customers in the queue.
     * @return true if the queue is not empty, false otherwise
     */
    public boolean isOnQueue(){
        return jono.size() != 0;
    }

    /**
     * Gets the generator for service times.
     * @return the service time generator
     */
    public ContinuousGenerator getGeneratorMean() {
        return generator;
    }

    /**
     * Updates queue statistics. Adjusts effective queue length if the service point is reserved.
     */
    public void updateQueueStats() {
        int effectiveQueueLength = jono.size();
        // If service point is reserved, subtract one (customer in service is not in queue)
        if (reserved && effectiveQueueLength > 0) {
            effectiveQueueLength--;
        }
    }

    /**
     * Gets the name of the service point.
     * @return the service point name
     */
    public String getServicePointName() {
        return servicePointName;
    }

    /**
     * Handles customer departure from this service point.
     * Returns the customer to be routed to the next service point, or null if the customer leaves the system.
     * Notifies visualization of customer departure.
     * @return the departing customer, or null
     */
    public Customer handleDeparture() {
        Customer customer = removeQueue();
        // Notify visualization of customer departure
        if (customer != null) {
            try {
                MainScreenController controller = MainScreenController.getInstance();
                if (controller != null) {
                    controller.onCustomerDeparture(servicePointName, String.valueOf(customer.getId()), "Next Service Point");
                }
            } catch (Exception e) {
                // Ignore visualization errors to not break simulation
            }
        }
        return customer;
    }

    /**
     * Determines where the customer should go after a normal departure.
     * @param allServicePoints array of all service points
     * @param index the index of the current service point
     * @return the next service point
     */
    public ServicePoint getNextServicePoint(ServicePoint[] allServicePoints, int index) {
        return allServicePoints[index + 1];
    }

    /**
     * Handles special departure cases (like payment failures, returns, etc.).
     * Returns the customer to be routed, or null if the customer leaves or stays.
     * Notifies visualization of special departure.
     * @param eventType the type of special departure event
     * @return the departing customer, or null
     */
    public Customer handleSpecialDeparture(EventType eventType) {
        Customer customer = handleDeparture(); // Default implementation
        // Notify visualization of special departure
        if (customer != null) {
            try {
                MainScreenController controller = MainScreenController.getInstance();
                if (controller != null) {
                    controller.onSpecialDeparture(servicePointName, String.valueOf(customer.getId()), eventType.toString());
                }
            } catch (Exception e) {
                // Ignore visualization errors to not break simulation
            }
        }
        return customer;
    }

    /**
     * Determines where the customer should go after a special departure.
     * @param eventType the type of special departure event
     * @param allServicePoints array of all service points
     * @return the next service point, or null if the customer leaves the system
     */
    public ServicePoint getNextServicePointForSpecialDeparture(EventType eventType, ServicePoint[] allServicePoints) {
        return null; // Default: customer leaves the system
    }

    /**
     * Gets the queue of customers for testing or external access.
     * @return the customer queue
     */
    public LinkedList<Customer> getQueue(){
        return jono;
    }
}
