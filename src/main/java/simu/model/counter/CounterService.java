package simu.model.counter;

import controller.MainScreenController;
import eduni.distributions.Bernoulli;
import eduni.distributions.ContinuousGenerator;
import simu.framework.Clock;
import simu.framework.Event;
import simu.framework.EventList;
import simu.framework.statistics.SimulationStatistics;
import simu.framework.statistics.counter.CounterStatistics;
import simu.framework.statistics.delivery.DeliveryStatistics;
import simu.framework.statistics.kitchen.KitchenStatistics;
import simu.framework.statistics.reception.ReceptionStatistics;
import simu.framework.Trace;
import simu.model.Customer;
import simu.model.EventType;
import simu.model.ServicePoint;

/**
 * CounterService handles customer checkouts and routing of faulty orders.
 * Uses framework statistics for tracking and manages the logic for normal and special departures.
 *
 * @author (your name)
 */
public class CounterService extends ServicePoint {

    /** Counter statistics for this service point. */
    private CounterStatistics counterStatistics = CounterStatistics.getInstance();
    /** Delivery statistics for this service point. */
    private DeliveryStatistics deliveryStatistics = DeliveryStatistics.getInstance();
    /** Kitchen statistics for this service point. */
    private KitchenStatistics kitchenStatistics = KitchenStatistics.getInstance();
    /** Reception statistics for this service point. */
    private ReceptionStatistics receptionStatistics = ReceptionStatistics.getInstance();
    /** Simulation statistics for the whole simulation. */
    private SimulationStatistics simulationStatistics = SimulationStatistics.getInstance();
    /** Bernoulli distribution for faulty order routing (50% kitchen, 50% reception). */
    private Bernoulli fixProblemPath = new Bernoulli(0.5);

    /**
     * Constructs a CounterService with the given generator, event list, and event type.
     * @param generator the service time generator
     * @param eventList the event list for scheduling events
     * @param eventType the type of event this service point handles
     */
    public CounterService(ContinuousGenerator generator, EventList eventList, EventType eventType) {
        super(generator, eventList, eventType);
    }

    /**
     * Begins service for the next customer in the queue, schedules the appropriate event,
     * and updates statistics. Handles both normal and faulty order logic.
     */
    @Override
    public void beginService() {
        if (!isOnQueue() || isReserved()) return;

        reserved = true;
        double serviceTime = generator.sample();
        Customer customer = jono.peek();
        updateQueueStats();

        if (!customer.isWalkIn()) {
            this.eventTypeScheduled = EventType.DepartureFromCounterToDelivery;
            Trace.out(Trace.Level.INFO, "Counter: Customer #" + customer.getId() + " checkout being processed for delivery. New event \"" + this.eventTypeScheduled + "\" scheduled at " + (Clock.getInstance().getTime()+serviceTime));
        }
        else if (customer.getIsFaulty() == true) {
            if (fixProblemPath.sample() == 1) {
                this.eventTypeScheduled = EventType.CounterErrorToKitchen;
                Trace.out(Trace.Level.INFO, "Counter: Customer #" + customer.getId() + " checkout with faulty order being processed. New event  \"" + this.eventTypeScheduled + "\" scheduled at " + (Clock.getInstance().getTime()+serviceTime));
            } else {
                this.eventTypeScheduled = EventType.CounterErrorToReception;
                Trace.out(Trace.Level.INFO, "Counter: Customer #" + customer.getId() + " checkout with faulty order being processed. New event  \"" + this.eventTypeScheduled + "\" scheduled at " + (Clock.getInstance().getTime() + serviceTime));
            }
        }
        else {
            Trace.out(Trace.Level.INFO, "Counter: Customer " + customer.getId() + " checkout being processed. End time at " + (Clock.getInstance().getTime() + serviceTime));
            this.eventTypeScheduled = EventType.DepartureFromCounterToCostumer;
        }

        // Schedule event
        eventList.add(new Event(this.eventTypeScheduled, Clock.getInstance().getTime() + serviceTime));
        counterStatistics.addServiceBusyTime(serviceTime);
    }

