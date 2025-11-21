package simu.model.kitchen;
import eduni.distributions.Bernoulli;
import eduni.distributions.ContinuousGenerator;

/**
 * Represents a cook in the kitchen simulation.
 * Each cook has a competency level, can prepare meals, and tracks their busy state and order completion.
 * Handles meal preparation logic, including success/failure based on competency and probability.
 */
public class Cook  {
    /** The competency level of the cook (e.g., EXPERT, INEXPERT). */
    private CookCompetency competency;
    /** Whether the cook is currently busy. */
    private Boolean isBusy = false;
    /** Whether the last order prepared by this cook failed. */
    private Boolean orderFailed = false;
    /** Generator for service times (meal preparation times). */
    private ContinuousGenerator generator;
    /** The time when the current order will be finished. */
    private double orderFinishTime;

    /** Bernoulli distribution for determining order failure (default 85% success rate). */
    Bernoulli bernoulli = new Bernoulli(0.85); // 85% success rate = 15% fail rate

    /**
     * Constructs a Cook with the given competency and service time generator.
     * @param competency the competency level of the cook
     * @param generator the generator for meal preparation times
     */
    public Cook(CookCompetency competency, ContinuousGenerator generator) {
        this.competency = competency;
        this.generator = generator;
    }

    /**
     * Prepares a meal, returning an Order object with the appropriate service time.
     * For expert cooks, the meal is prepared faster. For inexpert cooks, the meal takes longer and may fail.
     * @return the prepared Order object (may be marked as failed)
     */
    public Order prepareMeal() {//muutin sen, jotta se toimii oikein kokkien kanssa, kokin varaus toimii oikein
        orderFailed = false;

        double serviceTime = generator.sample();

        if (competency == CookCompetency.EXPERT) {

            double fastServiceTime = serviceTime * 0.7;

            Order expertOrder = new Order(fastServiceTime);

            return expertOrder;
        } else {

            double slowServiceTime = serviceTime * 1.3;

            Order inexpOrder = new Order(slowServiceTime);

            orderFailed = bernoulli.sample() == 0;
            inexpOrder.setFailed(orderFailed);

            return inexpOrder;
        }
    }

    /**
     * Checks if the cook is currently busy.
     * @return true if the cook is busy, false otherwise
     */
    public Boolean isBusy() {
        return isBusy;
    }

    /**
     * Sets the busy state of the cook.
     * @param busy true if the cook is busy, false otherwise
     */
    public void setBusy(boolean busy) {
        isBusy = busy;
    }

    /**
     * Gets the time when the current order will be finished.
     * @return the order finish time
     */
    public double getOrderFinishTime() {
        return orderFinishTime;
    }

    /**
     * Sets the time when the current order will be finished.
     * @param orderFinishTime the order finish time
     */
    public void setOrderFinishTime(double orderFinishTime) {
        this.orderFinishTime = orderFinishTime;
    }

    /**
     * Gets the competency level of the cook as a string.
     * @return the competency level as a string
     */
    public String getCompetency() {
        return competency.toString();
    }
}
