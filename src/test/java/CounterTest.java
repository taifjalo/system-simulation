import eduni.distributions.Normal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import simu.framework.Clock;
import simu.framework.EventList;
import simu.framework.IEventType;
import simu.model.Customer;
import simu.model.EventType;
import simu.model.ServicePoint;
import simu.model.counter.CounterService;
import simu.model.kitchen.CookCompetency;
import simu.model.kitchen.KitchenServicePoint;
import simu.model.reception.ReceptionService;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the CounterService class and related customer routing logic.
 * Tests cover walk-in and delivery customers, error handling, departures, and routing.
 */
public class CounterTest {
    /** Event list used for scheduling and verifying events in tests. */
    private EventList eventList;
    /** CounterService instance under test. */
    private CounterService counterService;

    /**
     * Sets up the test environment before each test.
     * Initializes the event list and counter service.
     */
    @BeforeEach
    void setUp() {
        Clock.getInstance().setTime(0);
        eventList = new EventList();
        counterService = new CounterService(
                new Normal(5.0, 1.0),
                eventList,
                EventType.DepartureFromCounterToCostumer
        );
    }

    /**
     * Tests that a walk-in customer is processed without error and the correct event is scheduled.
     */
    @Test
    void testWalkInCustomer_NoError() {
        Customer customer = new Customer(true); // walk-in
        customer.setIsFaulty(false);
        counterService.addQueue(customer);

        counterService.beginService();

        assertFalse(eventList.getEventList().isEmpty());
        assertEquals(EventType.DepartureFromCounterToCostumer,
                eventList.getEventList().poll().getType());
    }

    /**
     * Tests that a delivery customer is routed to the correct event type.
     */
    @Test
    void testDeliveryCustomer() {
        Customer customer = new Customer(false); // not walk-in
        counterService.addQueue(customer);

        counterService.beginService();

        assertEquals(EventType.DepartureFromCounterToDelivery,
                eventList.getEventList().poll().getType());
    }

    /**
     * Tests that a faulty customer is routed to either the kitchen or reception for error handling.
     */
    @Test
    void testFaultyCustomerToKitchen() {
        Customer customer = new Customer(true);
        customer.setIsFaulty(true);
        counterService.addQueue(customer);


        counterService.beginService();


        IEventType type = eventList.getEventList().poll().getType();
        assertTrue(type == EventType.CounterErrorToKitchen || type == EventType.CounterErrorToReception);
    }

    /**
     * Tests that handleDeparture removes the customer from the queue and returns the departed customer.
     */
    @Test
    void testHandleDeparture() {
        Customer customer = new Customer(true);
        counterService.addQueue(customer);

        counterService.beginService();
        Customer departed = counterService.handleDeparture();

        assertNotNull(departed);
        assertTrue(counterService.getQueue().isEmpty());
    }

    /**
     * Tests that handleSpecialDeparture returns the customer when routed to the kitchen.
     */
    @Test
    void testHandleSpecialDepartureToKitchen() {
        Customer customer = new Customer(true);
        customer.setIsFaulty(true);
        counterService.addQueue(customer);

        Customer returned = counterService.handleSpecialDeparture(EventType.CounterErrorToKitchen);

        assertNotNull(returned);
    }

    /**
     * Tests routing logic for special departures to kitchen, reception, or null (customer leaves system).
     */
    @Test
    void testRouting() {
        KitchenServicePoint kitchen = new KitchenServicePoint(new Normal(5,1), eventList, EventType.DepartureFromKitchen,new ArrayList<>(List.of(CookCompetency.EXPERT,CookCompetency.EXPERT)));
        ReceptionService reception = new ReceptionService(new Normal(5,1), eventList, EventType.DepartureFromReception);

        ServicePoint[] servicePoints = new ServicePoint[2];
        servicePoints[1] = kitchen;
        servicePoints[0] = reception;

        assertEquals(kitchen,
                counterService.getNextServicePointForSpecialDeparture(EventType.CounterErrorToKitchen, servicePoints));
        assertEquals(reception,
                counterService.getNextServicePointForSpecialDeparture(EventType.CounterErrorToReception, servicePoints));
        assertNull(counterService.getNextServicePointForSpecialDeparture(EventType.DepartureFromCounterToCostumer, servicePoints));
    }
}