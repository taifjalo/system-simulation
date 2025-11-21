package simu.model;

import controller.Controller;
import eduni.distributions.Negexp;
import eduni.distributions.Normal;
import simu.framework.*;
import simu.framework.statistics.SimulationStatistics;
import simu.framework.statistics.counter.CounterStatistics;
import simu.framework.statistics.delivery.DeliveryStatistics;
import simu.framework.statistics.kitchen.KitchenStatistics;
import simu.framework.statistics.reception.ReceptionStatistics;
import simu.model.counter.CounterService;
import simu.model.delivery.DeliveryService;
import simu.model.kitchen.KitchenServicePoint;
import simu.model.reception.ReceptionService;

/**
 * Simulation engine for the restaurant simulation model.
 * Extends the base Engine class and manages arrival processes, service points, event handling, and statistics.
 * Responsible for initializing the simulation, running events, collecting results, and saving data to the database.
 */
public class MyEngine extends Engine {
    /** Arrival process for call-in customers. */
    private ArrivalProcess arrivalProcessCall;
    /** Arrival process for restaurant (walk-in) customers. */
    private ArrivalProcess arrivalProcessRestaurant;
    /** Reception statistics instance. */
    private ReceptionStatistics receptionStatistics = ReceptionStatistics.getInstance();
    /** Kitchen statistics instance. */
    private KitchenStatistics kitchenStatistics = KitchenStatistics.getInstance();
    /** Counter statistics instance. */
    private CounterStatistics counterStatistics = CounterStatistics.getInstance();
    /** Delivery statistics instance. */
    private DeliveryStatistics deliveryStatistics = DeliveryStatistics.getInstance();
    /** Simulation statistics instance. */
    private SimulationStatistics simulationStatistics = SimulationStatistics.getInstance();
    /** The simulation start time. */
    private double simulationStartTime;


    /**
     * Constructs the simulation engine and initializes all service points and arrival processes.
     * @param controller the main controller for visualization and callbacks
     */
    public MyEngine(Controller controller) { // NEW
        super(controller); // NEW

        servicePoints = new ServicePoint[4]; // Increased from 3 to 4

        servicePoints[0] = new ReceptionService(new Normal(receptionStatistics.getMean(), receptionStatistics.getVariance()), eventList, EventType.DepartureFromReception); //Lisäsin oman Recdeption Service Listaan.
        servicePoints[1] = new KitchenServicePoint(new Normal(kitchenStatistics.getMean(), kitchenStatistics.getVariance()), eventList, EventType.DepartureFromKitchen,controller.getCookLevels());
        servicePoints[2] = new CounterService(new Normal(counterStatistics.getMean(), kitchenStatistics.getVariance()), eventList, EventType.DepartureFromCounterToCostumer);
        servicePoints[3] = new DeliveryService(new Normal(deliveryStatistics.getMean(), kitchenStatistics.getVariance()), eventList, EventType.DepartureFromDelivery); // New delivery service

        arrivalProcessRestaurant = new ArrivalProcess(new Negexp(simulationStatistics.getWalkInMeanTime(), 5), eventList, EventType.ArrivalRestaurant);
        arrivalProcessCall = new ArrivalProcess(new Negexp(simulationStatistics.getCallInMeanTime(), 5), eventList, EventType.ArrivalCall);
    }

    /**
     * Initializes the simulation, resets statistics, and schedules the first arrivals.
     */
    @Override
    protected void initialization() {
        simulationStartTime = Clock.getInstance().getTime();

        Trace.out(Trace.Level.INFO, "=== SIMULATION INITIALIZED ===");
        Trace.out(Trace.Level.INFO, "Database collector ready for new normalized schema");
        Trace.out(Trace.Level.INFO, "All statistics were reset during construction");

        arrivalProcessRestaurant.generateNext(); // First arrival to the system in restaurant
        arrivalProcessCall.generateNext(); // First arrival to the system by call
    }

