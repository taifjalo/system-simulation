package controller.simulation;

import controller.Controller;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import simu.model.MyEngine;

import java.util.function.Consumer;

/**
 * Manages simulation engine lifecycle and state.
 * <p>
 * Separates simulation logic from UI controller.
 * Handles starting, pausing, resuming, stopping, and speed control for the simulation engine.
 * Uses reflection to interact with simulation statistics and clock.
 */
public class SimulationManager {
    /** The simulation engine instance. */
    private MyEngine simulationEngine;
    /** Reference to the main controller. */
    private Controller mainController;
    /** Whether the simulation is currently running. */
    private boolean simulationRunning = false;
    /** Whether the simulation is currently paused. */
    private boolean simulationPaused = false;
    /** The last valid delay (ms) for simulation visualization. */
    private long lastValidDelay = 100L;
    /** Static reference to the current simulation engine (singleton). */
    private static MyEngine currentSimulationEngine = null;
    /** Logger for console output. */
    private Consumer<String> consoleLogger;
    /** Callback to run when simulation completes. */
    private Runnable onSimulationComplete;

    /**
     * Constructs a SimulationManager with a console logger.
     * @param consoleLogger Function to log messages to console
     */
    public SimulationManager(Consumer<String> consoleLogger) {
        this.consoleLogger = consoleLogger;
    }

    /**
     * Sets the main controller for simulation.
     * @param controller the main controller
     */
    public void setMainController(Controller controller) {
        this.mainController = controller;
    }

    /**
     * Sets the callback to be run when simulation completes.
     * @param callback the callback to run
     */
    public void setOnSimulationComplete(Runnable callback) {
        this.onSimulationComplete = callback;
    }

    /**
     * Starts the simulation. Initializes engine, resets state, and schedules start.
     */
    public void startSimulation() {
        if (mainController == null) {
            log("❌ No simulation configuration available");
            log("💡 Legacy animations will run without simulation");
            return;
        }
        log("🔄 Starting real-time pizzeria simulation...");
        log("👥 Customer arrivals will use original configured intervals");
        log("⏰ Only visualization delay will be adjusted for speed control");
        // Stop any existing simulation
        stopExistingSimulation();
        // Reset statistics and clock
        resetSimulationState();
        // Create new simulation engine
        simulationEngine = new MyEngine(mainController);
        currentSimulationEngine = simulationEngine;
        // Configure simulation time
        configureSimulationTime();
        // Set initial delay
        setSimulationDelay(100L);
        // Start simulation after delay
        scheduleSimulationStart();
    }

    /**
     * Sets the simulation delay for visualization speed.
     * @param delay the delay in milliseconds
     */
    public void setSimulationDelay(long delay) {
        lastValidDelay = Math.max(25L, delay);
        try {
            if (simulationEngine != null) {
                simulationEngine.setDelay(lastValidDelay);
                log("⏰ Simulation delay set to " + lastValidDelay + "ms for real-time visualization");
            }
        } catch (Exception e) {
            log("⚠️ Simulation will run at default speed");
        }
    }

    /**
     * Stop any existing simulation from previous instances
     */
    private void stopExistingSimulation() {
        if (currentSimulationEngine != null) {
            try {
                log("🛑 Stopping previous simulation...");
                if (currentSimulationEngine.isAlive()) {
                    currentSimulationEngine.interrupt();
                    currentSimulationEngine.join(2000);
                    if (currentSimulationEngine.isAlive()) {
                        log("⚠️ Previous simulation thread did not stop gracefully");
                    } else {
                        log("✅ Previous simulation stopped successfully");
                    }
                }
                currentSimulationEngine = null;
            } catch (Exception e) {
                log("⚠️ Error stopping previous simulation: " + e.getMessage());
                currentSimulationEngine = null;
            }
        }
    }

    /**
     * Reset simulation statistics and clock
     */
    private void resetSimulationState() {
        try {
            Class<?> simStatsClass = Class.forName("simu.framework.statistics.SimulationStatistics");
            java.lang.reflect.Method resetMethod = simStatsClass.getMethod("resetAllStatistics");
            resetMethod.invoke(null);
            Class<?> clockClass = Class.forName("simu.framework.Clock");
            java.lang.reflect.Method getInstanceMethod = clockClass.getMethod("getInstance");
            Object clockInstance = getInstanceMethod.invoke(null);
            java.lang.reflect.Method setTimeMethod = clockClass.getMethod("setTime", double.class);
            setTimeMethod.invoke(clockInstance, 0.0);
            log("🔄 All statistics and clock reset for new simulation");
        } catch (Exception e) {
            log("⚠️ Failed to reset statistics: " + e.getMessage());
        }
    }

