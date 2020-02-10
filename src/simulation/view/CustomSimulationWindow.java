package simulation.view;

import exceptions.MalformedXMLException;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import simulation.xmlGeneration.SimulationSettings;
import simulation.xmlGeneration.XMLGenerator;

public class CustomSimulationWindow extends Application {

  private static final double WINDOW_WIDTH = 300;
  private static final double WINDOW_HEIGHT = 500;
  private static final double PADDING = 5;

  private String myWindowTitle;
  private Stage myStage;

  public CustomSimulationWindow(String simulationName) {
    myWindowTitle = simulationName + " Settings";
  }

  @Override
  public void start(Stage primaryStage) {
    primaryStage.setTitle(myWindowTitle);
    myStage = primaryStage;
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

    Label titleLabel = new Label("Choose your custom settings!");
    titleLabel.setFont(new Font("Arial", 16));
    master.add(titleLabel, 0, 0);

    HBox hb = new HBox();
    Label label = new Label("File Path");
    TextField filePath = new TextField();
    hb.getChildren().addAll(label, filePath);
    hb.setSpacing(10);
    master.add(hb, 0,1);

    ComboBox ruleSelector = addDropdownInput(master, "Rule Type: ",
        FXCollections.observableArrayList("fireRules", "gameOfLifeRules", "percolationRules"
            , "predatorPreyRules", "rockPaperScissorsRules", "segregationRules"), 2);

    TextField simulationTitle = addTextInputBox(master, "Simulation Title: ", 3);
    TextField simulationAuthors = addTextInputBox(master, "Simulation Author(s): ", 4);
    TextField numGlobalVars = addTextInputBox(master, "Number of Global Variables: ", 5);
    TextField globalVars = addTextInputBox(master, "Global Vars (, ): ", 6);
    TextField gridWidth = addTextInputBox(master, "Grid Width: ", 7);
    TextField gridHeight = addTextInputBox(master, "Grid Height: ", 8);
    ComboBox isToroidal = addDropdownInput(master, "Grid Is Toroidal: ",
        FXCollections.observableArrayList("true", "false"), 9);
    ComboBox neighborhoodType = addDropdownInput(master, "Neighborhood Type: ",
        FXCollections.observableArrayList("1", "2"), 10);
    ComboBox gridType = addDropdownInput(master, "Grid Type: ",
        FXCollections.observableArrayList("rectangular", "triangular"), 11);

    TextField myTextFile = addGridStatesFile(master);

    Button button = new Button("Create and Run Custom Simulation!");
    button.setOnAction(value ->  {
      SimulationSettings customSim = new SimulationSettings();

      String myFile = filePath.getText();
      customSim.setFilePath(myFile);
      customSim.setRuleSelector(ruleSelector.getValue().toString());
      customSim.setSimulationTitle(simulationTitle.getText());
      customSim.setSimulationAuthor(simulationAuthors.getText());
      customSim.setNumGlobalVars(Integer.parseInt(numGlobalVars.getText()));
      int myWidth = Integer.parseInt(gridWidth.getText());
      customSim.setGridWidth(myWidth);
      int myHeight = Integer.parseInt(gridHeight.getText());
      customSim.setGridHeight(myHeight);
      customSim.setGridIsToroidal(Boolean.parseBoolean(isToroidal.getValue().toString()));
      customSim.setNeighborhoodType(Integer.parseInt(neighborhoodType.getValue().toString()));
      customSim.setGridType(gridType.getValue().toString());

      fillGlobalVarsArray(globalVars, customSim);

      try {
        fillInitialStateGrid(customSim, myTextFile.getText(), myWidth, myHeight);
      } catch (FileNotFoundException e) {
        new Alert(AlertType.WARNING, "Malformed XML file - try another one", ButtonType.OK).show();
      }

      XMLGenerator myGenerator = new XMLGenerator();
      try {
        myGenerator.generateXML(customSim);
      } catch (FileNotFoundException e) {
        new Alert(AlertType.WARNING, "Malformed XML file - try another one", ButtonType.OK).show();
      }

      SimulationWindow customSimulationWindow = new SimulationWindow();
      try {
        customSimulationWindow.start(new Stage(), myFile);
      } catch (MalformedXMLException e) {
        new Alert(AlertType.WARNING, "Malformed XML file - try another one", ButtonType.OK).show();
      }

      myStage.close();

    });
    master.add(button,0,13);

    master.setPadding(new Insets(PADDING));

    return master;
  }

  private TextField addTextInputBox(GridPane master, String s, int i) {
    HBox hb;
    Label label;
    hb = new HBox();
    label = new Label(s);
    TextField gridHeight = new TextField();
    hb.getChildren().addAll(label, gridHeight);
    hb.setSpacing(10);
    master.add(hb, 0, i);
    return gridHeight;
  }

  private ComboBox addDropdownInput(GridPane master, String s, ObservableList<String> strings, int i) {
    HBox hb;
    Label label;
    ObservableList<String> options;
    hb = new HBox();
    label = new Label(s);
    options = strings;
    ComboBox gridType = new ComboBox(options);
    hb.getChildren().addAll(label, gridType);
    hb.setSpacing(10);
    master.add(hb, 0, i);
    return gridType;
  }

  private TextField addGridStatesFile(GridPane master) {
    TextField gridStatesFile = addTextInputBox(master, "Grid States File: ", 12);
    return gridStatesFile;
  }

  private void fillGlobalVarsArray(TextField globalVars, SimulationSettings customSim) {
    String[] varsStrArray = globalVars.getText().split(", ");
    double[] varsDoubleArray = new double[varsStrArray.length];
    for (int i = 0; i < varsStrArray.length; i++){
      varsDoubleArray[i] = Integer.parseInt(varsStrArray[i]);
    }
    customSim.setGlobalVars(varsDoubleArray);
  }

  public void fillInitialStateGrid(SimulationSettings customSim, String myFile, int myWidth,
      int myHeight) throws FileNotFoundException {
    File file = new File("data/" + myFile);
    Scanner sc = new Scanner(file);
    int[][] myInitialStateGrid = new int[myHeight][myWidth];
    for (int i = 0; i < myHeight; i++) {
      for (int j = 0; j < myWidth; j++) {
        myInitialStateGrid[i][j] = Integer.parseInt(sc.next());
      }
    }
    customSim.setInitialStateGrid(myInitialStateGrid);
  }

}
