package simulation.controller;


import exceptions.MalformedXMLException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;
import simulation.events.IUpdate;
import simulation.model.Grid;
import simulation.rules.Rules;
import simulation.xmlGeneration.SimulationSettings;
import simulation.xmlGeneration.XMLGenerator;


public class Simulation {

  private static final double MS_TO_SECONDS = 1000.0;
  private static final int INITIAL_SIM_SPEED = 1000;
  public static final double MAX_SIM_SPEED = 1 / 4.0;
  public static final int MIN_SIM_SPEED = 4;

  private String mySimulationTitle;
  private String mySimulationAuthor;
  private int mySimulationSpeed = INITIAL_SIM_SPEED;
  private boolean mySimulationRunning;
  private SimulationSettings mySimulationSettings;

  private Grid myGrid;

  private Timeline timeline;
  private IUpdate listener;
  private Rules myRules;


  /**
   * Creates a new Simulation from a file
   * @param xmlFileName the XML file path
   * @throws IOException on malformed XML
   * @throws SAXException on malformed XML
   * @throws ParserConfigurationException on malformed XML
   */
  public Simulation(String xmlFileName)
      throws IOException, SAXException, ParserConfigurationException {

    Initializer myInitializer = new Initializer(xmlFileName);

    mySimulationSettings = myInitializer.getSimulationSettings();
    mySimulationTitle = myInitializer.getSimulationTitle();
    mySimulationAuthor = myInitializer.getSimulationAuthor();
    myGrid = myInitializer.getGrid();
    myRules = myInitializer.getRules();
  }

  /**
   * Runs the simulation with the set settings
   */
  public void play() {
    mySimulationRunning = true;
    autoStep();
  }

  private void autoStep() {
    timeline = new Timeline(
        new KeyFrame(Duration.seconds(mySimulationSpeed / MS_TO_SECONDS), ev -> {
          try {
            step();
          } catch (MalformedXMLException e) {
            listener.errorAlert();
          }
        }));
    timeline.setCycleCount(Animation.INDEFINITE);
    timeline.play();
  }

  /**
   * Step the simulation one generation
   */
  public void step() throws MalformedXMLException {
    myGrid.step();
    alertGUI();
  }

  private void alertGUI() {
    listener.simulationUpdate();
  }

  /**
   * Pause the simulation
   */
  public void pause() {
    if (mySimulationRunning) {
      if (timeline != null) {
        timeline.pause();
      }
      mySimulationRunning = false;
    }
  }

  /**
   * Speed up the simulation by 2x
   */
  public void speedUp() {
    if (timeline != null) {
      if (mySimulationSpeed >= MAX_SIM_SPEED * MS_TO_SECONDS) {
        mySimulationSpeed /= 2;
      }
      timeline.stop();
      autoStep();
    }
  }

  /**
   * Slow down the simulation by 0.5x
   */
  public void slowDown() {
    if (timeline != null) {
      if (mySimulationSpeed <= MIN_SIM_SPEED * MS_TO_SECONDS) {
        mySimulationSpeed *= 2;
      }
      timeline.stop();
      autoStep();
    }
  }

  /**
   * Stops the simulation if it's running
   */
  public void stop() {
    if (timeline != null) {
      timeline.stop();
    }
  }

  @Deprecated
  public void parseSettings(String string) {
    System.out.println(string);
  }

  /**
   * Adds an update listener to this Simulation
   * @param listener implements IUpdate
   */
  public void setListener(IUpdate listener) {
    this.listener = listener;
  }

  /**
   * Method for returning important stats from the simulation
   *
   * @return stats
   */
  @Deprecated
  public int getMaxSizes() {
    return 5;
  }

  /**
   * These getters are used to communicate with GUI the characteristics of the simulation as given
   * from the Initializer
   *
   * @return Values of each specific initialized element
   */
  public String getTitle() {
    return mySimulationTitle;
  }

  /**
   * These getters are used to communicate with GUI the characteristics of the simulation as given
   * from the Initializer
   *
   * @return Values of each specific initialized element
   */
  public String getAuthor() {
    return mySimulationAuthor;
  }

  /**
   * These getters are used to communicate with GUI the characteristics of the simulation as given
   * from the Initializer
   *
   * @return Values of each specific initialized element
   */
  public Pane getGridPane(int MAX_SIZE) {
    return myGrid.getGridPane(MAX_SIZE);
  }

  /**
   * Saves this Simulation's state as an XML file with the given filepath
   * @param filePath the file path for the XML to save
   * @throws FileNotFoundException on invalid file path
   */
  public void saveSimulationState(String filePath) throws FileNotFoundException {
    XMLGenerator myGenerator = new XMLGenerator();
    mySimulationSettings.setFilePath(filePath);
    mySimulationSettings.setRuleSelector(myRules.toString());
    mySimulationSettings.setSimulationTitle(mySimulationTitle);
    mySimulationSettings.setSimulationAuthor(mySimulationAuthor);
    mySimulationSettings.setInitialStateGrid(myGrid.toIntArray());
    myGrid.updateSettings(mySimulationSettings);
    myGenerator.generateXML(mySimulationSettings);
  }

  /**
   * These getters are used to communicate with GUI the characteristics of the simulation as given
   * from the Initializer
   *
   * @return Values of each specific initialized element
   */
  public Map<String, Double[]> getSettings() {
    return myRules.getSettings();
  }

  /**
   * These setters are used to communicate with GUI the characteristics of the simulation as given
   * from the Initializer
   */
  public void setSetting(String name, double value) {
    myRules.setSetting(name, value);
  }

  /**
   * Gets the area of the grid
   * @return the number of cells
   */
  public double getArea() {
    return myGrid.getArea();
  }

  /**
   * Gets counts of each cell type in this grid
   * @return a map of state number to count
   */
  public Map<Integer, Integer> getCellCounts() {
    return myGrid.getCellCounts();
  }

  /**
   * Gets a list of the cell type names in this simulation's rules
   * @return a list of String cell types
   */
  public List<String> getCellTypes() {
    return myRules.getCellTypes();
  }
}
