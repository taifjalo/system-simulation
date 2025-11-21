package simu.model.delivery;

/**
 * Enum representing the possible outcomes for delivery service interactions.
 *
 * @author (your name)
 */
public enum DeliveryOutcome {
    /** Normal successful delivery. */
    SUCCESSFUL_DELIVERY,
    /** Customer requests remake for faulty order. */
    REMAKE_REQUESTED,
    /** Customer refuses to pay for faulty order. */
    PAYMENT_REFUSED
}