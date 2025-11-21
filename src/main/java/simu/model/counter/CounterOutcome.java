package simu.model.counter;

/**
 * Enum representing the possible outcomes for counter service interactions.
 *
 * @author (your name)
 */
public enum CounterOutcome {
    /** Call-in customer checkout for delivery. */
    DELIVERY_CHECKOUT,
    /** Walk-in customer normal checkout (complete). */
    WALK_IN_CHECKOUT,
    /** Faulty order sent back to kitchen for remake. */
    FAULTY_TO_KITCHEN,
    /** Faulty order sent to reception for refund. */
    FAULTY_TO_RECEPTION
}