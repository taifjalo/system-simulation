package view.overview;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import simu.backend.dao.OverviewStatisticsDao;
import simu.backend.entity.OverviewStatistics;
import simu.framework.Trace;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Service class that handles all business logic for the Overview Table.
 * <p>
 * Separates data processing and business rules from UI concerns.
 * Loads, formats, and cleans up simulation statistics for display.
 */
public class OverviewTableService {
    

    /** Formatter for numeric values. */
    private final DecimalFormat formatter = new DecimalFormat("#0.##");
    /** DAO for accessing overview statistics. */
    private final OverviewStatisticsDao dao;
    

    /**
     * Default constructor. Initializes the DAO.
     */
    public OverviewTableService() {
        this.dao = new OverviewStatisticsDao();
    }
    

    /**
     * Constructor for dependency injection (useful for testing).
     * @param dao the OverviewStatisticsDao to use
     */
    public OverviewTableService(OverviewStatisticsDao dao) {
        this.dao = dao;
    }
    
    /**
     * Loads and formats all simulation overview data.
     * @return ObservableList of formatted simulation data ready for display
     */
    public ObservableList<SimulationOverview> loadSimulationOverviewData() {
        ObservableList<SimulationOverview> data = FXCollections.observableArrayList();
        
        try {
            Trace.out(Trace.Level.INFO, "Loading statistics from database");
            List<OverviewStatistics> statistics = dao.findAll();
            Trace.out(Trace.Level.INFO, "DAO findAll() returned: " + (statistics != null ? statistics.size() + " records" : "null"));

            if (statistics != null && !statistics.isEmpty()) {
                Trace.out(Trace.Level.INFO, "Processing " + statistics.size() + " statistics records...");

                // Optional: trace first few records at WAR level
                for (int i = 0; i < Math.min(3, statistics.size()); i++) {
                    OverviewStatistics stat = statistics.get(i);
                    Trace.out(Trace.Level.WAR, "Record " + (i + 1) + " ID=" + stat.getId()
                            + ", SimTime=" + stat.getSimulationTime()
                            + ", TotalSimTime=" + stat.getTotalSimulationTime()
                            + ", ArrivedCustomers=" + stat.getTotalArrivedCustomers()
                            + ", ServicedCustomers=" + stat.getTotalServicedCustomers());
                }

                processRealData(data, statistics);
                Trace.out(Trace.Level.INFO, "Successfully loaded " + statistics.size() + " simulation runs from database");
                Trace.out(Trace.Level.INFO, "Generated " + data.size() + " table rows for display");
            } else {
                Trace.out(Trace.Level.WAR, "No database data found - statistics list is " + (statistics == null ? "null" : "empty"));
            }
        } catch (Exception e) {
            Trace.out(Trace.Level.ERR, "Error loading database data: " + e.getMessage());
            java.io.StringWriter sw = new java.io.StringWriter();
            e.printStackTrace(new java.io.PrintWriter(sw));
            Trace.out(Trace.Level.WAR, sw.toString());
        }
        
        return data;
    }
    
