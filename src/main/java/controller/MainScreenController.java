package controller;

import controller.animation.AnimationManager;
import controller.simulation.SimulationEventLogger;
import controller.simulation.SimulationManager;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

/**
 * Main controller for the workflow simulation screen.
 * <p>
 * Coordinates between {@link AnimationManager}, {@link SimulationManager}, and the UI.
 * Provides a clean separation of concerns for better maintainability.
 * Handles simulation lifecycle, animation control, and user interactions.
 */
public class MainScreenController {


    /** Pane for displaying animations. */
    @FXML
    private Pane animationPane;

    /** Play/Pause button. */
    @FXML
    private Button btnPlay;

    /** Back button to return to configuration. */
    @FXML
    private Button btnBack;

    /** Button to slow down simulation/animation. */
    @FXML
    private Button btnSlowDown;

    /** Button to speed up simulation/animation. */
    @FXML
    private Button btnSpeed;

    /** Button to open statistics dashboard. */
    @FXML
    private Button btnStats;

    /** Console area for logging simulation events. */
    @FXML
    private TextArea consoleArea;

    /** Container for the console area. */
    @FXML
    private VBox consoleBox;

    /** Control bar containing simulation controls. */
    @FXML
    private HBox controlBar;


    // Component managers
    /** Manages all animation logic. */
    private AnimationManager animationManager;
    /** Manages simulation logic and state. */
    private SimulationManager simulationManager;
    /** Logs simulation events to the console. */
    private SimulationEventLogger eventLogger;

    // State
    /** Whether the simulation/animation is currently playing. */
    private boolean isPlaying = false;
    /** Current speed multiplier for simulation/animation. */
    private double currentSpeed = 1.0;
    /** Minimum allowed speed multiplier. */
    private static final double MIN_SPEED = 0.25;
    /** Maximum allowed speed multiplier. */
    private static final double MAX_SPEED = 4.0;

    // External references
    /** Reference to the main controller. */
    private Controller mainController;
    /** Reference to the primary stage. */
    private Stage stage;
    /** Singleton instance for static access. */
    private static MainScreenController instance;


    /**
     * Initializes the controller after FXML loading.
     * Sets up UI, managers, and animations. Displays initial console messages.
     */
    @FXML
    void initialize() {
        setupUI();
        initializeManagers();
        setupAnimations();
        updateConsole("🚀 Advanced Workflow Simulation initialized");
        updateConsole("🎬 Simulation and animations will auto-start when ready");
        updateConsole("⏸️ Use Pause/Play button to control both simulation and animations");
        instance = this;
    }


    /**
     * Initializes component managers for animation, simulation, and event logging.
     */
    private void initializeManagers() {
        // Animation manager
        animationManager = new AnimationManager(animationPane);
        // Simulation manager with console logger
        simulationManager = new SimulationManager(this::updateConsole);
        simulationManager.setOnSimulationComplete(() -> {
            stopAllAnimations();
        });
        // Event logger
        eventLogger = new SimulationEventLogger(this::updateConsole);
    }


    /**
     * Sets the main controller and starts the simulation and animations.
     *
     * @param controller the main application controller
     */
    public void setMainController(Controller controller) {
        this.mainController = controller;
        simulationManager.setMainController(controller);
        // Start simulation
        simulationManager.startSimulation();
        // Auto-start animations after delay
        autoStartAnimations();
    }


    /**
     * Sets the primary stage and configures the close handler to stop simulation and animations.
     *
     * @param stage the primary stage
     */
    public void setStage(Stage stage) {
        this.stage = stage;
        stage.setOnCloseRequest(event -> {
            if (simulationManager.isSimulationRunning()) {
                updateConsole("🛑 Stopping simulation due to window close...");
                simulationManager.stopSimulation();
                stopAllAnimations();
                updateConsole("✅ Simulation stopped - window closed");
            }
        });
    }


    /**
     * Automatically starts animations after a short delay if not already playing.
     */
    private void autoStartAnimations() {
        Timeline autoStartTimeline = new Timeline(new KeyFrame(Duration.seconds(3), e -> {
            if (!isPlaying) {
                updateConsole("🎬 Auto-starting animations...");
                playAllAnimations();
                isPlaying = true;
                btnPlay.setText("⏸");
                updateConsole("▶ Simulation and animations are now running automatically!");
            }
        }));
        autoStartTimeline.play();
    }


