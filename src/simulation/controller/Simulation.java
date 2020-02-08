package simulation.controller;


import exceptions.MalformedXMLException;
import simulation.events.IUpdate;
import java.util.List;
import simulation.model.Cell;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import javafx.scene.paint.Color;

import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;


public class Simulation {
  private static final double MS_TO_SECONDS = 1000.0;
  private static final int INITIAL_SIM_SPEED = 1000;

  private String mySimulationTitle;
  private String mySimulationAuthor;
  private int mySimulationSpeed = INITIAL_SIM_SPEED;
  private boolean mySimulationRunning;

  private int myGridWidth;
  private int myGridHeight;

  private Cell[][] myCellGrid;
  private List<Cell> myCells;

  private Timeline timeline;
  private IUpdate listener;


  public Simulation(String xmlFileName)
      throws IOException, SAXException, ParserConfigurationException {

    Initializer myInitializer = new Initializer(xmlFileName);

    mySimulationTitle = myInitializer.getSimulationTitle();
    mySimulationAuthor = myInitializer.getSimulationAuthor();
    myCellGrid = myInitializer.getCellGrid();
    myGridHeight = myCellGrid.length;
    myGridWidth = myCellGrid[0].length;
    myCells = myInitializer.getCells();
  }

  /**
   * Runs the simulation with the set settings
   */
  public void play() {
    mySimulationRunning = true;
    autoStep();
  }

  private int[] getStatsFromGrid(){
    int[] statArray = new int[100];
    for(Cell[] ca : myCellGrid){
      for(Cell c : ca){
        statArray[c.getState()]++;
      }
    }
    return statArray;
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
    for (Cell cell : myCells) {
        cell.getNextState();
    }
    for (Cell cell : myCells) {
      cell.updateState();
    }
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

  /**
   * return 2x2 grid of cellColors
   */
  public Color[][] getColorGrid() {
    Color[][] myColorGrid = new Color[myGridHeight][myGridWidth];
    for (int i = 0; i < myGridHeight; i++) {
      for (int j = 0; j < myGridWidth; j++) {
        myColorGrid[i][j] = myCellGrid[i][j].getColor();
      }
    }
    return myColorGrid;
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
  public void onGridClick(int i, int j){
    myCellGrid[i][j].incrementState();
    try {
      step();
    } catch (MalformedXMLException e) {
      e.printStackTrace();
    }
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

}