    /**
     * Cleans up invalid database records (where total_simulation_time = 0).
     */
    public void cleanupInvalidRecords() {
        try {
            System.out.println("=== CLEANING UP INVALID RECORDS ===");
            int deletedCount = dao.deleteInvalidRecords();
            System.out.println("✅ Cleanup completed. Deleted " + deletedCount + " invalid records.");
        } catch (Exception e) {
            System.err.println("❌ Error during cleanup: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Determines how many data columns should be visible based on available data.
     * @param data The loaded simulation data
     * @return Number of columns that contain actual data
     */
    public int calculateVisibleColumns(ObservableList<SimulationOverview> data) {
        if (data.isEmpty()) return 4; // Show 4 columns even when no data
        
        // Check how many runs have data (Run1 = most recent)
        SimulationOverview firstRow = data.get(0);
        int count = 0;
        if (hasValidData(firstRow.getRun1())) count++;
        if (hasValidData(firstRow.getRun2())) count++;
        if (hasValidData(firstRow.getRun3())) count++;
        if (hasValidData(firstRow.getRun4())) count++;
        
        return Math.max(count, 4); // At least 4 columns
    }
    
    /**
     * Calculates the optimal table width based on number of visible columns.
     * @param visibleColumns Number of data columns with actual data
     * @return Recommended table width in pixels
     */
    public double calculateTableWidth(int visibleColumns) {
        return 180 + (visibleColumns * 100); // metric column + data columns
    }
    
    /**
     * Processes raw database statistics into formatted display data.
     * Orders data from most recent (Run 1) to oldest (Run 4).
     * Filters out invalid/duplicate records.
     * @param data the ObservableList to populate
     * @param statistics the list of OverviewStatistics from the database
     */
    private void processRealData(ObservableList<SimulationOverview> data, List<OverviewStatistics> statistics) {
        // Filter out invalid records - a record is valid if it has either:
        // 1. total_simulation_time > 0, OR
        // 2. total_arrived_customers > 0 (meaning simulation ran but time wasn't recorded properly)
        List<OverviewStatistics> validStats = statistics.stream()
            .filter(stat -> stat.getTotalSimulationTime() > 0 || stat.getTotalArrivedCustomers() > 0)
            .collect(java.util.stream.Collectors.toList());
            
    Trace.out(Trace.Level.INFO, "Filtered to " + validStats.size() + " valid records (total_simulation_time > 0 OR total_arrived_customers > 0)");
        
        if (validStats.isEmpty()) {
            Trace.out(Trace.Level.WAR, "No valid statistics found - all records have both total_simulation_time = 0 AND total_arrived_customers = 0");
            return;
        }
        
        // Get the last few simulation runs (up to 4), most recent first
        int size = Math.min(4, validStats.size());
        OverviewStatistics[] runs = new OverviewStatistics[4];
        
        // Fill runs array with most recent valid records first (Run1 = most recent)
        for (int i = 0; i < size; i++) {
            runs[i] = validStats.get(validStats.size() - 1 - i);
            Trace.out(Trace.Level.WAR, "Run " + (i + 1) + ": ID=" + runs[i].getId()
                    + ", TotalSimTime=" + runs[i].getTotalSimulationTime()
                    + ", ArrivedCustomers=" + runs[i].getTotalArrivedCustomers()
                    + ", ServicedCustomers=" + runs[i].getTotalServicedCustomers());
        }
        
        // Create formatted rows for each metric from database schema
        // Basic customer metrics
        data.add(createMetricRow("Total arrived customers", runs, 
            stat -> String.valueOf(stat.getTotalArrivedCustomers())));
            
        data.add(createMetricRow("Total serviced customers", runs, 
            stat -> String.valueOf(stat.getTotalServicedCustomers())));
            
        data.add(createMetricRow("Refused delivery customers", runs, 
            stat -> String.valueOf(stat.getRefusedDeliveryCustomers())));
            
        data.add(createMetricRow("Return money customers", runs, 
            stat -> String.valueOf(stat.getReturnMoneyCustomers())));
            
        data.add(createMetricRow("Remake orders customers", runs, 
            stat -> String.valueOf(stat.getRemakeOrdersCustomers())));
            
        // Time metrics
        data.add(createMetricRow("Simulation time", runs, 
            stat -> formatter.format(stat.getSimulationTime())));
            
        data.add(createMetricRow("Total simulation time", runs, 
            stat -> formatter.format(stat.getTotalSimulationTime())));
            
        data.add(createMetricRow("Call-in mean time", runs, 
            stat -> formatter.format(stat.getCallInMeanTime())));
            
        data.add(createMetricRow("Walk-in mean time", runs, 
            stat -> formatter.format(stat.getWalkInMeanTime())));
            
        data.add(createMetricRow("Total waiting time", runs, 
            stat -> formatter.format(stat.getTotalWaitingTime())));
            
        // Performance metrics
        data.add(createMetricRow("System throughput", runs, 
            stat -> stat.getThroughput() >= 0 ? formatter.format(stat.getThroughput()) : "-"));
            
        data.add(createMetricRow("Average response time", runs, 
            stat -> stat.getAvgResponseTime() >= 0 ? formatter.format(stat.getAvgResponseTime()) : "-"));
            
        // Calculated metrics
        data.add(createMetricRow("Utilization", runs, 
            stat -> stat.getUtilization() >= 0 ? formatter.format(stat.getUtilization()) + "%" : "-"));
            
        data.add(createMetricRow("Avg queue length", runs, 
            stat -> stat.getAvgQueueLength() >= 0 ? formatter.format(stat.getAvgQueueLength()) : "-"));
    }
    
    /**
     * Creates a formatted metric row using a data extractor function.
     * @param metricName the name of the metric
     * @param runs the array of OverviewStatistics for each run
     * @param extractor the function to extract the value from a statistic
     * @return a SimulationOverview row for the table
     */
    private SimulationOverview createMetricRow(String metricName, OverviewStatistics[] runs, 
                                             DataExtractor extractor) {
        String run1Val = runs[0] != null ? extractor.extract(runs[0]) : "-";
        String run2Val = runs[1] != null ? extractor.extract(runs[1]) : "-";
        
        // Debug logging for troublesome metrics
        if (runs[0] != null && (metricName.equals("Utilization") || metricName.equals("System throughput") || metricName.equals("Average response time") || metricName.equals("Total waiting time"))) {
            OverviewStatistics stat = runs[0];
            Trace.out(Trace.Level.WAR, metricName + " Debug - SimTime: " + stat.getSimulationTime()
                    + ", ServicedCustomers: " + stat.getTotalServicedCustomers()
                    + ", WaitingTime: " + stat.getTotalWaitingTime()
                    + ", SystemThroughput: " + stat.getSystemThroughput()
                    + ", AvgResponseTime: " + stat.getAverageResponseTime()
                    + ", Calculated Value: " + run1Val);
            
            // Additional debug for waiting time analysis
            if (metricName.equals("Total waiting time")) {
                float avgPerCustomer = stat.getTotalServicedCustomers() > 0 ? 
                    stat.getTotalWaitingTime() / stat.getTotalServicedCustomers() : 0;
                Trace.out(Trace.Level.WAR, "  -> Average waiting time per customer: " + avgPerCustomer);
                Trace.out(Trace.Level.WAR, "  -> This seems " + (avgPerCustomer > 100 ? "very high" : avgPerCustomer > 10 ? "reasonable" : "low"));
            }
        }
        
        return new SimulationOverview(
            metricName,
            run1Val,
            run2Val,
            runs[2] != null ? extractor.extract(runs[2]) : "-",
            runs[3] != null ? extractor.extract(runs[3]) : "-"
        );
    }
    
    /**
     * Checks if a data value represents valid (non-empty) data.
     * @param value the value to check
     * @return true if the value is valid, false otherwise
     */
    private boolean hasValidData(String value) {
        return value != null && !value.equals("-") && !value.trim().isEmpty();
    }
    
    /**
     * Functional interface for extracting data from OverviewStatistics.
     */
    @FunctionalInterface
    private interface DataExtractor {
        String extract(OverviewStatistics stat);
    }
}