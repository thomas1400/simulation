package controller;


import events.IUpdate;
import java.util.ArrayList;
import java.util.List;
import model.Cell;

import rules.Rule;

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

  private String myRuleSelector;
  private double[] myGlobalVars;
  private boolean myGridIsToroidal;
  private int[][] myInitialStateGrid;

  private int myGridWidth;
  private int myGridHeight;

  private Cell[][] myCellGrid;
  private List<Cell> myCells;

  private Timeline timeline;
  private IUpdate listener;

  public Simulation(String xmlFileName)
      throws IOException, SAXException, ParserConfigurationException {
    loadConfigFile(xmlFileName);
    CellInitializer myInitializer = new CellInitializer(myRuleSelector, myGlobalVars);
    Rule myRuleClass = myInitializer.setNewRulesClass();
    fillCellGrid(myRuleClass);
    initializeCellPointers();
  }

  private void loadConfigFile(String file)
      throws ParserConfigurationException, SAXException, IOException {
    XMLReader xmlReader = new XMLReader(file);

    myRuleSelector = xmlReader.getRule();
    mySimulationTitle = xmlReader.getSimulationTitle();
    mySimulationAuthor = xmlReader.getSimulationAuthor();
    myGlobalVars = xmlReader.getGlobalVars();
    myGridWidth = xmlReader.getGridWidth();
    myGridHeight = xmlReader.getGridHeight();
    myGridIsToroidal = xmlReader.getIsToroidal();
    myInitialStateGrid = xmlReader.getGrid();
  }

  private void fillCellGrid(Rule ruleType) {
    myCellGrid = new Cell[myGridHeight][myGridWidth];
    myCells = new ArrayList<>();
    for (int i = 0; i < myGridHeight; i++) {
      for (int j = 0; j < myGridWidth; j++) {
        int initialCellState = myInitialStateGrid[i][j];
        Cell newCell = new Cell(ruleType, initialCellState);
        myCellGrid[i][j] = newCell;
        myCells.add(newCell);
      }
    }
  }

  private void initializeCellPointers() {
    for (int i = 0; i < myGridHeight; i++) {
      for (int j = 0; j < myGridWidth; j++) {
        Cell myCell = myCellGrid[i][j];
        int index = 0;
        int[] indices = new int[]{7, 0, 1, 6, 2, 5, 4, 3};
        for (int io = -1; io <= 1; io++) {
          for (int jo = -1; jo <= 1; jo++) {
            if (gridCoordinatesInBounds(i + io, j + jo) && (io != 0 || jo != 0)) {
              myCell.setNeighbor(indices[index], myCellGrid[i + io][j + jo]);
            } else if (myGridIsToroidal) {
              int[] newCoords = normalizeOverflowingCoordinates(i + io, j + jo);
              myCell.setNeighbor(indices[index], myCellGrid[newCoords[0]][newCoords[1]]);
            }
            if (jo != 0 || io != 0) {
              index += 1;
            }
          }
        }
      }
    }
  }

  private int[] normalizeOverflowingCoordinates(int i, int j) {
    if (i < 0) {
      i += myGridHeight;
    }
    if (j < 0) {
      j += myGridWidth;
    }
    if (i >= myGridHeight) {
      i -= myGridHeight;
    }
    if (j >= myGridWidth) {
      j -= myGridWidth;
    }

    return new int[]{i, j};
  }

  private boolean gridCoordinatesInBounds(int y, int x) {
    return (y >= 0 && y < myGridHeight && x >= 0 && x < myGridWidth);
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
   * return 2x2 grid of cellStates
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

  public String getTitle() {
    return mySimulationTitle;
  }

  public String getAuthor() {
    return mySimulationAuthor;
  }

}
