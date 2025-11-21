package simu.model.reception;

import eduni.distributions.Bernoulli;
import eduni.distributions.ContinuousGenerator;
import simu.framework.Clock;
import simu.framework.Event;
import simu.framework.EventList;
import simu.framework.statistics.SimulationStatistics;
import simu.framework.statistics.kitchen.KitchenStatistics;
import simu.framework.statistics.reception.ReceptionStatistics;
import simu.framework.Trace;
import simu.model.Customer;
import simu.model.EventType;
import simu.model.ServicePoint;

/**
 * ReceptionService handles customer checkouts, payment processing, and money returns.
 * Uses framework statistics for tracking.
 */
public class ReceptionService extends ServicePoint {

    private ReceptionStatistics receptionStatistics = ReceptionStatistics.getInstance();
    private KitchenStatistics kitchenStatistics = KitchenStatistics.getInstance();
    private SimulationStatistics simulationStatistics = SimulationStatistics.getInstance();
    private Bernoulli errorPath = new Bernoulli(0.1);

    public ReceptionService (ContinuousGenerator generator, EventList eventList, EventType eventType){
        super(generator, eventList, eventType);
    }
    
    public ReceptionService (ContinuousGenerator generator, EventList eventList, EventType eventType, String name){
        super(generator, eventList, eventType, name);
    }

    public void beginService() {
        reserved = true;
        double serviceTime = generator.sample();
        Customer customer = jono.peek();
        updateQueueStats();

        // Call visualization hook
        try {
            controller.MainScreenController mainController = controller.MainScreenController.getInstance();
            if (mainController != null) {
                mainController.onServiceBegin("Reception", String.valueOf(customer.getId()));
            }
        } catch (Exception e) {
            // Ignore if visualization controller not available
        }

        if (customer.getIsFaulty()) {
            this.eventTypeScheduled = EventType.ReturnMoney;
            Trace.out(Trace.Level.INFO, "Walk-in customer for money back #" + customer.getId() + " being served. New event \"" + this.eventTypeScheduled + "\" scheduled at " + (Clock.getInstance().getTime()+serviceTime));
        }
        else if(errorPath.sample() == 1) {
            this.eventTypeScheduled = EventType.PaymentFailed;
            Trace.out(Trace.Level.INFO, "Reception: Customer #" + customer.getId() + " payment attempt being processed. New event \"" + this.eventTypeScheduled + "\" scheduled at " + (Clock.getInstance().getTime()+serviceTime));
        }
        else {
            this.eventTypeScheduled = EventType.DepartureFromReception;
            Trace.out(Trace.Level.INFO, "Reception: Customer #" + customer.getId() + " checkout being processed. New event \"" + this.eventTypeScheduled + "\" scheduled at " + (Clock.getInstance().getTime()+serviceTime));
        }

        eventList.add(new Event(this.eventTypeScheduled, Clock.getInstance().getTime() + serviceTime));
        receptionStatistics.addServiceBusyTime(serviceTime);
    }

    @Override
    public Customer handleDeparture() {
        Customer customer = removeQueue();
        receptionStatistics.incrementServicedCustomers();
        kitchenStatistics.incrementArrivedCustomers();
        receptionStatistics.addServiceWaitingTime(Clock.getInstance().getTime()-customer.getServicePointArrival());
        customer.setServicePointArrivalTime(Clock.getInstance().getTime());
        
        // Call visualization hook
        try {
            controller.MainScreenController mainController = controller.MainScreenController.getInstance();
            if (mainController != null) {
                mainController.onCustomerDeparture("Reception", String.valueOf(customer.getId()), "Kitchen");
            }
        } catch (Exception e) {
            // Ignore if visualization controller not available
        }
        
        return customer;
    }

    @Override
    public Customer handleSpecialDeparture(EventType eventType) {
        Customer customer;

        switch (eventType) {
            case ReturnMoney:
                customer = removeQueue();
                if (customer != null) {
                    // Call visualization hook
                    try {
                        controller.MainScreenController mainController = controller.MainScreenController.getInstance();
                        if (mainController != null) {
                            mainController.onSpecialDeparture("Reception", String.valueOf(customer.getId()), "Return Money");
                            // Note: onCustomerServed will be called by customer.reportResults()
                        }
                    } catch (Exception e) {
                        // Ignore if visualization controller not available
                    }
                    
                    receptionStatistics.addServiceWaitingTime(Clock.getInstance().getTime()-customer.getServicePointArrival());
                    customer.setRemovalTime(Clock.getInstance().getTime());
                    receptionStatistics.incrementServicedCustomers();
                    simulationStatistics.incrementTotalServicedCustomers();
                    simulationStatistics.incrementReturnMoneyCustomers();
                    simulationStatistics.addTotalWaitingTime(customer.getRemovalTime() - customer.getArrivalTime());
                    customer.reportResults();
                }
                return null; // Customer leaves system

            case PaymentFailed:
                customer = removeQueue();
                if (customer != null) {
                    // Call visualization hook
                    try {
                        controller.MainScreenController mainController = controller.MainScreenController.getInstance();
                        if (mainController != null) {
                            mainController.onSpecialDeparture("Reception", String.valueOf(customer.getId()), "Payment Failed");
                            mainController.onCustomerNotServed(String.valueOf(customer.getId()), "Payment Failed");
                        }
                    } catch (Exception e) {
                        // Ignore if visualization controller not available
                    }
                    
                    receptionStatistics.addServiceWaitingTime(Clock.getInstance().getTime()-customer.getServicePointArrival());
                    customer.setServicePointArrivalTime(Clock.getInstance().getTime());
                    receptionStatistics.incrementServicedCustomers();
                    receptionStatistics.incrementArrivedCustomers();
                    customer.reportPaymentIssue();
                }
                return customer; // Customer returns to same queue

            default:
                return handleDeparture();
        }
    }

    @Override
    public ServicePoint getNextServicePointForSpecialDeparture(EventType eventType, ServicePoint[] allServicePoints) {
        switch (eventType) {
            case PaymentFailed:
                return this; // Customer returns to reception
            case ReturnMoney:
                return null; // Customer leaves system
            default:
                return null; // Other event types not handled by reception
        }
    }
    
    /**
     * Get statistics for external access
     */
    public ReceptionStatistics getStatistics() {
        return receptionStatistics;
    }
}
