package controller;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import view.overview.OverviewTableService;
import view.overview.SimulationOverview;
import simu.backend.dao.*;
import simu.backend.entity.*;
import simu.framework.Trace;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Controller for the Overview Table.
 * <p>
 * Handles UI concerns and application coordination for the overview table.
 * Follows MVC by delegating business logic to {@link view.overview.OverviewTableService}.
 * Provides methods for displaying, exporting, and refreshing simulation statistics.
 */
public class OverviewTableController implements Initializable {
    

    /** TableView for displaying simulation overview data. */
    @FXML
    private TableView<SimulationOverview> overviewTable;
    /** TableColumn for metric names. */
    @FXML
    private TableColumn<SimulationOverview, String> metricColumn;
    /** TableColumn for most recent run. */
    @FXML
    private TableColumn<SimulationOverview, String> run1Column;
    /** TableColumn for second most recent run. */
    @FXML
    private TableColumn<SimulationOverview, String> run2Column;
    /** TableColumn for third most recent run. */
    @FXML
    private TableColumn<SimulationOverview, String> run3Column;
    /** TableColumn for fourth most recent run. */
    @FXML
    private TableColumn<SimulationOverview, String> run4Column;
    /** Service for business logic and data loading. */
    private OverviewTableService service;
    

    /**
     * Default constructor. Initializes the service.
     */
    public OverviewTableController() {
        this.service = new OverviewTableService();
    }
    

    /**
     * Constructor for dependency injection (useful for testing).
     * @param service the OverviewTableService to use
     */
    public OverviewTableController(OverviewTableService service) {
        this.service = service;
    }
    

    /**
     * Initializes the controller after FXML loading. Sets up columns and loads data.
     * @param location the location used to resolve relative paths
     * @param resources the resources used to localize the root object
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupTableColumns();
        loadAndDisplayData();
    }
    

    /**
     * Sets up the table columns with their cell value factories.
     */
    private void setupTableColumns() {
        metricColumn.setCellValueFactory(new PropertyValueFactory<>("metric"));
        run1Column.setCellValueFactory(new PropertyValueFactory<>("run1"));
        run2Column.setCellValueFactory(new PropertyValueFactory<>("run2"));
        run3Column.setCellValueFactory(new PropertyValueFactory<>("run3"));
        run4Column.setCellValueFactory(new PropertyValueFactory<>("run4"));
    }
    

    /**
     * Loads data using the service and updates the UI.
     */
    private void loadAndDisplayData() {
        ObservableList<SimulationOverview> data = service.loadSimulationOverviewData();
        adjustTableDisplay(data);
        overviewTable.setItems(data);
    }
    

    /**
     * Adjusts table display properties based on available data.
     * @param data the simulation overview data
     */
    private void adjustTableDisplay(ObservableList<SimulationOverview> data) {
        int visibleColumns = service.calculateVisibleColumns(data);
        double tableWidth = service.calculateTableWidth(visibleColumns);
        // Apply calculated dimensions to UI
        overviewTable.setPrefWidth(tableWidth);
        overviewTable.setMaxWidth(tableWidth);
        overviewTable.setMinWidth(tableWidth);
        // Show/hide columns based on available data
        run1Column.setVisible(visibleColumns >= 1);
        run2Column.setVisible(visibleColumns >= 2);
        run3Column.setVisible(visibleColumns >= 3);
        run4Column.setVisible(visibleColumns >= 4);
    }
    

    /**
     * Public method to refresh the table data.
     * Can be called from other parts of the application.
     */
    public void refreshData() {
        loadAndDisplayData();
    }
    

    /**
     * Cleans up invalid database records and refreshes the display.
     */
    public void cleanupAndRefresh() {
        service.cleanupInvalidRecords();
        loadAndDisplayData();
    }
    
