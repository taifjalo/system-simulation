import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import simu.framework.EventList;
import simu.model.Customer;
import simu.model.EventType;
import simu.model.delivery.DeliveryService;
import eduni.distributions.Normal;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the DeliveryService class and its handling of delivery events.
 * Tests cover successful deliveries, refused deliveries, and remake order scenarios.
 */
public class DeliveryTest {
    /** Event list used for scheduling and verifying events in tests. */
    private EventList eventList;
    /** DeliveryService instance under test. */
    private DeliveryService deliveryService;

    /**
     * Sets up the test environment before each test.
     * Initializes the event list and delivery service.
     */
    @BeforeEach
    void setUp() {
        eventList = new EventList();
        deliveryService = new DeliveryService(
                new Normal(5, 1),
                eventList,
                EventType.DepartureFromDelivery
        );
    }

    /**
     * Tests that a successful delivery is processed and the customer's removal time is set.
     */
    @Test
    void testSuccessfulDelivery() {
        Customer customer = new Customer(false); // not walk-in
        deliveryService.addQueue(customer);
        deliveryService.beginService();
        assertFalse(eventList.getEventList().isEmpty());
        // Simulate customer departure
        deliveryService.handleSpecialDeparture(EventType.DepartureFromDelivery);
        assertNotNull(customer.getRemovalTime());
    }

    /**
     * Tests that a refused delivery is processed and the customer's removal time is set.
     */
    @Test
    void testRefusedDelivery() {
        Customer customer = new Customer(false);
        customer.setIsFaulty(true); // order is faulty
        deliveryService.addQueue(customer);
        deliveryService.beginService();
        assertFalse(eventList.getEventList().isEmpty());
        deliveryService.handleSpecialDeparture(EventType.DeliveryRefused);
        assertNotNull(customer.getRemovalTime());
    }

    /**
     * Tests that a remake order is processed and the returned customer is not marked as faulty.
     */
    @Test
    void testRemakeOrder() {
        Customer customer = new Customer(false);
        customer.setIsFaulty(true);
        deliveryService.addQueue(customer);
        deliveryService.beginService();
        assertFalse(eventList.getEventList().isEmpty());
        Customer returned = deliveryService.handleSpecialDeparture(EventType.RemakeOrder);
        assertNotNull(returned);
        assertFalse(returned.getIsFaulty());
    }
}
