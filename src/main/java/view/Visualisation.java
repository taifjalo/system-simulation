package view;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Visualization component for the simulation, implemented as a JavaFX Canvas.
 * Draws customers and manages the display for simulation events.
 */
public class Visualisation extends Canvas implements IVisualisation {
    /** Graphics context for drawing on the canvas. */
    private GraphicsContext gc;
    /** X-coordinate for drawing the next customer. */
    double i = 0;
    /** Y-coordinate for drawing the next customer. */
    double j = 10;

    /**
     * Constructs a Visualisation canvas with the specified width and height.
     * @param w the width of the canvas
     * @param h the height of the canvas
     */
    public Visualisation(int w, int h) {
        super(w, h);
        gc = this.getGraphicsContext2D();
        clearDisplay();
    }

    /**
     * Clears the display by filling the canvas with yellow color.
     */
    public void clearDisplay() {
        gc.setFill(Color.YELLOW);
        gc.fillRect(0, 0, this.getWidth(), this.getHeight());
    }

    /**
     * Draws a new customer as a red oval on the canvas and updates the drawing position.
     */
    public void newCustomer() {
        gc.setFill(Color.RED);
        gc.fillOval(i,j,10,10);

        i += 10;
        if (i > this.getWidth()-1) {
            i = 0;
            j += 10;
        }
    }
}