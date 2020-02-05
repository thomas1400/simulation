package controller;

import events.IUpdate;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import model.Cell;
import javafx.scene.paint.Color;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;
import rules.FireRule;
import rules.GameOfLifeRule;
import rules.PercolationRule;
import rules.PredatorPreyRule;
import rules.Rule;
import rules.SegregationRule;

public class Simulation {

  public Rule myRuleClass;

  public int simulationSpeed = 1000;
  public boolean simulationRunning;

  private String myRuleSelector;
  private String mySimulationTitle;
  private String mySimulationAuthor;
  private double[] myGlobalVars;
  private int myGridWidth;
  private int myGridHeight;
  private boolean myGridIsToroidal;
  private int[][] myInitialStateGrid;
  private Cell[][] myCellGrid;
  private Timeline timeline;

  private IUpdate listener;

  public Simulation(String xmlFileName)
      throws IOException, SAXException, ParserConfigurationException {
    loadConfigFile(xmlFileName);
    setNewRulesClass(myRuleSelector, myGlobalVars);
    fillCellGrid(myRuleClass);
    initializeCellPointers();
    //printGridStates();
  }

  /**
   * Runs the simulation with the set settings
   */
  public void play() {
    simulationRunning = true;
    autoStep();
  }

  private void autoStep() {
    timeline = new Timeline(new KeyFrame(Duration.seconds(simulationSpeed / 1000.0), ev -> step()));
    timeline.setCycleCount(Animation.INDEFINITE);
    timeline.play();
  }

  /**
   * Step the simulation one generation
   */
  public void step() {
    for (Cell[] cells : myCellGrid) {
      for (Cell cell : cells) {
        cell.getNextState();
      }
    }
    for (Cell[] cells : myCellGrid) {
      for (Cell cell : cells) {
        cell.updateState();
      }
    }
    alertGUI();
    //printGridStates();
  }

  /**
   * Pause the simulation
   */
  public void pause() {
    if (simulationRunning) {
      timeline.pause();
    }
    simulationRunning = false;
  }

  /**
   * Speed up the simulation by 2x
   */
  public void speedUp() {
    simulationSpeed /= 2;
    timeline.stop();
    autoStep();
  }

  /**
   * Slow down the simulation by 0.5x
   */
  public void slowDown() {
    simulationSpeed *= 2;
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

//  public void printGridStates() {
//    for (Cell[] cells : myCellGrid) {
//      System.out.println();
//      for (Cell cell : cells) {
//        System.out.print(cell.getState() + " ");
//      }
//    }
//    System.out.println();
//  }

  public String getTitle() {
    return mySimulationTitle;
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

  private void setNewRulesClass(String rulesType, double[] myGlobalVars) {
    switch (rulesType) {
      case "fireRules":
        //first global variable should be fire spread probability
        myRuleClass = new FireRule();
        break;
      case "gameOfLifeRules":
        myRuleClass = new GameOfLifeRule();
        break;
      case "percolationRules":
        myRuleClass = new PercolationRule();
        break;
      case "predatorPreyRules":
        myRuleClass = new PredatorPreyRule();
        break;
      case "segregationRules":
        myRuleClass = new SegregationRule();
        break;
      default:
        System.out.println("Invalid Rules Class");
        System.exit(0);
    }
    myRuleClass.setGlobalVariables(myGlobalVars);
  }

  private void fillCellGrid(Rule ruleType) {
    myCellGrid = new Cell[myGridHeight][myGridWidth];
    for (int i = 0; i < myGridHeight; i++) {
      for (int j = 0; j < myGridWidth; j++) {
        int initialCellState = myInitialStateGrid[i][j];
        Cell newCell = new Cell(ruleType, initialCellState);
        myCellGrid[i][j] = newCell;
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
    return (0 <= y && y < myGridHeight && 0 <= x && x < myGridWidth);
  }

  public void setListener(IUpdate listener) {
    this.listener = listener;
  }

  private void alertGUI() {
    listener.simulationUpdate();
  }
}
