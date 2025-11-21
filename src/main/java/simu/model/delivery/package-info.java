package simu.model.delivery;

/**
 * This package contains the delivery service components following the same pattern
 * as the kitchen service structure.
 * 
 * Components:
 * 
 * 1. DeliveryService - Main service point that extends ServicePoint
 *    - Handles call-in customers for delivery
 *    - Orchestrates the delivery process
 *    - Uses composition with DeliveryDecision and DeliveryStatistics
 * 
 * 2. DeliveryDecision - Decision-making logic
 *    - Uses Bernoulli distribution for faulty order decisions
 *    - Determines whether customers request remakes or refuse payment
 *    - Encapsulates all probability-based decision logic
 * 
 * 3. DeliveryOutcome - Enum for delivery results
 *    - SUCCESSFUL_DELIVERY: Normal successful delivery
 *    - REMAKE_REQUESTED: Customer requests remake for faulty order  
 *    - PAYMENT_REFUSED: Customer refuses to pay for faulty order
 * 
 * 4. DeliveryStatistics - Statistics tracking
 *    - Tracks successful, remade, and declined deliveries
 *    - Calculates success rates and totals
 *    - Provides centralized statistics management
 * 
 * This structure follows the Single Responsibility Principle and makes the code
 * more maintainable and testable by separating concerns into focused components.
 */