    /**
     * Configure simulation time from controller settings using reflection
     */
    private void configureSimulationTime() {
        try {
            java.lang.reflect.Method getStatsMethod = mainController.getClass().getMethod("getSimulationStatistics");
            Object simStats = getStatsMethod.invoke(mainController);
            java.lang.reflect.Method getTimeMethod = simStats.getClass().getMethod("getSimulationTime");
            double configuredTime = (double) getTimeMethod.invoke(simStats);
            log("📊 Retrieved simulation time from config: " + configuredTime);
            if (configuredTime <= 0) {
                configuredTime = 1000.0;
                log("⚠️ Invalid simulation time, using default: " + configuredTime);
            }
            simulationEngine.getClass().getMethod("setSimulationTime", double.class)
                    .invoke(simulationEngine, configuredTime);
            log("📊 Simulation time set to " + configuredTime + " time units (from configuration)");
        } catch (Exception e) {
            log("⚠️ Using default simulation time: " + e.getMessage());
        }
    }

    /**
     * Start the simulation after delay
     */
    private void scheduleSimulationStart() {
        log("⏳ Starting simulation in 2 seconds...");
        Timeline startDelay = new Timeline(new KeyFrame(Duration.seconds(2), e -> {
            log("🚀 Simulation starting now!");
            try {
                simulationRunning = true;
                simulationEngine.start();
                log("✅ Simulation engine thread started directly");
                // Monitor completion
                monitorSimulationCompletion();
            } catch (Exception e2) {
                simulationRunning = false;
                log("❌ Could not start simulation: " + e2.getMessage());
                log("💡 Legacy animations will continue running");
            }
        }));
        startDelay.play();
    }

    /**
     * Monitor simulation completion in background thread
     */
    private void monitorSimulationCompletion() {
        Thread monitor = new Thread(() -> {
            try {
                simulationEngine.join();
                javafx.application.Platform.runLater(() -> {
                    simulationRunning = false;
                    log("🏁 Simulation completed successfully!");
                    if (onSimulationComplete != null) {
                        onSimulationComplete.run();
                    }
                });
            } catch (InterruptedException e) {
                javafx.application.Platform.runLater(() -> {
                    simulationRunning = false;
                    log("🛑 Simulation was interrupted");
                });
            }
        });
        monitor.setDaemon(true);
        monitor.start();
    }

    /**
     * Pause simulation using reflection (if method exists)
     */
    public void pauseSimulation() {
        if (simulationEngine != null && simulationRunning && !simulationPaused) {
            try {
                // Try to call pauseSimulation via reflection
                java.lang.reflect.Method pauseMethod = simulationEngine.getClass().getMethod("pauseSimulation");
                pauseMethod.invoke(simulationEngine);
                simulationPaused = true;
                log("⏸ Simulation paused");
            } catch (NoSuchMethodException e) {
                log("⚠️ Pause not supported by simulation engine");
            } catch (Exception e) {
                log("⚠️ Failed to pause simulation: " + e.getMessage());
            }
        }
    }

    /**
     * Resume simulation using reflection (if method exists)
     */
    public void resumeSimulation() {
        if (simulationEngine != null && simulationPaused) {
            try {
                // Try to call resumeSimulation via reflection
                java.lang.reflect.Method resumeMethod = simulationEngine.getClass().getMethod("resumeSimulation");
                resumeMethod.invoke(simulationEngine);
                simulationPaused = false;
                log("▶ Simulation resumed");
            } catch (NoSuchMethodException e) {
                log("⚠️ Resume not supported by simulation engine");
                simulationPaused = false; // Reset state
            } catch (Exception e) {
                log("⚠️ Failed to resume simulation: " + e.getMessage());
            }
        }
    }

    /**
     * Stop simulation completely
     */
    public void stopSimulation() {
        if (simulationRunning && simulationEngine != null) {
            try {
                log("🛑 Stopping simulation...");
                simulationEngine.interrupt();
                simulationRunning = false;
                simulationPaused = false;
                if (currentSimulationEngine == simulationEngine) {
                    currentSimulationEngine = null;
                }
                log("✅ Simulation stopped");
            } catch (Exception e) {
                log("⚠️ Failed to stop simulation: " + e.getMessage());
            }
        }
    }

    /**
     * Adjust simulation speed
     */
    public void adjustSpeed(double speedMultiplier) {
        long newDelay = (long) (100 / speedMultiplier);
        lastValidDelay = Math.max(25L, newDelay);
        if (simulationEngine != null && !simulationPaused) {
            try {
                simulationEngine.setDelay(lastValidDelay);
            } catch (Exception e) {
                // Speed control not supported
            }
        }
    }

    // ===== GETTERS =====
    public boolean isSimulationRunning() {
        return simulationRunning;
    }

    public boolean isSimulationPaused() {
        return simulationPaused;
    }

    public long getLastValidDelay() {
        return lastValidDelay;
    }

    public MyEngine getSimulationEngine() {
        return simulationEngine;
    }

    // ===== HELPER METHOD =====
    private void log(String message) {
        if (consoleLogger != null) {
            consoleLogger.accept(message);
        }
    }
}