package simu.model.counter;

/**
 * This package contains the counter service components following the same pattern
 * as the kitchen, delivery, and reception service structures.
 * 
 * Components:
 * 
 * 1. CounterService - Main service point that extends ServicePoint
 *    - Handles customer checkouts and routing of faulty orders
 *    - Manages both call-in customers (delivery) and walk-in customers
 *    - Uses composition with CounterDecision and CounterStatistics
 * 
 * 2. CounterDecision - Decision-making and routing logic
 *    - Uses Bernoulli distribution for faulty order resolution (50% kitchen vs 50% reception)
 *    - Determines appropriate routing based on customer type and order status
 *    - Encapsulates all decision-making business logic
 * 
 * 3. CounterOutcome - Enum for counter results
 *    - DELIVERY_CHECKOUT: Call-in customer checkout for delivery
 *    - WALK_IN_CHECKOUT: Walk-in customer normal checkout (complete)
 *    - FAULTY_TO_KITCHEN: Faulty order sent back to kitchen for remake
 *    - FAULTY_TO_RECEPTION: Faulty order sent to reception for refund
 * 
 * 4. CounterStatistics - Statistics tracking
 *    - Tracks delivery checkouts, walk-in checkouts, and faulty order resolutions
 *    - Calculates success rates and kitchen resolution rates
 *    - Provides comprehensive statistics for counter operations
 * 
 * Counter Service Workflow:
 * - Call-in customers → Delivery checkout
 * - Walk-in customers with good orders → Normal checkout (complete)
 * - Walk-in customers with faulty orders → 50% to kitchen (remake), 50% to reception (refund)
 * 
 * This structure follows the Single Responsibility Principle and makes the code
 * more maintainable and testable by separating concerns into focused components.
 * Each component has a clear, single responsibility that can be easily tested
 * and modified independently.
 */