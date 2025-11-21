package simu.model.counter;

import eduni.distributions.Bernoulli;
import simu.model.Customer;

/**
 * Handles decision making for counter customers.
 * For faulty orders, uses a Bernoulli distribution to determine whether to send
 * customers to kitchen (remake) or reception (refund) - 50% each by default.
 *
 * @author (your name)
 */
public class CounterDecision {
    /**
     * Bernoulli distribution for faulty order resolution (probability of kitchen/remake).
     */
    private final Bernoulli fixProblemPath;
    
    /**
     * Constructs a CounterDecision with a 50% probability for kitchen/remake.
     */
    public CounterDecision() {
        this.fixProblemPath = new Bernoulli(0.5);
    }
    
    /**
     * Constructs a CounterDecision with a custom probability for kitchen/remake.
     * @param kitchenProbability probability that a faulty order is sent to the kitchen
     */
    public CounterDecision(double kitchenProbability) {
        this.fixProblemPath = new Bernoulli(kitchenProbability);
    }
    
    /**
     * Determines the appropriate outcome for a customer at the counter.
     * @param customer The customer being served
     * @return CounterOutcome based on customer type and order status
     */
    public CounterOutcome determineOutcome(Customer customer) {
        // Call-in customers go to delivery
        if (!customer.isWalkIn()) {
            return CounterOutcome.DELIVERY_CHECKOUT;
        }
        
        // Walk-in customers with faulty orders
        if (customer.getIsFaulty()) {
            // Use Bernoulli distribution: 1 = kitchen (remake), 0 = reception (refund)
            return fixProblemPath.sample() == 1 ? 
                CounterOutcome.FAULTY_TO_KITCHEN : 
                CounterOutcome.FAULTY_TO_RECEPTION;
        }
        
        // Normal walk-in customer checkout
        return CounterOutcome.WALK_IN_CHECKOUT;
    }
}