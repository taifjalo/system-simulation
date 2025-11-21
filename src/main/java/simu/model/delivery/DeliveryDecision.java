package simu.model.delivery;

import eduni.distributions.Bernoulli;

/**
 * Handles decision making for delivery customers with faulty orders.
 * Uses a Bernoulli distribution to determine whether customers will
 * request a remake or refuse payment (50% each by default).
 *
 * @author (your name)
 */
public class DeliveryDecision {
    /**
     * Bernoulli distribution for remake vs decline decision (probability of remake).
     */
    private final Bernoulli remakeDecision;
    
    /**
     * Constructs a DeliveryDecision with a 50% probability for remake.
     */
    public DeliveryDecision() {
        this.remakeDecision = new Bernoulli(0.5);
    }
    
    /**
     * Constructs a DeliveryDecision with a custom probability for remake.
     * @param remakeProbability probability that a faulty delivery results in a remake
     */
    public DeliveryDecision(double remakeProbability) {
        this.remakeDecision = new Bernoulli(remakeProbability);
    }
    
    /**
     * Determines the outcome for a faulty delivery.
     * @return DeliveryOutcome.REMAKE_REQUESTED or DeliveryOutcome.PAYMENT_REFUSED
     */
    public DeliveryOutcome decideFaultyDeliveryOutcome() {
        // Bernoulli returns 1 for remake, 0 for refuse
        return remakeDecision.sample() == 1 ? 
            DeliveryOutcome.REMAKE_REQUESTED : 
            DeliveryOutcome.PAYMENT_REFUSED;
    }
    
    /**
     * Determines the outcome for a normal (non-faulty) delivery.
     * @return DeliveryOutcome.SUCCESSFUL_DELIVERY
     */
    public DeliveryOutcome decideNormalDeliveryOutcome() {
        return DeliveryOutcome.SUCCESSFUL_DELIVERY;
    }
}