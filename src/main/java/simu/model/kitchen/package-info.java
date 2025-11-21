package simu.model.kitchen;

/**
 * This package contains the kitchen service components following clean architecture principles.
 * The kitchen has been optimized for better maintainability and consistency with other services.
 * 
 * Components:
 * 
 * 1. KitchenServicePoint - Main service point that extends ServicePoint
 *    - Handles meal preparation using multiple cooks with different competencies
 *    - Uses composition with CookSelector and KitchenStatistics
 *    - Manages 3 cooks: 1 expert (33% selection chance) and 2 inexperienced (67% selection chance)
 * 
 * 2. Cook - Individual cook entity with competency-based behavior
 *    - Expert cooks: 30% faster service time, never fail orders
 *    - Inexperienced cooks: 30% slower service time, 15% failure rate
 *    - Encapsulates cooking logic and timing calculations
 * 
 * 3. CookSelector - Cook selection and assignment logic
 *    - Uses uniform distribution for cook selection (33% expert, 67% inexperienced)
 *    - Returns detailed selection information including competency and random percentage
 *    - Separates selection logic from main service point
 * 
 * 4. Order - Order entity representing meal preparation result
 *    - Contains preparation time and failure status
 *    - Simple data object with clear state management
 * 
 * 5. CookCompetency - Enum for cook skill levels
 *    - EXPERT: Fast, reliable cooks
 *    - INEXPERIENCED: Slower cooks with failure possibility
 * 
 * 6. KitchenStatistics - Statistics tracking for kitchen operations
 *    - Tracks expert vs inexperienced order counts
 *    - Monitors failure rates and success percentages
 *    - Provides comprehensive kitchen performance metrics
 * 
 * Kitchen Workflow:
 * - Customer arrives → Cook selected based on availability distribution
 * - Expert cook → 70% normal time, never fails
 * - Inexperienced cook → 130% normal time, 15% failure chance
 * - Failed orders mark customer as faulty for downstream handling
 * 
 * Optimizations Made:
 * - Fixed logic bug in Cook failure handling
 * - Extracted statistics to separate component
 * - Created CookSelector for better separation of concerns
 * - Added constants for speed multipliers and success rates
 * - Improved documentation and method clarity
 * - Made fields final where appropriate for immutability
 * 
 * This structure follows the Single Responsibility Principle and makes the code
 * more maintainable, testable, and consistent with other service patterns.
 */