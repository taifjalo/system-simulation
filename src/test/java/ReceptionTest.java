import eduni.distributions.Normal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import simu.framework.Clock;
import simu.framework.EventList;
import simu.model.Customer;
import simu.model.EventType;
import simu.model.reception.ReceptionService;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the ReceptionService class and its handling of reception events.
 * Tests cover payment success, return money, payment failure, departures, and routing logic.
 */
public class ReceptionTest {
    /** Event list used for scheduling and verifying events in tests. */
    private EventList eventList;
    /** ReceptionService instance under test. */
    private ReceptionService receptionService;

    /**
     * Sets up the test environment before each test.
     * Initializes the event list and reception service.
     */
    @BeforeEach
    void setUp() {
        Clock.getInstance().setTime(0); // сброс времени
        eventList = new EventList();
        receptionService = new ReceptionService(
                new Normal(5.0, 1.0),
                eventList,
                EventType.DepartureFromReception
        );
    }

    /**
     * Tests that a successful payment results in the correct event being scheduled.
     */
    @Test
    void testBeginService_SuccessfulPayment() {
        Customer customer = new Customer(true);
        receptionService.addQueue(customer);

        receptionService.beginService();

        assertFalse(eventList.getEventList().isEmpty());
        assertEquals(EventType.DepartureFromReception, eventList.getEventList().poll().getType());
    }

    /**
     * Tests that a faulty customer triggers a return money event and is removed from the queue.
     */
    @Test
    void testBeginService_ReturnMoney() {
        Customer customer = new Customer(true);
        customer.setIsFaulty(true); // клиент с ошибкой
        receptionService.addQueue(customer);

        receptionService.beginService();

        assertEquals(EventType.ReturnMoney, eventList.getEventList().poll().getType());

        receptionService.handleSpecialDeparture(EventType.ReturnMoney);

        assertTrue(receptionService.getQueue().isEmpty());
    }

    /**
     * Tests that a payment failure returns the customer to the queue.
     */
    @Test
    void testBeginService_PaymentFailed() {
        Customer customer = new Customer(true);
        receptionService.addQueue(customer);

        receptionService.beginService();

        Customer returned = receptionService.handleSpecialDeparture(EventType.PaymentFailed);

        assertNotNull(returned);
        assertEquals(customer, returned);
    }

    /**
     * Tests that handleDeparture removes the customer from the queue and returns the departed customer.
     */
    @Test
    void testHandleDeparture() {
        Customer customer = new Customer(true);
        receptionService.addQueue(customer);

        receptionService.beginService();
        Customer departed = receptionService.handleDeparture();

        assertNotNull(departed);
        assertTrue(receptionService.getQueue().isEmpty());
    }

    /**
     * Tests routing logic for special departures (return money and payment failed).
     */
    @Test
    void testRouting() {
        assertNull(receptionService.getNextServicePointForSpecialDeparture(EventType.ReturnMoney, null));
        assertEquals(receptionService,
                receptionService.getNextServicePointForSpecialDeparture(EventType.PaymentFailed, null));
    }
}
