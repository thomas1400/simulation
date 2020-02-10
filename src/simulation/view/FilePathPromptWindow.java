package simulation.view;

import java.util.Map;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import simulation.controller.Simulation;

public class FilePathPromptWindow extends Application {

  private static final double WINDOW_WIDTH = 150;
  private static final double WINDOW_HEIGHT = 50;
  private static final double PADDING = 5;

  private String myWindowTitle;

  public FilePathPromptWindow(String simulationName) {
    myWindowTitle = simulationName + " Settings";
  }

  @Override
  public void start(Stage primaryStage) {
    primaryStage.setTitle(myWindowTitle);
    Scene gridScene = new Scene(makeMasterGrid(), WINDOW_WIDTH, WINDOW_HEIGHT);

    String style = getClass().getResource("/resources/stylesheet.css").toExternalForm();
    gridScene.getStylesheets().add(style);

    primaryStage.setScene(gridScene);
    primaryStage.show();
  }

  private GridPane makeMasterGrid() {
    GridPane master = new GridPane();
    master.setHgap(5);
    master.setVgap(10);

    master.add(new Label("File Path Name:"), 0, 0);

    master.setPadding(new Insets(PADDING));

    return master;
  }

}