    /**
     * Static method to show results window using FXML.
     * This is the main entry point for displaying the overview table.
     */
    public static void showResults() {
        try {
            FXMLLoader loader = new FXMLLoader(OverviewTableController.class.getResource("/view/overview/OverviewTable.fxml"));
            BorderPane root = loader.load();
            
            Stage stage = new Stage();
            Scene scene = new Scene(root, 600, 440);
            
            // Apply CSS styling (optional)
            try {
                scene.getStylesheets().add(OverviewTableController.class.getResource("/view/overview/table-style.css").toExternalForm());
            } catch (Exception e) {
                Trace.out(Trace.Level.WAR, "Could not load CSS file, using default styling");
            }
            
            stage.setTitle("Simulation Results - Overview Statistics");
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
            
        } catch (IOException e) {
            Trace.out(Trace.Level.ERR, "Error loading FXML: " + e.getMessage());
            java.io.StringWriter sw = new java.io.StringWriter();
            e.printStackTrace(new java.io.PrintWriter(sw));
            Trace.out(Trace.Level.WAR, sw.toString());
        }
    }
    
    /**
     * Show overview table as a new scene in the existing stage.
     * @param stage the stage to display the overview table in
     */
    public static void showAsScene(Stage stage) {
        try {
            FXMLLoader loader = new FXMLLoader(OverviewTableController.class.getResource("/view/overview/OverviewTable.fxml"));
            BorderPane root = loader.load(); // Call load() first, which returns the root

            // Get the controller instance and set the stage reference
            OverviewTableController controller = loader.getController();
            controller.setStage(stage);

            // Use fixed dimensions to prevent window growth
            Scene scene = new Scene(root, 800, 500);

            // Apply CSS styling
            try {
                scene.getStylesheets().add(OverviewTableController.class.getResource("/view/overview/table-style.css").toExternalForm());
            } catch (Exception e) {
                Trace.out(Trace.Level.WAR, "Could not load CSS file, using default styling");
            }

            stage.setScene(scene);
            stage.setTitle("Simulation Results - Overview Statistics");
            stage.setResizable(false);

        } catch (IOException e) {
            Trace.out(Trace.Level.ERR, "Error loading FXML: " + e.getMessage());
            java.io.StringWriter sw = new java.io.StringWriter();
            e.printStackTrace(new java.io.PrintWriter(sw));
            Trace.out(Trace.Level.WAR, sw.toString());
        }
    }



    /** Reference to the primary stage. */
    private Stage stage;

    /**
     * Sets the primary stage for this controller.
     * @param stage the primary stage
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }
    
    /**
     * Handles the Back button click event. Returns to the main configuration screen.
     */
    @FXML
    public void handleBackClick() {
        try {
            // Load the main configuration scene
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/pizzeria_simulation_config.fxml"));
            BorderPane root = loader.load();
            
            // Get the controller and set the stage reference
            Controller controller = loader.getController();
            controller.setStage(stage);
            
            // Use fixed dimensions to prevent window growth
            Scene scene = new Scene(root, 800, 500);
            
            // Apply CSS styling
            try {
                scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
            } catch (Exception e) {
                Trace.out(Trace.Level.WAR, "Could not load CSS file, using default styling");
            }
            
            stage.setScene(scene);
            stage.setTitle("Pizzeria Simulation Configuration");
            stage.setResizable(false); // Prevent window resizing to maintain consistent size
            
        } catch (IOException e) {
            Trace.out(Trace.Level.ERR, "Error loading configuration FXML: " + e.getMessage());
            java.io.StringWriter sw = new java.io.StringWriter();
            e.printStackTrace(new java.io.PrintWriter(sw));
            Trace.out(Trace.Level.WAR, sw.toString());
        }
    }
    

    /**
     * Handles the Back to Workflow button click event. Currently delegates to handleBackClick.
     */
    @FXML
    public void handleBackToWorkflow() {
        // For now, delegate to handleBackClick - can be customized later if needed
        handleBackClick();
    }
    
