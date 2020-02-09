package simulation.view;

import exceptions.MalformedXMLException;
import java.util.Collections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import simulation.controller.Simulation;
import simulation.events.IUpdate;
import java.io.File;
import java.io.IOException;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

/**
 * The 'simulation.view' for the Simulation project. Handles all visual aspects of the project, including
 * updating the main window and listening to the simulation simulation.controller
 */
public class GUI extends Application implements IUpdate {

  private static final int WINDOW_HEIGHT = 625 + 25;
  private static final int WINDOW_WIDTH = 512;
  private static final int BUTTON_START_INDEX = 0;

  private String windowTitle;
  private Stage mainWindow;
  private GridPane gridGroup;
  private Simulation simulation;
  private GridPane buttonGroup;
  private GridPane graphGroup;
  private GridPane settingGroup;
  private String xmlFileName;
  private Stage myStage;
  private XYChart.Series data;

  /**
   * Starts the JavaFX application and handles initial setup method calls
   *
   * @param primaryStage the main stage which the program draws on
   */
  @Override
  public void start(Stage primaryStage) {
    data = new XYChart.Series();
    myStage = primaryStage;
    try {
      xmlFileName = getSimulationFile();
      newSimulation();
    } catch (Exception e) {
      e.printStackTrace(); // TODO : remove
      myStage.close();
    }
  }

  private void newSimulation() throws MalformedXMLException {
    loadSimulation();
    setUpWindow(myStage);
    simulation.setListener(this);
    mainWindow.show();
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
      // TODO: Potentially a better way to handle this
      return null;
    }
    return file.toString();
  }

  private void updateGUI() throws MalformedXMLException {
    setUpWindow(mainWindow);
  }

  private void setUpWindow(Stage primaryStage) throws MalformedXMLException {
    mainWindow = primaryStage;
    primaryStage.setOnCloseRequest(event -> simulation.stop());
    mainWindow.setTitle(windowTitle);
    Scene gridScene = new Scene(makeMasterGrid(), WINDOW_WIDTH, WINDOW_HEIGHT);
    //gridScene.getStylesheets().add(getClass().getResource("/resources/stylesheet.css").toExternalForm());
    mainWindow.setScene(gridScene);
  }

  private GridPane makeMasterGrid() throws MalformedXMLException {
    GridPane mainGrid = new GridPane();
    buttonGroup = new GridPane();
    gridGroup = new GridPane();
    graphGroup = new GridPane();
    settingGroup = new GridPane();
    double cellGap = 1.0;
    gridGroup.setHgap(cellGap);
    gridGroup.setVgap(cellGap);
    makeButtons();
    makeGrid();
    makeGraphs();
    makeSetting();
    mainGrid.add(buttonGroup, 0, 0);
    mainGrid.add(gridGroup, 0, 1);
    mainGrid.add(graphGroup,0,2);
    mainGrid.add(settingGroup,1,0);
    return mainGrid;
  }

  private void makeSetting(){
    Label label = new Label("Enter Setting:");
    TextField textField = new TextField();
    Button confirmBtn = new Button("Confirm");
    confirmBtn.setOnAction(e -> simulation.parseSettings(textField.getText()));
    HBox hb = new HBox();
    hb.getChildren().addAll(label, textField, confirmBtn);
    hb.setSpacing(10);
    settingGroup.add(hb, 0,0);
  }

  private void makeGraphs(){
    simulation.getMaxSizes();
    // TODO: Get max values, so you don't have to graph all the way up to 1500
    //graphGroup = new GridPane();
    NumberAxis xAxis = new NumberAxis(0, 1500, 1);
    xAxis.setLabel(simulation.getTitle());
    NumberAxis yAxis = new NumberAxis(0, 1500, 1);
    yAxis.setLabel(simulation.getTitle());
    LineChart lineChart = new LineChart(xAxis, yAxis);
    lineChart.getData().add(data);
    graphGroup.add(lineChart, 0, 0);
  }
  private void makeButtons() throws MalformedXMLException{
    int colIndex = BUTTON_START_INDEX;
    buttonGroup.add(makeButton("Home", e -> System.out.println("Home")), colIndex, 0);
    colIndex ++;
    buttonGroup.add(makeButton("Reset", e -> {
      try{
        reset();
      } catch (MalformedXMLException ex) {
        // TODO: Figure out lambda exceptions
        ex.printStackTrace();
      }
    }), colIndex, 0);
    colIndex ++;
    buttonGroup.add(makeButton("Slow Down", e -> simulation.slowDown()), colIndex, 0);
    colIndex ++;
    buttonGroup.add(makeButton("Pause", e -> simulation.pause()), colIndex, 0);
    colIndex ++;
    buttonGroup.add(makeButton("Speed Up", e -> simulation.speedUp()), colIndex, 0);
    colIndex ++;
    buttonGroup.add(makeButton("Step", e -> {
      try {
        simulation.step();
      } catch (Exception ex) {
        ex.printStackTrace();
      }
    }), colIndex, 0);
    colIndex ++;
    buttonGroup.add(makeButton("Play", e -> simulation.play()), colIndex, 0);
    colIndex ++;
    buttonGroup.add(makeButton("Config", e -> {
      try {
        loadConfig();
      } catch (MalformedXMLException ex) {
        ex.printStackTrace();
      }
    }), colIndex, 0);
  }
  private Button makeButton(String title, EventHandler<ActionEvent> action) throws MalformedXMLException{
    Button btn = new Button(title);
    btn.setOnAction(action);
    return btn;
  }
  private void reset() throws MalformedXMLException {
    simulation.pause();
    simulation = null;
    System.gc();
    simulation = makeSimulation(xmlFileName);
    newSimulation();
  }

  private void loadConfig() throws MalformedXMLException {
    simulation.pause();
    simulation = null;
    System.gc();
    xmlFileName = getSimulationFile();
    simulation = makeSimulation(xmlFileName);
    newSimulation();
  }

  private void makeGrid() {
    Color[][] colorGrid = simulation.getColorGrid();
    int width = colorGrid[0].length;
    int height = colorGrid.length;
    int largestDimension = Math.max(width, height);
    int squareSize = WINDOW_WIDTH / largestDimension - 1;
    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        int iTemp = i;
        int jTemp = j;
        Rectangle rec = new Rectangle();
        rec.setFill(colorGrid[i][j]);
        rec.setWidth(squareSize);
        rec.setHeight(squareSize);
        rec.setOnMouseClicked(e -> {
          try {
            simulation.onGridClick(iTemp, jTemp);
          } catch (MalformedXMLException ex) {
            ex.printStackTrace();
          }
        });
        gridGroup.add(rec, j, i);
      }
    }
  }

  private void loadSimulation() throws MalformedXMLException {
    simulation = makeSimulation(xmlFileName);
    windowTitle = simulation.getTitle();
    //you can use simulation.getColorGrid() to get a Color[][] for each cell's state
  }

  private Simulation makeSimulation(String xmlFileName) throws MalformedXMLException {
    Alert alert = new Alert(AlertType.WARNING, "", ButtonType.OK);
    try {
      Simulation simulation = new Simulation(xmlFileName);
      return simulation;
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
  /**
   * From IUpdate: method called when the simulation alerts the GUI when the simulation steps
   */
  @Override
  public void simulationUpdate(int newX, int newY) throws MalformedXMLException {
    updateStats(newX, newY);
    updateGUI();
  }

}
