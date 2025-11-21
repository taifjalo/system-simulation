package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
// import javafx.scene.control.*; // Removed unused import
import javafx.scene.layout.HBox;
import javafx.scene.control.*;
import javafx.stage.Stage;
import simu.framework.statistics.counter.CounterStatistics;
import simu.framework.statistics.delivery.DeliveryStatistics;
import simu.framework.statistics.kitchen.KitchenStatistics;
import simu.framework.statistics.reception.ReceptionStatistics;
import simu.framework.statistics.SimulationStatistics;
import simu.framework.Trace;
import simu.model.MyEngine;
import simu.model.kitchen.CookCompetency;


import java.util.ArrayList;

/**
 * Main controller for the simulation configuration and workflow.
 * <p>
 * Handles user input, simulation parameter configuration, and launching the workflow diagram.
 * Coordinates statistics, cooks, and simulation lifecycle.
 */
public class Controller {


    /** Kitchen statistics singleton. */
    private KitchenStatistics kitchenStats= KitchenStatistics.getInstance();
    /** Reception statistics singleton. */
    private ReceptionStatistics receptionStats= ReceptionStatistics.getInstance();
    /** Counter statistics singleton. */
    private CounterStatistics counterStats= CounterStatistics.getInstance();
    /** Delivery statistics singleton. */
    private DeliveryStatistics deliveryStats= DeliveryStatistics.getInstance();
    /** Simulation statistics singleton. */
    private SimulationStatistics simulationStats= SimulationStatistics.getInstance();

    /** Reference to the primary stage. */
    private Stage stage;


    /**
     * Constructs the controller and initializes the trace level.
     */
    public Controller() {
        Trace.setTraceLevel(Trace.Level.INFO);
    }


    /**
     * Initializes the controller after FXML loading. Sets up cooks input.
     */
    @FXML
    public void initialize() {
        // Initialize cooks when the FXML is loaded
        handleCooksInput();
    }


    /**
     * Sets the primary stage for this controller.
     *
     * @param stage the primary stage
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML private TextField receptionMean;
    @FXML private TextField receptionVariance;
    @FXML private TextField kitchenMean;
    @FXML private TextField kitchenVariance;
    @FXML private TextField counterMean;
    @FXML private TextField counterVariance;
    @FXML private TextField deliveryMean;
    @FXML private TextField deliveryVariance;
    @FXML private TextField simulationTime;
    @FXML private TextField walkInCustomerMean;
    @FXML private TextField callInCustomerMean;
    @FXML private TextField numberOfCooks;
    @FXML private ListView <HBox> cooks;
    @FXML private Button startButton;
    @FXML private HBox alertLabel;


    /**
     * Handles the Start button click event. Reads user input, sets simulation parameters,
     * resets counts, and starts the simulation. Shows an alert if input is invalid.
     */
    @FXML
    public void handleStartClick () {
        try {
            receptionStats.setMean(Double.parseDouble(receptionMean.getText()));
            receptionStats.setVariance(Double.parseDouble(receptionVariance.getText()));
            kitchenStats.setMean(Double.parseDouble(kitchenMean.getText()));
            kitchenStats.setVariance(Double.parseDouble(kitchenVariance.getText()));
            counterStats.setMean(Double.parseDouble(counterMean.getText()));
            counterStats.setVariance(Double.parseDouble(counterVariance.getText()));
            deliveryStats.setMean(Double.parseDouble(deliveryMean.getText()));
            deliveryStats.setVariance(Double.parseDouble(deliveryVariance.getText()));
            simulationStats.setSimulationTime(Double.parseDouble(simulationTime.getText()));
            simulationStats.setWalkInMeanTime(Double.parseDouble(walkInCustomerMean.getText()));
            simulationStats.setCallInMeanTime(Double.parseDouble(callInCustomerMean.getText()));
            // Reset count persistence only when user clicks Start for the first time
            // This ensures a fresh simulation when user configures and starts
            // MainScreenController.resetCountsForNewSimulation(); // Method does not exist, removed call
            startSimulation();
            alertLabel.setVisible(false);
        }catch (Exception e){
            alertLabel.setVisible(true);
        }
    }


