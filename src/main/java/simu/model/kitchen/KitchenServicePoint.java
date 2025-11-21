package simu.model.kitchen;

import simu.model.Customer;
import simu.model.EventType;
import simu.model.ServicePoint;
import simu.framework.statistics.counter.CounterStatistics;
import simu.framework.statistics.kitchen.KitchenStatistics;
import eduni.distributions.ContinuousGenerator;
import eduni.distributions.Uniform;
import simu.framework.Clock;
import simu.framework.Event;
import simu.framework.EventList;
import simu.framework.Trace;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * Represents the kitchen service point in the simulation.
 * Manages a list of cooks, handles customer service logic, and updates kitchen and counter statistics.
 * Responsible for assigning cooks to orders, tracking order preparation, and handling departures.
 */
public class KitchenServicePoint extends ServicePoint {
    /** List of cooks available in the kitchen. */
    ArrayList<Cook> cooks = new ArrayList<>();

    /** Kitchen statistics instance for tracking kitchen-related metrics. */
    private KitchenStatistics kitchenStatistics = KitchenStatistics.getInstance();
    /** Counter statistics instance for tracking counter-related metrics. */
    private CounterStatistics counterStatistics = CounterStatistics.getInstance();

    /**
     * Constructs a KitchenServicePoint with the given generator, event list, event type, and cook competencies.
     * @param generator the generator for service times
     * @param eventList the event list for scheduling events
     * @param eventType the type of event this service point handles
     * @param competencies list of cook competencies to initialize cooks
     */
    public KitchenServicePoint(ContinuousGenerator generator, EventList eventList, EventType eventType, ArrayList<CookCompetency> competencies) {
        super(generator, eventList, eventType);
        for (CookCompetency competency : competencies) {
            cooks.add(new Cook(competency, generator));
        }
    }

    /**
     * Begins service for the next customer in the queue.
     * Randomly shuffles cooks, assigns an available cook, prepares the order, and schedules the next event.
     * Updates statistics and triggers visualization callbacks as needed.
     */
    @Override
    public void beginService() { //muutin sen, jotta se toimii oikein kokkien kanssa

        Collections.shuffle(cooks, new Random());

        if (cooks.stream().anyMatch(cook -> !cook.isBusy())) {

            Customer customer = null;
            for (Customer c : jono) {
                if (!c.getOnKitchen()) {
                    customer = c;
                    customer.setOnKitchen(true);
                    break;
                }
            }

            if (customer == null) {
                return;
            }

            // Call visualization hook
            try {
                controller.MainScreenController mainController = controller.MainScreenController.getInstance();
                if (mainController != null) {
                    mainController.onServiceBegin("Kitchen", String.valueOf(customer.getId()));
                }
            } catch (Exception e) {
                // Ignore if visualization controller not available
            }

            int selectedCookIndex=-1;
            for (int i = 0; i < cooks.size(); i++) {
                Cook cook = cooks.get(i);
                if (!cook.isBusy()) {
                    cook.setBusy(true);
                    selectedCookIndex = i;
                    break;
                }
            }


            Cook selectedCook = cooks.get(selectedCookIndex);
            Order preparedOrder = selectedCook.prepareMeal();
            selectedCook.setOrderFinishTime(Clock.getInstance().getTime()+preparedOrder.getPreparationTime());


            // Set customer as faulty only if the order preparation failed
            if (preparedOrder.isFailed()) {
                customer.setIsFaulty(true);
                Trace.out(Trace.Level.INFO, "Kitchen: Customer #" + customer.getId() + " order will be FAILED by " +
                        selectedCook.getCompetency() + " cook (index=" + (selectedCookIndex+1) +
                        ". New event \"" + this.eventTypeScheduled + "\" scheduled at " + (Clock.getInstance().getTime() + preparedOrder.getPreparationTime()));
            } else {
                customer.setIsFaulty(false);
                Trace.out(Trace.Level.INFO, "Kitchen: Customer #" + customer.getId() + " order will be SUCCEED by " +
                        selectedCook.getCompetency() + " cook (index=" + (selectedCookIndex+1) +
                        "). New event \"" + this.eventTypeScheduled + "\" scheduled at " + (Clock.getInstance().getTime() + preparedOrder.getPreparationTime()));
            }

            kitchenStatistics.addServiceBusyTime(preparedOrder.getPreparationTime());
            eventList.add(new Event(this.eventTypeScheduled, Clock.getInstance().getTime() + preparedOrder.getPreparationTime()));
            }

    }

    
    /**
     * Handles the departure of a customer from the kitchen.
     * Updates statistics, resets cook busy states, and triggers visualization callbacks.
     * @return the departing customer
     */
    @Override
    public Customer handleDeparture() {//Muutin myös handledeparture function tätä varten
        Customer customer = removeQueue();
        kitchenStatistics.incrementServicedCustomers();
        counterStatistics.incrementArrivedCustomers();
        kitchenStatistics.addServiceWaitingTime(Clock.getInstance().getTime() - customer.getServicePointArrival());
        customer.setServicePointArrivalTime(Clock.getInstance().getTime());
        customer.setOnKitchen(false);
        for (Cook cook : cooks) {
            if (cook.getOrderFinishTime()==Clock.getInstance().getTime()){
                cook.setBusy(false);
            }
        }

        // Call visualization hook
        try {
            controller.MainScreenController mainController = controller.MainScreenController.getInstance();
            if (mainController != null) {
                mainController.onCustomerDeparture("Kitchen", String.valueOf(customer.getId()), "Counter");
            }
        } catch (Exception e) {
            // Ignore if visualization controller not available
        }

        return customer;
    }

    
    /**
     * Gets the kitchen statistics for external access.
     * @return the KitchenStatistics instance
     */
    public KitchenStatistics getStatistics() {
        return kitchenStatistics;
    }

    /**
     * Gets the list of cooks for testing or external access.
     * @return the list of cooks
     */
    public ArrayList<Cook> getCooks() {
        return cooks;
    }
}
