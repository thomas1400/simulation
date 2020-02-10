package simulation.view;

import exceptions.MalformedXMLException;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import simulation.controller.Simulation;
import simulation.events.IUpdate;
import java.io.File;
import java.io.IOException;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

/**
 * The 'simulation.view' for the Simulation project. Handles all visual aspects of the project, including
 * updating the main window and listening to the simulation simulation.controller
 */
public class SimulationWindow extends Application implements IUpdate {

  private static final int WINDOW_HEIGHT = 625 + 25;
  private static final int WINDOW_WIDTH = 512;
  private static final int BUTTON_START_INDEX = 0;
  private static final double PADDING = 5;

  private String windowTitle;
  private Stage mainWindow;
  private Simulation simulation;
  private GridPane mainGrid;
  private Pane gridPane;
  private GridPane buttonGroup;
  private GridPane graphGroup;
  private GridPane settingGroup;
  private String xmlFileName;
  private Stage myStage;
  private XYChart.Series data;
  private Map<String, EventHandler<ActionEvent>> buttonData;

  /**
   * Starts the JavaFX application and handles initial setup method calls
   *
   * @param primaryStage the main stage which the program draws on
   */
  @Override
  public void start(Stage primaryStage) throws MalformedXMLException {
    data = new XYChart.Series();
    myStage = primaryStage;
    xmlFileName = getSimulationFile();
    if (xmlFileName != null) {
      newSimulation();
    } else {
      myStage.close();
    }
  }

  public void start(Stage primaryStage, String xmlFile) throws MalformedXMLException {
    data = new XYChart.Series();
    myStage = primaryStage;
    xmlFileName = xmlFile;
    if (xmlFileName != null) {
      newSimulation();
    } else {
      myStage.close();
    }
  }

  private void newSimulation() throws MalformedXMLException {
    loadSimulation();
    setUpWindow(myStage);
    simulation.setListener(this);
    mainWindow.show();
    setUpWindow(myStage);
  }

  private String getSimulationFile() {
    FileChooser fc = new FileChooser();
    String dataPath = System.getProperty("user.dir") + "/data";
    File workingDirectory = new File(dataPath);
    fc.setInitialDirectory(workingDirectory);

    FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("XML files (*.xml)", "*.xml");
    fc.getExtensionFilters().add(extFilter);

    File file = fc.showOpenDialog(mainWindow);
    if (file == null) {
      return null;
    }
    return file.toString();
  }

  private void updateGUI() {
    mainGrid.getChildren().remove(gridPane);
    gridPane = simulation.getGridPane((int)(WINDOW_WIDTH - 2*PADDING));
    mainGrid.add(gridPane, 0, 2);

    // TODO: Add graph updating
  }

  private void setUpWindow(Stage primaryStage) throws MalformedXMLException {
    mainWindow = primaryStage;
    primaryStage.setOnCloseRequest(event -> simulation.stop());
    mainWindow.setTitle(windowTitle);
    Scene gridScene = new Scene(makeMasterGrid(), WINDOW_WIDTH, WINDOW_HEIGHT);
    String style = getClass().getResource("/resources/stylesheet.css").toExternalForm();
    gridScene.getStylesheets().add(style);
    mainWindow.setScene(gridScene);
  }

  private GridPane makeMasterGrid() throws MalformedXMLException {
    renderElements();
    mainGrid.setVgap(2.0);
    mainGrid.add(buttonGroup, 0, 0);
    mainGrid.add(gridPane, 0, 1);
    //mainGrid.add(graphGroup,0,3);

    mainGrid.setPadding(new Insets(PADDING));

    return mainGrid;
  }

  private void renderElements() throws MalformedXMLException {
    buttonGroup = new GridPane();
    gridPane = simulation.getGridPane((int)(WINDOW_WIDTH - 2*PADDING));
    graphGroup = new GridPane();
    mainGrid = new GridPane();

    makeButtons();
    makeGraphs();
  }

  private void makeGraphs(){
    graphGroup = new GridPane();
    simulation.getMaxSizes();
    // TODO: Get max values, so you don't have to graph all the way up to 1500
    NumberAxis xAxis = new NumberAxis(0, 1500, 1);
    xAxis.setLabel(simulation.getTitle());
    NumberAxis yAxis = new NumberAxis(0, 1500, 1);
    yAxis.setLabel(simulation.getTitle());
    LineChart lineChart = new LineChart(xAxis, yAxis);
    lineChart.getData().add(data);
    graphGroup.add(lineChart, 0, 0);
    graphGroup.autosize();
  }

  private void setupButtons() {
    buttonData = new LinkedHashMap<>();
    buttonData.putIfAbsent("Reset", e -> {
      try {
        reset();
      } catch (MalformedXMLException ex) {
        errorAlert();
      }
    });
    buttonData.putIfAbsent("Slow-Down", e -> simulation.slowDown());
    buttonData.put("Pause", e -> simulation.slowDown());
    buttonData.putIfAbsent("Speed-Up", e -> simulation.speedUp());
    buttonData.putIfAbsent("Step", e -> {
      try {
        simulation.step();
      } catch (MalformedXMLException ex) {
        errorAlert();
      }
    });
    buttonData.putIfAbsent("Play", e -> simulation.play());
    buttonData.putIfAbsent("Load File", e -> {
      try {
        loadNewFile();
      } catch (MalformedXMLException ex) {
        errorAlert();
      }
    });
    buttonData.putIfAbsent("Settings", e -> openSettings());
    buttonData.putIfAbsent("Save", e -> {
      try {
        simulation.saveSimulationState("data/testing123.xml");
        //simulation.saveSimulationState(promptUserForFilePath());
      } catch (FileNotFoundException ex) {
        errorAlert();
      }
    });
  }

