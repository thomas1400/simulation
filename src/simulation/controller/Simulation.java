package simulation.controller;


import exceptions.MalformedXMLException;
import java.util.Map;
import javafx.scene.layout.Pane;
import simulation.events.IUpdate;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;
import simulation.model.Grid;
import simulation.xmlGeneration.XMLGenerator;
import simulation.rules.Rules;


public class Simulation {
  private static final double MS_TO_SECONDS = 1000.0;
  private static final int INITIAL_SIM_SPEED = 1000;

  private String mySimulationTitle;
  private String mySimulationAuthor;
  private int mySimulationSpeed = INITIAL_SIM_SPEED;
  private boolean mySimulationRunning;

  private Grid myGrid;

  private Timeline timeline;
  private IUpdate listener;
  private Rules myRules;


  public Simulation(String xmlFileName)
      throws IOException, SAXException, ParserConfigurationException {

    Initializer myInitializer = new Initializer(xmlFileName);

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

  private int[] getStatsFromGrid(){
    return myGrid.getStats();
  }

  private void autoStep() {
    timeline = new Timeline(new KeyFrame(Duration.seconds(mySimulationSpeed / MS_TO_SECONDS), ev -> {
      try {
        step();
      } catch (MalformedXMLException e) {
        e.printStackTrace();
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

  private void alertGUI() throws MalformedXMLException {
    int[] temp = getStatsFromGrid();
    listener.simulationUpdate(temp[0], temp[1]);
  }

  /**
   * Pause the simulation
   */
  public void pause() {
    if (mySimulationRunning) {
      timeline.pause();
    }
    mySimulationRunning = false;
  }

  /**
   * Speed up the simulation by 2x
   */
  public void speedUp() {
    mySimulationSpeed /= 2;
    timeline.stop();
    autoStep();
  }

  /**
   * Slow down the simulation by 0.5x
   */
  public void slowDown() {
    mySimulationSpeed *= 2;
    timeline.stop();
    autoStep();
  }

  public void stop() {
    if (timeline != null) {
      timeline.stop();
    }
  }

  public void parseSettings(String string){
    System.out.println(string);
  }

  public void setListener(IUpdate listener) {
    this.listener = listener;
  }

  /**
   * Method for returning important stats from the simulation
   * TODO: Implement
   * @return stats
   */
  public int getMaxSizes(){
    return 5;
  }

  /**
   * These getters are used to communicate with GUI the characteristics of the simulation
   * as given from the Initializer
   * @return Values of each specific initialized element
   */
  public String getTitle() {
    return mySimulationTitle;
  }

  public String getAuthor() {
    return mySimulationAuthor;
  }

  public Pane getGridPane(int MAX_SIZE) {
    return myGrid.getGridPane(MAX_SIZE);
  }

  public void saveSimulationState() {
    XMLGenerator myGenerator = new XMLGenerator();
    System.out.println(myGrid.toTxt());
  }

  public Map<String, Double[]> getSettings() {
    return myRules.getSettings();
  }

  public void setSetting(String name, double value) {
    myRules.setSetting(name, value);
  }
}