    /**
     * Handles the Export to JSON button click event. Exports the complete database to a JSON file.
     */
    @FXML
    public void handleExportToJson() {
        try {
            // Open file chooser
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Export Complete Database to JSON");
            fileChooser.setInitialFileName("pizzeria_simulation_database_" + getCurrentTimestamp() + ".json");
            fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("JSON Files", "*.json")
            );
            
            File file = fileChooser.showSaveDialog(stage);
            
            if (file != null) {
                exportCompleteDatabase(file);
                showAlert("Export Successful", "Complete database exported successfully to:\n" + file.getAbsolutePath());
            }
            
        } catch (Exception e) {
            Trace.out(Trace.Level.ERR, "Error exporting database to JSON: " + e.getMessage());
            java.io.StringWriter sw = new java.io.StringWriter();
            e.printStackTrace(new java.io.PrintWriter(sw));
            Trace.out(Trace.Level.WAR, sw.toString());
            showAlert("Export Error", "Failed to export database: " + e.getMessage());
        }
    }
    
    /**
     * Exports the complete database to a JSON file in an orderly fashion.
     * @param file the file to export to
     * @throws IOException if writing to the file fails
     */
    private void exportCompleteDatabase(File file) throws IOException {
        StringBuilder json = new StringBuilder();
        
        // Initialize DAOs
        OverviewStatisticsDao overviewDao = new OverviewStatisticsDao();
        ReceptionStatisticsDao receptionDao = new ReceptionStatisticsDao();
        KitchenStatisticsDao kitchenDao = new KitchenStatisticsDao();
        CounterStatisticsDao counterDao = new CounterStatisticsDao();
        DeliveryStatisticsDao deliveryDao = new DeliveryStatisticsDao();
        
        // Get all data from database
        List<OverviewStatistics> overviewStats = overviewDao.findAll();
        
        json.append("{\n");
        json.append("  \"export_info\": {\n");
        json.append("    \"timestamp\": \"").append(getCurrentTimestamp()).append("\",\n");
        json.append("    \"description\": \"Complete Pizzeria Simulation Database Export\",\n");
        json.append("    \"database_name\": \"simu_project\",\n");
        json.append("    \"total_simulation_runs\": ").append(overviewStats != null ? overviewStats.size() : 0).append("\n");
        json.append("  },\n");
        
        // Export Overview Statistics (main simulation runs)
        json.append("  \"overview_statistics\": [\n");
        if (overviewStats != null && !overviewStats.isEmpty()) {
            for (int i = 0; i < overviewStats.size(); i++) {
                OverviewStatistics stat = overviewStats.get(i);
                if (i > 0) json.append(",\n");
                
                json.append("    {\n");
                json.append("      \"id\": ").append(stat.getId()).append(",\n");
                json.append("      \"simulation_time\": ").append(stat.getSimulationTime()).append(",\n");
                json.append("      \"total_simulation_time\": ").append(stat.getTotalSimulationTime()).append(",\n");
                json.append("      \"call_in_mean_time\": ").append(stat.getCallInMeanTime()).append(",\n");
                json.append("      \"walk_in_mean_time\": ").append(stat.getWalkInMeanTime()).append(",\n");
                json.append("      \"total_arrived_customers\": ").append(stat.getTotalArrivedCustomers()).append(",\n");
                json.append("      \"total_serviced_customers\": ").append(stat.getTotalServicedCustomers()).append(",\n");
                json.append("      \"refused_delivery_customers\": ").append(stat.getRefusedDeliveryCustomers()).append(",\n");
                json.append("      \"return_money_customers\": ").append(stat.getReturnMoneyCustomers()).append(",\n");
                json.append("      \"remake_orders_customers\": ").append(stat.getRemakeOrdersCustomers()).append(",\n");
                json.append("      \"total_waiting_time\": ").append(stat.getTotalWaitingTime()).append(",\n");
                json.append("      \"system_throughput\": ").append(stat.getSystemThroughput()).append(",\n");
                json.append("      \"average_response_time\": ").append(stat.getAverageResponseTime()).append(",\n");
                json.append("      \"busy_time\": ").append(stat.getBusyTime() != null ? stat.getBusyTime() : "null").append(",\n");
                
                // Calculated metrics
                json.append("      \"calculated_metrics\": {\n");
                json.append("        \"utilization\": ").append(stat.getUtilization()).append(",\n");
                json.append("        \"throughput\": ").append(stat.getThroughput()).append(",\n");
                json.append("        \"avg_response_time\": ").append(stat.getAvgResponseTime()).append(",\n");
                json.append("        \"avg_queue_length\": ").append(stat.getAvgQueueLength()).append("\n");
                json.append("      }\n");
                json.append("    }");
            }
        }
        json.append("\n  ],\n");
        
        // Export detailed service point statistics for each overview record
        json.append("  \"service_point_statistics\": {\n");
        
        if (overviewStats != null && !overviewStats.isEmpty()) {
            boolean firstOverview = true;
            for (OverviewStatistics overview : overviewStats) {
                if (!firstOverview) json.append(",\n");
                firstOverview = false;
                
                json.append("    \"simulation_run_").append(overview.getId()).append("\": {\n");
                
                // Get service point statistics for this overview ID
                List<ReceptionStatistics> receptionStats = getReceptionStatisticsByOverviewId(receptionDao, overview.getId());
                List<KitchenStatistics> kitchenStats = getKitchenStatisticsByOverviewId(kitchenDao, overview.getId());
                List<CounterStatistics> counterStats = getCounterStatisticsByOverviewId(counterDao, overview.getId());
                List<DeliveryStatistics> deliveryStats = getDeliveryStatisticsByOverviewId(deliveryDao, overview.getId());
                
                // Reception Statistics
                json.append("      \"reception_statistics\": [\n");
                exportServicePointStatistics(json, receptionStats, "reception");
                json.append("      ],\n");
                
                // Kitchen Statistics
                json.append("      \"kitchen_statistics\": [\n");
                exportServicePointStatistics(json, kitchenStats, "kitchen");
                json.append("      ],\n");
                
                // Counter Statistics
                json.append("      \"counter_statistics\": [\n");
                exportServicePointStatistics(json, counterStats, "counter");
                json.append("      ],\n");
                
                // Delivery Statistics
                json.append("      \"delivery_statistics\": [\n");
                exportServicePointStatistics(json, deliveryStats, "delivery");
                json.append("      ]\n");
                
                json.append("    }");
            }
        }
        
        json.append("\n  }\n");
        json.append("}");
        
        // Write to file
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(json.toString());
        }
        
    Trace.out(Trace.Level.INFO, "Successfully exported complete database to: " + file.getAbsolutePath());
    }
    
    /**
     * Helper method to export service point statistics in a generic way.
     * @param json the StringBuilder to append to
     * @param statistics the list of statistics objects
     * @param serviceType the type of service point
     */
    private void exportServicePointStatistics(StringBuilder json, List<?> statistics, String serviceType) {
        if (statistics != null && !statistics.isEmpty()) {
            for (int i = 0; i < statistics.size(); i++) {
                if (i > 0) json.append(",\n");
                
                Object stat = statistics.get(i);
                json.append("        {\n");
                
                // Use reflection-like approach or instanceof to handle different types
                if (stat instanceof ReceptionStatistics) {
                    ReceptionStatistics rs = (ReceptionStatistics) stat;
                    exportServicePointFields(json, rs.getId(), rs.getOverviewId(), rs.getArrivedCustomers(),
                        rs.getServicedCustomers(), rs.getServiceBusyTime(), rs.getServiceUtilization(),
                        rs.getServiceThroughput(), rs.getAverageServiceTime(), rs.getWaitingTime(),
                        rs.getAverageQueueLength(), rs.getMeanValue(), rs.getVarianceValue());
                } else if (stat instanceof KitchenStatistics) {
                    KitchenStatistics ks = (KitchenStatistics) stat;
                    exportServicePointFields(json, ks.getId(), ks.getOverviewId(), ks.getArrivedCustomers(),
                        ks.getServicedCustomers(), ks.getServiceBusyTime(), ks.getServiceUtilization(),
                        ks.getServiceThroughput(), ks.getAverageServiceTime(), ks.getWaitingTime(),
                        ks.getAverageQueueLength(), ks.getMeanValue(), ks.getVarianceValue());
                } else if (stat instanceof CounterStatistics) {
                    CounterStatistics cs = (CounterStatistics) stat;
                    exportServicePointFields(json, cs.getId(), cs.getOverviewId(), cs.getArrivedCustomers(),
                        cs.getServicedCustomers(), cs.getServiceBusyTime(), cs.getServiceUtilization(),
                        cs.getServiceThroughput(), cs.getAverageServiceTime(), cs.getWaitingTime(),
                        cs.getAverageQueueLength(), cs.getMeanValue(), cs.getVarianceValue());
                } else if (stat instanceof DeliveryStatistics) {
                    DeliveryStatistics ds = (DeliveryStatistics) stat;
                    exportServicePointFields(json, ds.getId(), ds.getOverviewId(), ds.getArrivedCustomers(),
                        ds.getServicedCustomers(), ds.getServiceBusyTime(), ds.getServiceUtilization(),
                        ds.getServiceThroughput(), ds.getAverageServiceTime(), ds.getWaitingTime(),
                        ds.getAverageQueueLength(), ds.getMeanValue(), ds.getVarianceValue());
                }
                
                json.append("        }");
            }
        }
    }
    
    /**
     * Helper method to export common service point fields.
     * @param json the StringBuilder to append to
     * @param id the record ID
     * @param overviewId the overview ID
     * @param arrivedCustomers number of arrived customers
     * @param servicedCustomers number of serviced customers
     * @param serviceBusyTime busy time
     * @param serviceUtilization utilization
     * @param serviceThroughput throughput
     * @param averageServiceTime average service time
     * @param waitingTime waiting time
     * @param averageQueueLength average queue length
     * @param meanValue mean value
     * @param varianceValue variance value
     */
    private void exportServicePointFields(StringBuilder json, int id, int overviewId, int arrivedCustomers,
                                        int servicedCustomers, float serviceBusyTime, float serviceUtilization,
                                        float serviceThroughput, float averageServiceTime, float waitingTime,
                                        float averageQueueLength, float meanValue, float varianceValue) {
        json.append("          \"id\": ").append(id).append(",\n");
        json.append("          \"overview_id\": ").append(overviewId).append(",\n");
        json.append("          \"arrived_customers\": ").append(arrivedCustomers).append(",\n");
        json.append("          \"serviced_customers\": ").append(servicedCustomers).append(",\n");
        json.append("          \"service_busy_time\": ").append(serviceBusyTime).append(",\n");
        json.append("          \"service_utilization\": ").append(serviceUtilization).append(",\n");
        json.append("          \"service_throughput\": ").append(serviceThroughput).append(",\n");
        json.append("          \"average_service_time\": ").append(averageServiceTime).append(",\n");
        json.append("          \"waiting_time\": ").append(waitingTime).append(",\n");
        json.append("          \"average_queue_length\": ").append(averageQueueLength).append(",\n");
        json.append("          \"mean_value\": ").append(meanValue).append(",\n");
        json.append("          \"variance_value\": ").append(varianceValue).append("\n");
    }
    
    /**
     * Helper method to get reception statistics by overview ID.
     * Note: This would need a proper DAO method, currently returns empty list.
     * @param dao the ReceptionStatisticsDao
     * @param overviewId the overview ID
     * @return list of ReceptionStatistics
     */
    private List<ReceptionStatistics> getReceptionStatisticsByOverviewId(ReceptionStatisticsDao dao, int overviewId) {
        // This would need a proper findByOverviewId method in the DAO
        return java.util.Collections.emptyList();
    }
    
    /**
     * Helper method to get kitchen statistics by overview ID.
     * Note: This would need a proper DAO method, currently returns empty list.
     * @param dao the KitchenStatisticsDao
     * @param overviewId the overview ID
     * @return list of KitchenStatistics
     */
    private List<KitchenStatistics> getKitchenStatisticsByOverviewId(KitchenStatisticsDao dao, int overviewId) {
        return java.util.Collections.emptyList();
    }
    
    /**
     * Helper method to get counter statistics by overview ID.
     * Note: This would need a proper DAO method, currently returns empty list.
     * @param dao the CounterStatisticsDao
     * @param overviewId the overview ID
     * @return list of CounterStatistics
     */
    private List<CounterStatistics> getCounterStatisticsByOverviewId(CounterStatisticsDao dao, int overviewId) {
        return java.util.Collections.emptyList();
    }
    
    /**
     * Helper method to get delivery statistics by overview ID.
     * Note: This would need a proper DAO method, currently returns empty list.
     * @param dao the DeliveryStatisticsDao
     * @param overviewId the overview ID
     * @return list of DeliveryStatistics
     */
    private List<DeliveryStatistics> getDeliveryStatisticsByOverviewId(DeliveryStatisticsDao dao, int overviewId) {
        return java.util.Collections.emptyList();
    }
    
    /**
     * Gets current timestamp formatted for filenames.
     * @return the current timestamp as a string
     */
    private String getCurrentTimestamp() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
    }
    
    /**
     * Shows an alert dialog to the user.
     * @param title the dialog title
     * @param message the dialog message
     */
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}