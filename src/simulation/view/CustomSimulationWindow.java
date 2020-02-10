package simulation.view;

import java.util.Map;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import simulation.controller.Simulation;

public class CustomSimulationWindow extends Application {

  private static final double WINDOW_WIDTH = 300;
  private static final double WINDOW_HEIGHT = 500;
  private static final double PADDING = 5;

  private String myWindowTitle;

  public CustomSimulationWindow(String simulationName) {
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
    ObservableList<String> options;


    master.add(new Label("Choose your custom settings!"), 0, 0);

    HBox hb = new HBox();
    Label label = new Label("File Path");
    TextField textField = new TextField();
    hb.getChildren().addAll(label, textField);
    hb.setSpacing(10);
    master.add(hb, 0,1);

    hb = new HBox();
    label = new Label("Rule Type: ");
    options = FXCollections.observableArrayList("fireRules", "gameOfLifeRules","percolationRules"
        , "predatorPreyRules", "rockPaperScissorsRules", "segregationRules");
    ComboBox comboBox = new ComboBox(options);
    hb.getChildren().addAll(label, comboBox);
    hb.setSpacing(10);
    master.add(hb, 0,2);

    hb = new HBox();
    label = new Label("Simulation Title: ");
    textField = new TextField();
    hb.getChildren().addAll(label, textField);
    hb.setSpacing(10);
    master.add(hb, 0,3);

    hb = new HBox();
    label = new Label("Simulation Author(s): ");
    textField = new TextField();
    hb.getChildren().addAll(label, textField);
    hb.setSpacing(10);
    master.add(hb, 0,4);

    hb = new HBox();
    label = new Label("Number of Global Variables: ");
    textField = new TextField();
    hb.getChildren().addAll(label, textField);
    hb.setSpacing(10);
    master.add(hb, 0,5);

    hb = new HBox();
    label = new Label("Global Vars (, ): ");
    textField = new TextField();
    hb.getChildren().addAll(label, textField);
    hb.setSpacing(10);
    master.add(hb, 0,6);

    hb = new HBox();
    label = new Label("Grid Width: ");
    textField = new TextField();
    hb.getChildren().addAll(label, textField);
    hb.setSpacing(10);
    master.add(hb, 0,7);

    hb = new HBox();
    label = new Label("Grid Height: ");
    textField = new TextField();
    hb.getChildren().addAll(label, textField);
    hb.setSpacing(10);
    master.add(hb, 0,8);

    hb = new HBox();
    label = new Label("Grid Is Toroidal: ");
    options = FXCollections.observableArrayList("true", "false");
    comboBox = new ComboBox(options);
    hb.getChildren().addAll(label, comboBox);
    hb.setSpacing(10);
    master.add(hb, 0,9);

    hb = new HBox();
    label = new Label("Neighborhood Type: ");
    options = FXCollections.observableArrayList("true", "false");
    comboBox = new ComboBox(options);
    hb.getChildren().addAll(label, comboBox);
    hb.setSpacing(10);
    master.add(hb, 0,10);

    hb = new HBox();
    label = new Label("Grid Type: ");
    options = FXCollections.observableArrayList("true", "false");
    comboBox = new ComboBox(options);
    hb.getChildren().addAll(label, comboBox);
    hb.setSpacing(10);
    master.add(hb, 0,11);

    hb = new HBox();
    label = new Label("Grid States File: ");
    textField = new TextField();
    hb.getChildren().addAll(label, textField);
    hb.setSpacing(10);
    master.add(hb, 0,12);

    master.setPadding(new Insets(PADDING));

    return master;
  }

}
