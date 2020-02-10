package simulation.view;

import exceptions.MalformedXMLException;
import java.io.FileNotFoundException;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import simulation.controller.Simulation;

public class FilePathPromptWindow {

  private static final double WINDOW_WIDTH = 250;
  private static final double WINDOW_HEIGHT = 125;
  private static final double PADDING = 5;

  private String myWindowTitle;
  private Simulation mySimulation;

  public FilePathPromptWindow(String simulationName) {
    myWindowTitle = simulationName + " Settings";
  }

  public void start(Stage primaryStage, Simulation simulation) {
    mySimulation = simulation;
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

    HBox hb = new HBox();
    Label label = new Label("File Path");
    TextField textField = new TextField();
    hb.getChildren().addAll(label, textField);
    hb.setSpacing(10);
    master.add(hb, 0,1);

    Button button = new Button("Choose this File");
    button.setOnAction(value ->  {
      try {
        mySimulation.saveSimulationState("data/" + textField.getText());
      } catch (FileNotFoundException e) {
        new Alert(AlertType.WARNING, "Malformed XML file - try another one", ButtonType.OK).show();
      }
    });
    master.add(button,0,2);

    master.setPadding(new Insets(PADDING));

    return master;
  }

}
