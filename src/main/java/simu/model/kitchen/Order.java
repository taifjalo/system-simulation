package simu.model.kitchen;

/**
 * Represents an order prepared by a cook in the kitchen simulation.
 * Tracks whether the order failed and the time required for preparation.
 */
public class Order {
    /** Indicates if the order preparation failed. */
    private boolean failed = false;
    /** The time required to prepare the order. */
    private double preparationTime;

    /**
     * Constructs an Order with the specified preparation time.
     * @param preparationTime the time required to prepare the order
     */
    public Order(double preparationTime) {
        this.failed = false;
        this.preparationTime = preparationTime;
    }

    /**
     * Sets whether the order preparation failed.
     * @param failed true if the order failed, false otherwise
     */
    public void setFailed(boolean failed) {
        this.failed = failed;
    }

    /**
     * Gets the time required to prepare the order.
     * @return the preparation time
     */
    public double getPreparationTime() {
        return preparationTime;
    }

    /**
     * Checks if the order preparation failed.
     * @return true if the order failed, false otherwise
     */
    public boolean isFailed() {
        return failed;
    }
}
