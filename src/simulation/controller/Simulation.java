package simulation.controller;


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

  private String mySimulationTitle;
  private String mySimulationAuthor;
  private int mySimulationSpeed = 1000;
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

  private void autoStep() {
    timeline = new Timeline(new KeyFrame(Duration.seconds(mySimulationSpeed /1000.0), ev -> step()));
    timeline.setCycleCount(Animation.INDEFINITE);
    timeline.play();
  }

  /**
   * Step the simulation one generation
   */
  public void step() {
    for (Cell cell : myCells) {
        cell.getNextState();
    }
    for (Cell cell : myCells) {
      cell.updateState();
    }
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

  public void setListener(IUpdate listener) {
    this.listener = listener;
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
