package view;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/**
 * Visualization component for the simulation, implemented as a JavaFX Canvas.
 * Displays the number of customers visually as text on the canvas.
 */
public class Visualisation2 extends Canvas implements IVisualisation {
    /** Graphics context for drawing on the canvas. */
    private GraphicsContext gc;
    /** Counter for the number of customers displayed. */
    int customerCount = 0;

    /**
     * Constructs a Visualisation2 canvas with the specified width and height.
     * @param w the width of the canvas
     * @param h the height of the canvas
     */
    public Visualisation2(int w, int h) {
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
     * Increments the customer count and displays the updated count as text on the canvas.
     * Erases the old text before writing the new value.
     */
    public void newCustomer() {
        customerCount++;
        
        gc.setFill(Color.YELLOW);					// first erase old text
        gc.fillRect(100,80, 130, 20);
        gc.setFill(Color.RED);						// then write new text
        gc.setFont(new Font(20));
        gc.fillText("Customer " + customerCount, 100, 100);
    }
}