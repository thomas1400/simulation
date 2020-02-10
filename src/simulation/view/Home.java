package simulation.view;

import exceptions.MalformedXMLException;
import java.io.FileInputStream;
import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class Home extends Application {

  private static final int WINDOW_WIDTH = 512;
  private static final int WINDOW_HEIGHT = 512;

  /**
   * Main method for the application - entry point
   */
  public static void main(String[] args) {
    Application.launch(args);
  }

  /**
   * Entry point for application
   * @param primaryStage the main stage for the Home view
   * @throws IOException caused by FileInputStream for loading the FXML
   */
  @Override
  public void start(Stage primaryStage) throws IOException {
    FXMLLoader loader = new FXMLLoader();
    String fxmlPath = "src/simulation/view/HomeView.fxml";
    System.out.println(System.getProperty("user.dir"));
    FileInputStream fis = new FileInputStream(fxmlPath);
    GridPane ap = loader.load(fis);
    Scene myHomeScene = new Scene(ap, WINDOW_WIDTH, WINDOW_HEIGHT);
    primaryStage.setScene(myHomeScene);
    primaryStage.show();
  }

  @FXML
  private void launchSim() {
    SimulationWindow simulationWindow = new SimulationWindow();
    try{
      simulationWindow.start(new Stage());
    } catch (MalformedXMLException e){
      new Alert(AlertType.WARNING, "Malformed XML file - try another one", ButtonType.OK).show();
    } catch (NumberFormatException e2){
      new Alert(AlertType.WARNING, "Error in XML file - try another one. "
          + "Error message: " + e2.getMessage(), ButtonType.OK).show();
    }
  }

}
