package simu.model.reception;

import eduni.distributions.Bernoulli;
import simu.model.Customer;

/**
 * Handles payment processing logic for reception service.
 * Uses a Bernoulli distribution to simulate payment failures (10% failure rate).
 */
public class PaymentProcessor {
    
    // Probability distribution for payment failures (10% failure rate)
    private final Bernoulli paymentFailureChance;
    
    public PaymentProcessor() {
        // 90% success rate = 10% failure rate
        this.paymentFailureChance = new Bernoulli(0.9);
    }
    
    public PaymentProcessor(double successRate) {
        this.paymentFailureChance = new Bernoulli(successRate);
    }
    
    /**
     * Processes payment for a customer and determines the outcome.
     * @param customer The customer attempting payment
     * @return ReceptionOutcome based on customer status and payment result
     */
    public ReceptionOutcome processPayment(Customer customer) {
        // If customer has a faulty order, return money
        if (customer.getIsFaulty()) {
            return ReceptionOutcome.MONEY_RETURNED;
        }
        
        // Check if payment fails (Bernoulli returns 0 for failure)
        if (paymentFailureChance.sample() == 0) {
            return ReceptionOutcome.PAYMENT_FAILED;
        }
        
        // Normal successful checkout
        return ReceptionOutcome.NORMAL_CHECKOUT;
    }
    

}