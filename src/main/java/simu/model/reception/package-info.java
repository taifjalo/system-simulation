package simu.model.reception;

/**
 * This package contains the reception service components following the same pattern
 * as the kitchen and delivery service structures.
 * 
 * Components:
 * 
 * 1. ReceptionService - Main service point that extends ServicePoint
 *    - Handles customer checkouts, payment processing, and money returns
 *    - Orchestrates the reception process 
 *    - Uses composition with PaymentProcessor and ReceptionStatistics
 * 
 * 2. PaymentProcessor - Payment processing logic
 *    - Uses Bernoulli distribution for payment failure simulation (10% failure rate)
 *    - Handles decision logic for faulty orders (money returns)
 *    - Encapsulates all payment-related business logic
 * 
 * 3. ReceptionOutcome - Enum for reception results
 *    - NORMAL_CHECKOUT: Successful payment and checkout
 *    - PAYMENT_FAILED: Payment failed, customer needs to retry
 *    - MONEY_RETURNED: Money returned for faulty orders
 * 
 * 4. ReceptionStatistics - Statistics tracking
 *    - Tracks normal checkouts, payment failures, and money returns
 *    - Calculates payment success rates and totals
 *    - Provides centralized statistics management including total reception returns
 * 
 * This structure follows the Single Responsibility Principle and makes the code
 * more maintainable and testable by separating concerns into focused components.
 * Each component has a clear, single responsibility that can be easily tested
 * and modified independently.
 */