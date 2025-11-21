package simu.model.delivery;

import controller.MainScreenController;
import eduni.distributions.Bernoulli;
import eduni.distributions.ContinuousGenerator;
import simu.framework.Clock;
import simu.framework.Event;
import simu.framework.EventList;
import simu.framework.statistics.SimulationStatistics;
import simu.framework.statistics.delivery.DeliveryStatistics;
import simu.framework.statistics.kitchen.KitchenStatistics;
import simu.framework.statistics.reception.ReceptionStatistics;
import simu.framework.Trace;
import simu.model.Customer;
import simu.model.EventType;
import simu.model.ServicePoint;

/**
 * DeliveryService handles call-in customers for delivery.
 * Uses framework statistics for tracking and manages the logic for normal and special departures.
 *
 * @author (your name)
 */
public class DeliveryService extends ServicePoint {
    /** Delivery statistics for this service point. */
    private DeliveryStatistics deliveryStatistics = DeliveryStatistics.getInstance();
    /** Kitchen statistics for this service point. */
    private KitchenStatistics kitchenStatistics = KitchenStatistics.getInstance();
    /** Simulation statistics for the whole simulation. */
    private SimulationStatistics simulationStatistics = SimulationStatistics.getInstance();
    /** Bernoulli distribution for remake choice (default 70% remake). */
    private Bernoulli remakeChoice = new Bernoulli(0.7);
    
    /**
     * Constructs a DeliveryService with the given generator, event list, and event type.
     * @param generator the service time generator
     * @param eventList the event list for scheduling events
     * @param eventType the type of event this service point handles
     */
    public DeliveryService(ContinuousGenerator generator, EventList eventList, EventType eventType) {
        super(generator, eventList, eventType);
    }
    
    /**
     * Constructs a DeliveryService with the given generator, event list, event type, and name.
     * @param generator the service time generator
     * @param eventList the event list for scheduling events
     * @param eventType the type of event this service point handles
     * @param name the name of the service point
     */
    public DeliveryService(ContinuousGenerator generator, EventList eventList, EventType eventType, String name) {
        super(generator, eventList, eventType, name);
    }

    /**
     * Begins service for the next customer in the queue, schedules the appropriate event,
     * and updates statistics. Handles both normal and faulty order logic.
     */
    @Override
    public void beginService() {
        reserved = true;
        double serviceTime = generator.sample();
        Customer customer = jono.peek();
        updateQueueStats();
        
        if (!customer.isWalkIn()) {
            if (customer.getIsFaulty()) {
                if (remakeChoice.sample() == 1) {
                    this.eventTypeScheduled = EventType.RemakeOrder;
                    Trace.out(Trace.Level.INFO, "Customer #" + customer.getId() + "will request remake for faulty delivery. New event \"" + this.eventTypeScheduled + "\" scheduled at " + (Clock.getInstance().getTime()+serviceTime));
                } else {
                    this.eventTypeScheduled = EventType.DeliveryRefused;
                    Trace.out(Trace.Level.INFO, "Customer #" + customer.getId() + "will refuse faulty delivery. New event \"" + this.eventTypeScheduled + "\" scheduled at " + (Clock.getInstance().getTime()+serviceTime));
                }
            } else {
                this.eventTypeScheduled = EventType.DepartureFromDelivery;
                Trace.out(Trace.Level.INFO, "Customer #" + customer.getId() + " delivery will be completed successfully. New event \"" + this.eventTypeScheduled + "\" scheduled at " + (Clock.getInstance().getTime()+serviceTime));
            }
        }
        
        eventList.add(new Event(this.eventTypeScheduled, Clock.getInstance().getTime() + serviceTime));
        deliveryStatistics.addServiceBusyTime(serviceTime);
    }
    
    /**
     * Handles special departures (e.g., remake, refused, or successful delivery).
     * Updates statistics and triggers visualization callbacks as needed.
     * @param eventType the type of special departure event
     * @return the departing customer, or null if handled internally
     */
    @Override
    public Customer handleSpecialDeparture(EventType eventType) {
        Customer customer;
        
        switch (eventType) {
            case DepartureFromDelivery:
                customer = removeQueue();
                if (customer != null) {
                    deliveryStatistics.addServiceWaitingTime(Clock.getInstance().getTime()-customer.getServicePointArrival());
                    customer.setRemovalTime(Clock.getInstance().getTime());
                    deliveryStatistics.incrementServicedCustomers();
                    simulationStatistics.incrementTotalServicedCustomers();
                    simulationStatistics.addTotalWaitingTime(customer.getRemovalTime() - customer.getArrivalTime());
                    customer.reportResults();
                    
                    // Notify visualization of successful service completion
                    try {
                        MainScreenController controller = MainScreenController.getInstance();
                        if (controller != null) {
                            controller.onCustomerServed(String.valueOf(customer.getId()));
                        }
                    } catch (Exception e) {
                        // Ignore visualization errors to not break simulation
                    }
                }
                return null; // Customer leaves system
                
            case DeliveryRefused:
                customer = removeQueue();
                if (customer != null) {
                    deliveryStatistics.addServiceWaitingTime(Clock.getInstance().getTime()-customer.getServicePointArrival());
                    customer.setRemovalTime(Clock.getInstance().getTime());
                    deliveryStatistics.incrementServicedCustomers();
                    simulationStatistics.incrementTotalServicedCustomers();
                    simulationStatistics.incrementRefusedDeliveryCustomers();
                    simulationStatistics.addTotalWaitingTime(customer.getRemovalTime() - customer.getArrivalTime());
                    Trace.out(Trace.Level.INFO, "Customer #" + customer.getId() + " refused faulty delivery - no payment");
                    customer.reportResults();
                    
                    // Notify visualization of customer not served
                    try {
                        MainScreenController controller = MainScreenController.getInstance();
                        if (controller != null) {
                            controller.onCustomerNotServed(String.valueOf(customer.getId()), "Delivery refused");
                        }
                    } catch (Exception e) {
                        // Ignore visualization errors to not break simulation
                    }
                }
                return null; // Customer leaves system
                
            case RemakeOrder:
                customer = removeQueue();
                if (customer != null) {
                    deliveryStatistics.addServiceWaitingTime(Clock.getInstance().getTime()-customer.getServicePointArrival());
                    customer.setServicePointArrivalTime(Clock.getInstance().getTime());
                    deliveryStatistics.incrementServicedCustomers();
                    kitchenStatistics.incrementArrivedCustomers();
                    simulationStatistics.incrementRemakeOrdersCustomers();
                    customer.setIsFaulty(false);
                    Trace.out(Trace.Level.INFO, "Customer #" + customer.getId() + " sent back to kitchen for remake");
                }
                return customer; // Customer will be routed to kitchen
                
            default:
                return super.handleSpecialDeparture(eventType);
        }
    }
    
    /**
     * Returns the next service point for special departures (e.g., kitchen for remake).
     * @param eventType the type of special departure event
     * @param allServicePoints array of all service points
     * @return the next service point, or null if not applicable
     */
    @Override
    public ServicePoint getNextServicePointForSpecialDeparture(EventType eventType, ServicePoint[] allServicePoints) {
        switch (eventType) {
            case RemakeOrder:
                return allServicePoints[1]; // Kitchen service point
            case DepartureFromDelivery:
            case DeliveryRefused:
            default:
                return null; // Customer leaves system
        }
    }
    
    /**
     * Get statistics for external access.
     * @return the DeliveryStatistics instance for this service point
     */
    public DeliveryStatistics getStatistics() {
        return deliveryStatistics;
    }
}