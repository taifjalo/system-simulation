import eduni.distributions.Normal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import simu.framework.EventList;
import simu.model.EventType;
import simu.model.Customer;
import simu.model.kitchen.CookCompetency;
import simu.model.kitchen.KitchenServicePoint;
import eduni.distributions.ContinuousGenerator;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the KitchenServicePoint class and its service logic.
 * Tests cover customer handling and cook assignment in the kitchen.
 */
public class KitchenTest {
    /** KitchenServicePoint instance under test. */
    private KitchenServicePoint kitchenServicePoint;
    /** Event list used for scheduling and verifying events in tests. */
    private EventList eventList;

    /**
     * Sets up the test environment before each test.
     * Initializes the event list and kitchen service point.
     */
    @BeforeEach
    void setUp() {
        eventList = new EventList();

        kitchenServicePoint = new KitchenServicePoint(
                new Normal(10.0,2.0),
                eventList,
                EventType.DepartureFromKitchen,
                new ArrayList<>(List.of(CookCompetency.EXPERT))
        );
    }

    /**
     * Tests that a customer is correctly handled by the kitchen service point and assigned to a cook.
     */
    @Test
    void testBeginServiceWithOneCustomer() {
        Customer customer = new Customer(true);

        kitchenServicePoint.addQueue(customer);

        kitchenServicePoint.beginService();

        assertTrue(customer.getOnKitchen());

        assertTrue(kitchenServicePoint.getCooks().get(0).isBusy());

        assertFalse(eventList.getEventList().isEmpty());
    }
}