  /*
  private String promptUserForFilePath() {
    FilePathPromptWindow sw = new FilePathPromptWindow("File Path Chooser");
    sw.start(new Stage());
  }
   */

  private void makeButtons() {
    setupButtons();
    int colIndex = BUTTON_START_INDEX;
    for (String key : buttonData.keySet()) {
      buttonGroup.add(makeButton(key, buttonData.get(key)), colIndex, 0);
      colIndex++;
    }
  }

//  private void makeButtons() throws MalformedXMLException{
//    buttonGroup = new GridPane();
//    int colIndex = BUTTON_START_INDEX;
//    buttonGroup.setHgap(2);
////    buttonGroup.add(makeButton("Home", e -> System.out.println("Home")), colIndex, 0);
////    colIndex ++;
//    buttonGroup.add(makeButton("Reset", e -> {
//      try{
//        reset();
//      } catch (MalformedXMLException ex) {
//        errorAlert();
//      }
//    }), colIndex, 0);
//    colIndex ++;
//    buttonGroup.add(makeButton("Slow-Down", e -> simulation.slowDown()), colIndex, 0);
//    colIndex ++;
//    buttonGroup.add(makeButton("Pause", e -> simulation.pause()), colIndex, 0);
//    colIndex ++;
//    buttonGroup.add(makeButton("Speed-Up", e -> simulation.speedUp()), colIndex, 0);
//    colIndex ++;
//    buttonGroup.add(makeButton("Step", e -> {
//      try {
//        simulation.step();
//      } catch (MalformedXMLException ex) {
//        errorAlert();
//      }
//    }), colIndex, 0);
//    colIndex ++;
//    buttonGroup.add(makeButton("Play", e -> simulation.play()), colIndex, 0);
//    colIndex ++;
//    buttonGroup.add(makeButton("Load File", e -> {
//      try {
//        loadNewFile();
//      } catch (MalformedXMLException ex) {
//        errorAlert();
//      }
//    }), colIndex, 0);
//    colIndex ++;
//    buttonGroup.add(makeButton("Settings", e -> openSettings()), colIndex, 0);
//  }

  private Button makeButton(String title, EventHandler<ActionEvent> action) {
    Button btn = new Button(title);
    btn.setOnAction(action);
    btn.setId(title);
    return btn;
  }

  private void reset() throws MalformedXMLException {
    simulation.pause();
    simulation = null;
    System.gc();
    simulation = makeSimulation(xmlFileName);
    newSimulation();

    // TODO: fix bug with resetting and settings window not changing variable anymore
  }

  private void loadNewFile() throws MalformedXMLException {
    simulation.pause();
    System.gc();
    xmlFileName = getSimulationFile();
    if (xmlFileName != null) {
      simulation = null;
      simulation = makeSimulation(xmlFileName);
      newSimulation();
    }
  }

  private void loadSimulation() throws MalformedXMLException {
    simulation = makeSimulation(xmlFileName);
    windowTitle = simulation.getTitle()  + " by: " + simulation.getAuthor();
  }

  private Simulation makeSimulation(String xmlFileName) throws MalformedXMLException {
    Alert alert = new Alert(AlertType.WARNING, "", ButtonType.OK);
    try {
      return new Simulation(xmlFileName);
    } catch (IOException e) {
      alert.setContentText("IOException - error when loading the XML file");
      alert.show();
      throw new MalformedXMLException("IO Exception caused by calling the Simulation constructor: "
      + e.getMessage());
    } catch (SAXException e) {
      alert.setContentText("SAX Exception - the attempted XML file is malformed. Please fix it"
          + " or try a new file.");
      alert.show();
      throw new MalformedXMLException("SAX Exception caused by calling the Simulation constructor: "
      + e.getMessage());
    } catch (ParserConfigurationException e) {
      alert.setContentText("ParserConfigurationException - the attempted XML file is malformed. Please fix it"
          + " or try a new file.");
      alert.show();
      throw new MalformedXMLException("ParserConfigurationException caused by calling the Simulation constructor: "
          + e.getMessage());
    }
  }
  private void updateStats(int newX, int newY){
    data.getData().add(new XYChart.Data(newX, newY));
  }

  private void openSettings() {
    SettingsWindow sw = new SettingsWindow(windowTitle, simulation);
    sw.start(new Stage());
  }

  private void errorAlert(){
    Alert alert = new Alert(AlertType.WARNING, "", ButtonType.OK);
    alert.setContentText("Exception caused by bad XML file - close the window and reload the"
        + "simulation");
    alert.show();
  }

  /**
   * From IUpdate: method called when the simulation alerts the GUI when the simulation steps
   */
  @Override
  public void simulationUpdate(int newX, int newY) throws MalformedXMLException {
    updateStats(newX, newY);
    updateGUI();
  }

}