    /**
     * Configures UI elements such as the console area and animation pane.
     */
    private void setupUI() {
        consoleArea.setWrapText(true);
        consoleArea.setEditable(false);
        if (animationPane != null) {
            animationPane.setMouseTransparent(true);
            animationPane.setStyle("-fx-background-color: transparent;");
        }
    }


    /**
     * Sets up all animations using the {@link AnimationManager}.
     */
    private void setupAnimations() {
        if (animationManager != null) {
            animationManager.setupAllAnimations();
        }
    }


    /**
     * Toggles play/pause state for both animation and simulation.
     */
    private void togglePlayPause() {
        if (isPlaying) {
            pauseAll();
        } else {
            resumeAll();
        }
        isPlaying = !isPlaying;
    }


    /**
     * Pauses both animations and simulation.
     */
    private void pauseAll() {
        pauseAllAnimations();
        if (simulationManager.isSimulationRunning() && !simulationManager.isSimulationPaused()) {
            simulationManager.pauseSimulation();
            updateConsole("⏸ BOTH simulation and animations paused");
        } else {
            updateConsole("⏸ Animations paused");
        }
        btnPlay.setText("▶");
    }


    /**
     * Resumes both animations and simulation.
     */
    private void resumeAll() {
        playAllAnimations();
        if (simulationManager.isSimulationPaused()) {
            simulationManager.resumeSimulation();
            updateConsole(String.format("▶ BOTH simulation and animations resumed • Speed: %.2fx", currentSpeed));
        } else {
            updateConsole(String.format("▶ Animations resumed • Speed: %.2fx", currentSpeed));
        }
        btnPlay.setText("⏸");
    }


    /**
     * Plays all animations using the {@link AnimationManager}.
     */
    private void playAllAnimations() {
        if (animationManager != null) {
            animationManager.playAll();
        }
    }


    /**
     * Pauses all animations using the {@link AnimationManager}.
     */
    private void pauseAllAnimations() {
        if (animationManager != null) {
            animationManager.pauseAll();
        }
    }


    /**
     * Stops all animations and resets play state.
     */
    private void stopAllAnimations() {
        if (animationManager != null) {
            animationManager.stopAll();
        }
        isPlaying = false;
        btnPlay.setText("▶");
    }


    /**
     * Adjusts the speed of both animation and simulation.
     *
     * @param speedMultiplier the multiplier to apply to the current speed
     */
    private void adjustSpeed(double speedMultiplier) {
        currentSpeed *= speedMultiplier;
        currentSpeed = Math.max(MIN_SPEED, Math.min(MAX_SPEED, currentSpeed));
        // Calculate new delay
        long newDelay = (long) (100 / currentSpeed);
        long delay = Math.max(25L, newDelay);
        // Adjust animation speed
        if (animationManager != null) {
            animationManager.setSpeed(currentSpeed);
        }
        // Adjust simulation speed
        if (simulationManager != null && !simulationManager.isSimulationPaused()) {
            simulationManager.adjustSpeed(currentSpeed);
            updateConsole(String.format("⚡ Speed adjusted to %.2fx (simulation delay: %dms)",
                    currentSpeed, delay));
        } else {
            updateConsole(String.format("⚡ Animation speed adjusted to %.2fx (simulation paused - delay will apply on resume: %dms)",
                    currentSpeed, delay));
        }
    }


    /**
     * Updates the console output area with a new message.
     *
     * @param message the message to append
     */
    private void updateConsole(String message) {
        consoleArea.appendText(message + "\n");
        consoleArea.setScrollTop(Double.MAX_VALUE);
    }


    // ===== PUBLIC STATIC ACCESS FOR SIMULATION EVENTS =====

    /**
     * Returns the singleton instance of this controller.
     *
     * @return the MainScreenController instance
     */
    public static MainScreenController getInstance() {
        return instance;
    }


    // ===== SIMULATION EVENT CALLBACKS =====

    /**
     * Called when a customer arrives in the simulation.
     *
     * @param isWalkIn true if the customer is a walk-in, false otherwise
     */
    public void onCustomerArrival(boolean isWalkIn) {
        if (eventLogger != null) {
            eventLogger.onCustomerArrival(isWalkIn);
        }
    }