    /**
     * Handles simulation events by type, including arrivals, departures, and special events.
     * @param t the event to process
     */
    @Override
    protected void runEvent(Event t) {  // B phase events

        switch ((EventType) t.getType()) {

            case ArrivalRestaurant:
                Customer walkInCustomer = new Customer(true);
                walkInCustomer.setServicePointArrivalTime(Clock.getInstance().getTime());
                walkInCustomer.setArrivalTime(Clock.getInstance().getTime());//lisäsin tään setArrivalTime
                servicePoints[0].addQueue(walkInCustomer);
                // Track arrival in framework statistics
                simulationStatistics.incrementTotalArrivedCustomers();
                receptionStatistics.incrementArrivedCustomers();
                
                // Call visualization hook
                try {
                    controller.MainScreenController mainController = controller.MainScreenController.getInstance();
                    if (mainController != null) {
                        mainController.onCustomerArrival(true); // true = walk-in
                    }
                } catch (Exception e) {
                    // Ignore if visualization controller not available
                }
                
                arrivalProcessRestaurant.generateNext();
                break;

            case ArrivalCall:
                Customer callInCustomer = new Customer(false);
                callInCustomer.setServicePointArrivalTime(Clock.getInstance().getTime());
                servicePoints[0].addQueue(callInCustomer);
                // Track arrival in framework statistics
                simulationStatistics.incrementTotalArrivedCustomers();
                receptionStatistics.incrementArrivedCustomers();
                
                // Call visualization hook
                try {
                    controller.MainScreenController mainController = controller.MainScreenController.getInstance();
                    if (mainController != null) {
                        mainController.onCustomerArrival(false); // false = call-in
                    }
                } catch (Exception e) {
                    // Ignore if visualization controller not available
                }
                
                arrivalProcessCall.generateNext();// NEW
                break;

            case ReturnMoney:
                handleServicePointDeparture(0, EventType.ReturnMoney);
                break;

            case PaymentFailed:
                handleServicePointDeparture(0, EventType.PaymentFailed);
                break;

            case DepartureFromReception:
                handleServicePointDeparture(0, EventType.DepartureFromReception);
                break;

            case DepartureFromKitchen:
                handleServicePointDeparture(1, EventType.DepartureFromKitchen);
                break;

            case DepartureFromCounterToCostumer:
                handleServicePointDeparture(2, EventType.DepartureFromCounterToCostumer);
                break;

            case CounterErrorToKitchen:
                handleServicePointDeparture(2, EventType.CounterErrorToKitchen);
                break;

            case CounterErrorToReception:
                handleServicePointDeparture(2, EventType.CounterErrorToReception);
                break;

            case DepartureFromCounterToDelivery:
                handleServicePointDeparture(2, EventType.DepartureFromCounterToDelivery);
                break;

            case DepartureFromDelivery:
                handleServicePointDeparture(3, EventType.DepartureFromDelivery);
                break;

            case DeliveryRefused:
                handleServicePointDeparture(3, EventType.DeliveryRefused);
                break;

            case RemakeOrder:
                handleServicePointDeparture(3, EventType.RemakeOrder);
                break;

		}
	}

    /**
     * Collects and displays simulation results, saves data to the database, and outputs summary information.
     */
    @Override
    protected void results() {
        Double time = Clock.getInstance().getTime();
        simulationStatistics.showSimulationStatistics(time);
        receptionStatistics.showServicePointStatistics(time);
        kitchenStatistics.showServicePointStatistics(time);
        counterStatistics.showServicePointStatistics(time);
        deliveryStatistics.showServicePointStatistics(time);
        saveSimulationToDatabase();
        Trace.out(Trace.Level.INFO, "\n=== SIMULATION COMPLETED ===");

	}

	/**
	 * Save simulation data to database using the new normalized schema
	 */
	private void saveSimulationToDatabase() {
		try {
			// Use framework statistics to save to database
			simu.framework.statistics.SimulationStatistics simStats = simu.framework.statistics.SimulationStatistics.getInstance();
			
			// The simulation time is already set correctly by showSimulationStatistics() 
			// in the results() method, so don't recalculate it here
			
		// Save everything to database using framework statistics
		simu.backend.entity.OverviewStatistics savedStats = simStats.saveToDatabase();
		if (savedStats != null) {
            Trace.out(Trace.Level.INFO, "Database storage successful. Overview ID: " + savedStats.getId());
            Trace.out(Trace.Level.INFO, "Simulation Time: " + savedStats.getSimulationTime());
            Trace.out(Trace.Level.INFO, "Total Simulation Time: " + savedStats.getTotalSimulationTime());
            Trace.out(Trace.Level.INFO, "Total Arrived Customers: " + savedStats.getTotalArrivedCustomers());
            Trace.out(Trace.Level.INFO, "Total Serviced Customers: " + savedStats.getTotalServicedCustomers());
            Trace.out(Trace.Level.INFO, "Busy Time: " + savedStats.getBusyTime());
            Trace.out(Trace.Level.INFO, "System Throughput: " + savedStats.getSystemThroughput());
            Trace.out(Trace.Level.INFO, "Average Response Time: " + savedStats.getAverageResponseTime());
		} else {
            Trace.out(Trace.Level.ERR, "Database save returned null - save may have failed!");
		}		} catch (Exception e) {
            Trace.out(Trace.Level.ERR, "Error saving simulation data to database: " + e.getMessage());
            // Log stack trace at warning level for debugging when enabled
            java.io.StringWriter sw = new java.io.StringWriter();
            e.printStackTrace(new java.io.PrintWriter(sw));
            Trace.out(Trace.Level.WAR, sw.toString());
		}
	}
	
	/**
	 * Helper method to handle service point departures
	 */
	private void handleServicePointDeparture(int servicePointIndex, EventType eventType) {

        ServicePoint currentServicePoint = servicePoints[servicePointIndex];

        Customer customer;
        ServicePoint nextServicePoint;

        if (eventType == EventType.DepartureFromReception || eventType == EventType.DepartureFromKitchen || eventType == EventType.DepartureFromCounterToDelivery) {
            // Normal departure
            customer = currentServicePoint.handleDeparture();
            nextServicePoint = currentServicePoint.getNextServicePoint(servicePoints, servicePointIndex);

            // Track customer flow
        } else {
            // Special departure (payment failed, return money, delivery events, etc.)
            customer = currentServicePoint.handleSpecialDeparture(eventType);
            nextServicePoint = currentServicePoint.getNextServicePointForSpecialDeparture(eventType, servicePoints);

        }

        // If there's ma next service point and customer, add customer to next queue
        if (nextServicePoint != null && customer != null) {
            nextServicePoint.addQueue(customer);
        }
    }
}

