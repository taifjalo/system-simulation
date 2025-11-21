package view.overview;

/**
 * Simple data class for TableView rows in the simulation overview.
 * <p>
 * Uses plain Java getters/setters instead of JavaFX properties to avoid compatibility issues.
 * Holds metric names and values for up to four simulation runs.
 */
public class SimulationOverview {

    /** The name of the metric (e.g., "Total arrived customers"). */
    private String metric;
    /** Value for the most recent run. */
    private String run1;
    /** Value for the second most recent run. */
    private String run2;
    /** Value for the third most recent run. */
    private String run3;
    /** Value for the fourth most recent run. */
    private String run4;
    

    /**
     * Default constructor.
     */
    public SimulationOverview() {
        // Default constructor
    }
    

    /**
     * Constructs a SimulationOverview row with all values.
     * @param metric the metric name
     * @param run1 value for the most recent run
     * @param run2 value for the second most recent run
     * @param run3 value for the third most recent run
     * @param run4 value for the fourth most recent run
     */
    public SimulationOverview(String metric, String run1, String run2, String run3, String run4) {
        this.metric = metric;
        this.run1 = run1;
        this.run2 = run2;
        this.run3 = run3;
        this.run4 = run4;
    }
    

    /**
     * Gets the metric name.
     * @return the metric name
     */
    public String getMetric() { return metric; }

    /**
     * Sets the metric name.
     * @param metric the metric name
     */
    public void setMetric(String metric) { this.metric = metric; }

    /**
     * Gets the value for the most recent run.
     * @return the value for run 1
     */
    public String getRun1() { return run1; }

    /**
     * Sets the value for the most recent run.
     * @param run1 the value for run 1
     */
    public void setRun1(String run1) { this.run1 = run1; }

    /**
     * Gets the value for the second most recent run.
     * @return the value for run 2
     */
    public String getRun2() { return run2; }

    /**
     * Sets the value for the second most recent run.
     * @param run2 the value for run 2
     */
    public void setRun2(String run2) { this.run2 = run2; }

    /**
     * Gets the value for the third most recent run.
     * @return the value for run 3
     */
    public String getRun3() { return run3; }

    /**
     * Sets the value for the third most recent run.
     * @param run3 the value for run 3
     */
    public void setRun3(String run3) { this.run3 = run3; }

    /**
     * Gets the value for the fourth most recent run.
     * @return the value for run 4
     */
    public String getRun4() { return run4; }

    /**
     * Sets the value for the fourth most recent run.
     * @param run4 the value for run 4
     */
    public void setRun4(String run4) { this.run4 = run4; }
}