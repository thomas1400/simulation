package simulation.view;

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

  private final int WINDOW_HEIGHT = 512 + 25;
  private final int WINDOW_WIDTH = 512;

  private String windowTitle;
  private Stage mainWindow;
  private GridPane gp;
  private Simulation simulation;
  private GridPane group;
  private String xmlFileName;
  private Stage myStage;


  /**
   * Starts the JavaFX application and handles initial setup method calls
   *
   * @param primaryStage the main stage which the program draws on
   */
  @Override
  public void start(Stage primaryStage)
      throws IOException, SAXException, ParserConfigurationException {
    xmlFileName = getSimulationFile();
    myStage = primaryStage;
    newSimulation();
  }

  private void newSimulation() throws ParserConfigurationException, SAXException, IOException {
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
    File file = fc.showOpenDialog(mainWindow);
    if (file == null) {
      myStage.close();
    }
    return file.toString();
  }

  private void update() {
    setUpWindow(mainWindow);
  }

  private void setUpWindow(Stage primaryStage) {
    mainWindow = primaryStage;
    mainWindow.setTitle(windowTitle);
    Scene gridScene = new Scene(makeMasterGrid(), WINDOW_WIDTH, WINDOW_HEIGHT);
    //gridScene.getStylesheets().add(getClass().getResource("/resources/stylesheet.css").toExternalForm());
    mainWindow.setScene(gridScene);
  }

  private GridPane makeMasterGrid() {
    GridPane mainGrid = new GridPane();
    group = new GridPane();
    gp = new GridPane();
    double cellGap = 1.0;
    gp.setHgap(cellGap);
    gp.setVgap(cellGap);
    makeButtons();
    makeGrid();
    mainGrid.add(group, 0, 0);
    mainGrid.add(gp, 0, 1);
    return mainGrid;
  }

  private void makeButtons() {
    //  members
    Button myHomeButton = new Button("Home");
    group.add(myHomeButton, 0, 0);
    // command for going home

    Button myResetButton = new Button("Reset");
    myResetButton.setOnAction(e -> {
      try {
        reset();
      } catch (ParserConfigurationException | SAXException | IOException ex) {
        ex.printStackTrace();
      }
    });
    group.add(myResetButton, 1, 0);

    Button mySlowDownButton = new Button("Slow Down");
    mySlowDownButton.setOnAction(e -> simulation.slowDown());
    group.add(mySlowDownButton, 2, 0);

    Button myPauseButton = new Button("Pause");
    myPauseButton.setOnAction(e -> simulation.pause());
    group.add(myPauseButton, 3, 0);

    Button mySpeedUpButton = new Button("Speed Up");
    mySpeedUpButton.setOnAction(e -> simulation.speedUp());
    group.add(mySpeedUpButton, 4, 0);

    Button myStepButton = new Button("Step");
    myStepButton.setOnAction(e -> {
      try {
        simulation.step();
      } catch (Exception ex) {
        ex.printStackTrace();
      }
    });
    group.add(myStepButton, 5, 0);

    Button myPlayButton = new Button("Play");
    myPlayButton.setOnAction(e -> simulation.play());
    group.add(myPlayButton, 6, 0);

    Button myLoadConfigButton = new Button("Config");
    myLoadConfigButton.setOnAction(e -> {
      try {
        loadConfig();
      } catch (ParserConfigurationException | SAXException | IOException ex) {
        ex.printStackTrace();
      }
    });
    group.add(myLoadConfigButton, 7, 0);
  }

  private void reset() throws ParserConfigurationException, SAXException, IOException {
    simulation.pause();
    simulation = null;
    System.gc();
    simulation = new Simulation(xmlFileName);
    newSimulation();
  }

  private void loadConfig() throws ParserConfigurationException, SAXException, IOException {
    simulation.pause();
    simulation = null;
    System.gc();
    xmlFileName = getSimulationFile();
    simulation = new Simulation(xmlFileName);
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
        Rectangle rec = new Rectangle();
        rec.setFill(colorGrid[i][j]);
        rec.setWidth(squareSize);
        rec.setHeight(squareSize);
        gp.add(rec, j, i);
      }
    }
  }

  private void loadSimulation() throws ParserConfigurationException, SAXException, IOException {
    simulation = new Simulation(xmlFileName);
    windowTitle = simulation.getTitle();
    //you can use simulation.getColorGrid() to get a Color[][] for each cell's state
  }

  /**
   * From IUpdate: method called when the simulation alerts the GUI when the simulation steps
   */
  @Override
  public void simulationUpdate() {
    update();
  }
}
