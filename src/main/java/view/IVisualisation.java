package view;

/**
 * Interface for visualization components in the simulation.
 * Provides methods for clearing the display and handling new customer events.
 */
public interface IVisualisation {
    /**
     * Clears the visualization display.
     */
    public void clearDisplay();

    /**
     * Handles the event of a new customer in the visualization.
     */
    public void newCustomer();
}