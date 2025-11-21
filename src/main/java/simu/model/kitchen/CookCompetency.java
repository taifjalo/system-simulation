package simu.model.kitchen;

/**
 * Enum representing the competency level of a cook in the kitchen simulation.
 * Used to determine meal preparation speed and failure probability.
 */
public enum CookCompetency {
    /**
     * Expert cook: prepares meals faster and with no failure.
     */
    EXPERT,
    /**
     * Inexperienced cook: prepares meals slower and may fail.
     */
    INEXPERIENCED
}
