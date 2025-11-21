package simu.model.reception;

/**
 * Enum representing the possible outcomes for reception service interactions.
 */
public enum ReceptionOutcome {
    NORMAL_CHECKOUT,        // Successful payment and checkout
    PAYMENT_FAILED,         // Payment failed, customer needs to retry
    MONEY_RETURNED          // Money returned for faulty orders
}