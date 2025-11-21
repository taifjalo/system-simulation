
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import controller.Controller;
/**
 * Main entry point for the Pizzeria Simulation application.
 * Initializes the JavaFX application and loads the main configuration screen.
 */
public class Main extends Application {


    /**
     * Starts the JavaFX application and loads the main configuration screen.
     *
     * @param primaryStage the primary stage for this application
     * @throws Exception if loading FXML or resources fails
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/pizzeria_simulation_config.fxml"));
        Scene scene = new Scene(loader.load());
        scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
        // Get the controller and set the stage reference
        Controller controller = loader.getController();
        controller.setStage(primaryStage);
        primaryStage.setTitle("Pizzeria Simulation Configuration");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false); // Prevent resizing to maintain consistent size
        primaryStage.show();
    }


    /**
     * Main method. Launches the JavaFX application.
     *
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