    /**
     * Handles the departure of a customer from the counter, updating statistics and waiting times.
     * @return the departing customer
     */
    @Override
    public Customer handleDeparture(){
        Customer customer = removeQueue();
        counterStatistics.incrementServicedCustomers();
        deliveryStatistics.incrementArrivedCustomers();
        counterStatistics.addServiceWaitingTime(Clock.getInstance().getTime()-customer.getServicePointArrival() );
        customer.setServicePointArrivalTime(Clock.getInstance().getTime());
        return customer;
    }

    /**
     * Handles special departures (e.g., faulty orders to kitchen or reception, normal checkout).
     * Updates statistics and triggers visualization callbacks as needed.
     * @param eventType the type of special departure event
     * @return the departing customer, or null if handled internally
     */
    public Customer handleSpecialDeparture(EventType eventType) {
        Customer customer;

        switch (eventType) {
            case CounterErrorToKitchen:
                customer = removeQueue();
                counterStatistics.addServiceWaitingTime(Clock.getInstance().getTime()-customer.getServicePointArrival() );
                customer.setServicePointArrivalTime(Clock.getInstance().getTime());
                counterStatistics.incrementServicedCustomers();
                kitchenStatistics.incrementArrivedCustomers();
                simulationStatistics.incrementRemakeOrdersCustomers();
                if (customer != null) {
                    Trace.out(Trace.Level.INFO, "Counter: Customer #" + customer.getId() + " with faulty order returned to kitchen que to remake order again.");
                }
                return customer;

            case CounterErrorToReception:
                customer = removeQueue();
                counterStatistics.addServiceWaitingTime(Clock.getInstance().getTime()-customer.getServicePointArrival() );
                customer.setServicePointArrivalTime(Clock.getInstance().getTime());
                counterStatistics.incrementServicedCustomers();
                receptionStatistics.incrementArrivedCustomers();
                if (customer != null) {
                    Trace.out(Trace.Level.INFO, "Counter: Customer #" + customer.getId() + " with faulty order returned to reception que to get money back.");
                }
                return customer;
            case DepartureFromCounterToCostumer:
                customer = removeQueue();
                if (customer != null) {
                    counterStatistics.addServiceWaitingTime(Clock.getInstance().getTime()-customer.getServicePointArrival() );
                    customer.setRemovalTime(Clock.getInstance().getTime());
                    counterStatistics.incrementServicedCustomers();
                    simulationStatistics.incrementTotalServicedCustomers();
                    simulationStatistics.addTotalWaitingTime(customer.getRemovalTime() - customer.getArrivalTime());
                    customer.reportResults();
                    
                    // Visualization callback for customer completion
                    try {
                        controller.MainScreenController mainScreenController = controller.MainScreenController.getInstance();
                        if (mainScreenController != null) {
                            mainScreenController.onCustomerServed("Customer #" + customer.getId());
                        }
                    } catch (Exception e) {
                        // Ignore visualization errors to prevent simulation disruption
                    }
                }
                return null;
                
            default:
                return null; // For all other event types not handled by this service point
        }
    }

    /**
     * Returns the next service point for special departures (e.g., kitchen or reception).
     * @param eventType the type of special departure event
     * @param allServicePoints array of all service points
     * @return the next service point, or null if not applicable
     */
    @Override
    public ServicePoint getNextServicePointForSpecialDeparture(EventType eventType, ServicePoint[] allServicePoints) {
        switch (eventType) {
            case CounterErrorToKitchen:
                return allServicePoints[1]; // Kitchen service point
            case CounterErrorToReception:
                return allServicePoints[0]; // Reception service point
            default:
                return null; // For all other event types that don't require special routing
        }
    }
    
    /**
     * Get statistics for external access.
     * @return the CounterStatistics instance for this service point
     */
    public CounterStatistics getStatistics() {
        return counterStatistics;
    }
}