    /**
     * Called when a service begins for a customer.
     *
     * @param servicePointName the name of the service point
     * @param customerId the ID of the customer
     */
    public void onServiceBegin(String servicePointName, String customerId) {
        if (eventLogger != null) {
            eventLogger.onServiceBegin(servicePointName, customerId);
        }
    }

    /**
     * Called when a customer departs from a service point.
     *
     * @param servicePointName the name of the service point
     * @param customerId the ID of the customer
     * @param nextDestination the next destination of the customer
     */
    public void onCustomerDeparture(String servicePointName, String customerId, String nextDestination) {
        if (eventLogger != null) {
            eventLogger.onCustomerDeparture(servicePointName, customerId, nextDestination);
        }
    }

    /**
     * Called when a customer departs for a special event.
     *
     * @param servicePointName the name of the service point
     * @param customerId the ID of the customer
     * @param eventType the type of special event
     */
    public void onSpecialDeparture(String servicePointName, String customerId, String eventType) {
        if (eventLogger != null) {
            eventLogger.onSpecialDeparture(servicePointName, customerId, eventType);
        }
    }

    /**
     * Called when a customer is successfully served.
     *
     * @param customerId the ID of the customer
     */
    public void onCustomerServed(String customerId) {
        if (eventLogger != null) {
            eventLogger.onCustomerServed(customerId);
        }
    }

    /**
     * Called when a customer is not served, with a reason.
     *
     * @param customerId the ID of the customer
     * @param reason the reason the customer was not served
     */
    public void onCustomerNotServed(String customerId, String reason) {
        if (eventLogger != null) {
            eventLogger.onCustomerNotServed(customerId, reason);
        }
    }


    // ===== FXML EVENT HANDLERS =====

    /**
     * Handles the Play/Pause button click event.
     *
     * @param event the action event
     */
    @FXML
    void onPlayButtonClicked(ActionEvent event) {
        togglePlayPause();
    }

    /**
     * Handles the Slow Down button click event.
     *
     * @param event the action event
     */
    @FXML
    void onSlowDownButtonClicked(ActionEvent event) {
        adjustSpeed(0.5);
    }

    /**
     * Handles the Speed Up button click event.
     *
     * @param event the action event
     */
    @FXML
    void onSpeedButtonClicked(ActionEvent event) {
        adjustSpeed(2.0);
    }

    /**
     * Handles the Back button click event. Navigates back to the configuration screen.
     *
     * @param event the action event
     */
    @FXML
    void onBackButtonClicked(ActionEvent event) {
        updateConsole("◀ Navigating back...");
        // Stop simulation if running
        if (simulationManager != null && simulationManager.isSimulationRunning()) {
            simulationManager.stopSimulation();
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/pizzeria_simulation_config.fxml"));
            BorderPane root = loader.load();
            // Get the controller and set stage if method exists
            Controller controller = loader.getController();
            // Try to set stage using reflection if method exists
            try {
                java.lang.reflect.Method setStageMethod = controller.getClass().getMethod("setStage", Stage.class);
                setStageMethod.invoke(controller, stage);
            } catch (NoSuchMethodException e) {
                // Method doesn't exist, that's okay
            } catch (Exception e) {
                System.out.println("Could not set stage: " + e.getMessage());
            }
            Scene scene = new Scene(root, 800, 500);
            try {
                scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
            } catch (Exception e) {
                System.out.println("Could not load CSS file, using default styling");
            }
            stage.setScene(scene);
            stage.setTitle("Pizzeria Simulation Configuration");
            stage.setResizable(false);
        } catch (IOException e) {
            System.err.println("Error loading configuration FXML: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Handles the Statistics button click event. Opens the statistics dashboard.
     *
     * @param event the action event
     */
    @FXML
    void onStatsButtonClicked(ActionEvent event) {
        updateConsole("📊 Opening statistics dashboard...");
        try {
            // Try to use OverviewTableController if it exists
            Class<?> overviewClass = Class.forName("controller.OverviewTableController");
            java.lang.reflect.Method showMethod = overviewClass.getMethod("showAsScene", Stage.class);
            showMethod.invoke(null, stage);
        } catch (ClassNotFoundException e) {
            updateConsole("⚠️ Statistics view not available");
            System.err.println("OverviewTableController not found");
        } catch (Exception e) {
            updateConsole("⚠️ Error opening statistics: " + e.getMessage());
            System.err.println("Error switching to stats view: " + e.getMessage());
            e.printStackTrace();
        }
    }
}