package controller.simulation;

import javafx.application.Platform;
import java.util.function.Consumer;

/**
 * Handles simulation event logging and callbacks.
 * <p>
 * Separates event handling logic from main controller. Logs events to a provided console logger.
 */
public class SimulationEventLogger {
    /** Logger for console output. */
    private final Consumer<String> consoleLogger;

    /**
     * Constructs a SimulationEventLogger with a console logger.
     * @param consoleLogger Function to log messages to console
     */
    public SimulationEventLogger(Consumer<String> consoleLogger) {
        this.consoleLogger = consoleLogger;
    }

    /**
     * Log customer arrival event.
     * @param isWalkIn true if customer is walk-in, false if call-in
     */
    public void onCustomerArrival(boolean isWalkIn) {
        String customerType = isWalkIn ? "🚶 Walk-in" : "📞 Call-in";
        Platform.runLater(() -> {
            log("🆕 " + customerType + " customer arrived at Reception");
        });
    }

    /**
     * Log service begin event.
     * @param servicePointName the name of the service point
     * @param customerId the ID of the customer
     */
    public void onServiceBegin(String servicePointName, String customerId) {
        Platform.runLater(() -> {
            log("🔄 Customer " + customerId + " began service at " + servicePointName);
        });
    }

    /**
     * Log customer departure event.
     * @param servicePointName the name of the service point
     * @param customerId the ID of the customer
     * @param nextDestination the next destination of the customer
     */
    public void onCustomerDeparture(String servicePointName, String customerId, String nextDestination) {
        Platform.runLater(() -> {
            log("➡️ Customer " + customerId + " departed " + servicePointName + " → " + nextDestination);
        });
    }

    /**
     * Log special departure event.
     * @param servicePointName the name of the service point
     * @param customerId the ID of the customer
     * @param eventType the type of special event
     */
    public void onSpecialDeparture(String servicePointName, String customerId, String eventType) {
        Platform.runLater(() -> {
            log("⚠️ Customer " + customerId + " special event at " + servicePointName + " (" + eventType + ")");
        });
    }

    /**
     * Log customer served successfully.
     * @param customerId the ID of the customer
     */
    public void onCustomerServed(String customerId) {
        Platform.runLater(() -> {
            log("✅ Customer " + customerId + " successfully served and left happy!");
        });
    }

    /**
     * Log customer not served.
     * @param customerId the ID of the customer
     * @param reason the reason the customer was not served
     */
    public void onCustomerNotServed(String customerId, String reason) {
        Platform.runLater(() -> {
            log("❌ Customer " + customerId + " left unsatisfied (" + reason + ")");
        });
    }

    /**
     * Helper method to log messages.
     * @param message the message to log
     */
    private void log(String message) {
        if (consoleLogger != null) {
            consoleLogger.accept(message);
        }
    }
}