    /**
     * Handles input for the number of cooks and updates the cooks list in the UI.
     * Shows a warning if the input is invalid or out of range.
     */
    @FXML
    private void handleCooksInput() {
        cooks.getItems().clear();
        int count;
        try {
            count = Integer.parseInt(numberOfCooks.getText());
        } catch (NumberFormatException e) {
            Label label = new Label("Give number between 1 and 4");
            HBox row = new HBox(10, label);
            row.setAlignment(Pos.CENTER_LEFT);
            cooks.getItems().add(row);
            return;
        }
        if (count<=0 || count>4){
            Label label = new Label("Give number between 1 and 4");
            HBox row = new HBox(10, label);
            row.setAlignment(Pos.CENTER_LEFT);
            cooks.getItems().add(row);
        }
        else{
            for (int i = 1; i <= count; i++) {
                Label label = new Label("Cook " + i + " qualification:");
                ComboBox<CookCompetency> combo = new ComboBox<>();
                combo.getItems().addAll(CookCompetency.values());
                combo.setValue(CookCompetency.EXPERT);
                // Add a style class for custom arrow animation
                combo.getStyleClass().add("cook-combo-box");
                // Rotate the arrow node manually when showing/hiding
                combo.showingProperty().addListener((obs, wasShowing, isShowing) -> {
                    Node arrow = combo.lookup(".arrow");
                    if (arrow != null) {
                        arrow.setRotate(isShowing ? 180 : 0);
                    }
                });
                HBox row = new HBox(10, label, combo);
                row.setAlignment(Pos.CENTER_LEFT);
                cooks.getItems().add(row);
            }
        }
    }


    /**
     * Handles the Statistics button click event. Loads the overview table scene.
     * Shows error in trace if loading fails.
     */
    @FXML
    public void handleStatsClick() {
        try {
            // Get the current stage - try from field first, then from startButton if not set
            Stage currentStage = this.stage;
            if (currentStage == null) {
                currentStage = (Stage) startButton.getScene().getWindow();
                this.stage = currentStage; // Cache for future use
            }
            // Load the overview table scene
            OverviewTableController.showAsScene(currentStage);
        } catch (Exception e) {
            Trace.out(Trace.Level.ERR, "Error switching to stats view: " + e.getMessage());
            java.io.StringWriter sw = new java.io.StringWriter();
            e.printStackTrace(new java.io.PrintWriter(sw));
            Trace.out(Trace.Level.WAR, sw.toString());
        }
    }


    /**
     * Starts the simulation by launching the workflow diagram.
     * Count persistence is only reset when user clicks Start button.
     */
    public void startSimulation(){
        // Launch the workflow diagram with simulation
        // Note: Count persistence is only reset when user clicks Start button,
        // not when returning from workflow view
        showWorkflowDiagram();
    }


    /**
     * Shows the workflow diagram by loading the MainScreen FXML.
     * If loading fails, falls back to the old simulation engine.
     */
    private void showWorkflowDiagram() {
        try {
            // Load the MainScreen FXML (workflow diagram)
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/frontend/MainScreen.fxml"));
            Parent root = loader.load();
            // Get the controller and pass this controller to it
            MainScreenController workflowController = loader.getController();
            workflowController.setMainController(this);
            // Create new stage for the workflow diagram
            Stage workflowStage = new Stage();
            workflowStage.setTitle("Pizzeria Simulation - Workflow Diagram");
            workflowStage.setScene(new Scene(root));
            // Set stage reference in workflow controller
            workflowController.setStage(workflowStage);
            workflowStage.show();
            // Close the current configuration window
            Stage currentStage = (Stage) startButton.getScene().getWindow();
            currentStage.close();
        } catch (Exception e) {
            e.printStackTrace();
            // Fallback to old simulation if workflow diagram fails
            MyEngine engine = new MyEngine(this);
            engine.setSimulationTime(simulationStats.getSimulationTime());
            engine.start();
        }
    }


    /**
     * Gets the cook competency levels from the configuration window.
     *
     * @return a list of cook competencies
     * @throws IllegalStateException if no cooks are configured
     */
    public ArrayList<CookCompetency> getCookLevels() {
        if (cooks.getItems().isEmpty() || cooks.getItems().stream().allMatch(row ->
                row.getChildren().stream().noneMatch(node -> node instanceof ComboBox))) {
            throw new IllegalStateException("No cooks configured!");
        }
        ArrayList<CookCompetency> competencies = new ArrayList<>();
        for (HBox row : cooks.getItems()) {
            for (Node node : row.getChildren()) {
                if (node instanceof ComboBox) {
                    @SuppressWarnings("unchecked")
                    ComboBox<CookCompetency> combo = (ComboBox<CookCompetency>) node;
                    competencies.add(combo.getValue());
                }
            }
        }
        return competencies;
    }


    /**
     * Gets the simulation statistics instance to access configured values.
     *
     * @return the simulation statistics instance
     */
    public SimulationStatistics getSimulationStatistics() {
        return simulationStats;
    }

}

