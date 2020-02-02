package controller;

import events.IUpdate;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import model.Cell;
import javafx.scene.paint.Color;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.traversal.DocumentTraversal;
import org.w3c.dom.traversal.NodeFilter;
import org.w3c.dom.traversal.NodeIterator;
import org.xml.sax.SAXException;
import rules.FireRules;
import rules.GameOfLifeRules;
import rules.PercolationRules;
import rules.PredatorPreyRules;
import rules.Rules;
import rules.SegregationRules;

public class Simulation {

  public Rules myRuleClass;

  public int simulationSpeed = 1000;
  public boolean simulationRunning;

  private String myRuleSelector;
  private String mySimulationTitle;
  private double[] myGlobalVars;
  private int myGridWidth;
  private int myGridHeight;
  private int[][] myInitialStateGrid;
  private Cell[][] myCellGrid;

  private IUpdate listener;

  public Simulation(String xmlFileName)
      throws IOException, SAXException, ParserConfigurationException {
    loadConfigFile(xmlFileName);
    setNewRulesClass(myRuleSelector, myGlobalVars);
    fillCellGrid(myRuleClass);
    initializeCellPointers();
    printGridStates();
  }

  /**
   * Runs the simulation with the set settings
   */
  public void play() {
    simulationRunning = true;
    autoStep();
  }

  private void autoStep() {
    Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), ev -> {
      step();
    }));
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
    printGridStates();
  }

  /**
   * Pause the simulation
   */
  public void pause() {
    simulationRunning = false;
  }

  /**
   * Speed up the simulation by 2x
   */
  public void speedUp() {
    simulationSpeed *= 2;
  }

  /**
   * Slow down the simulation by 0.5x
   */
  public void slowDown() {
    simulationSpeed /= 2;
  }

  /**
   * return 2x2 grid of cellStates
   */
  public Color[][] getColorGrid() {
    Color[][] myColorGrid = new Color[myCellGrid.length][myCellGrid.length];
    for (int i = 0; i < myCellGrid.length; i++) {
      for (int j = 0; j < myCellGrid[i].length; j++) {
        myColorGrid[i][j] = myCellGrid[i][j].getColor();
      }
    }
    return myColorGrid;
  }

  public void printGridStates() {
    for (int i = 0; i < myCellGrid.length; i++) {
      System.out.println();
      for (int j = 0; j < myCellGrid[i].length; j++) {
        System.out.print(myCellGrid[i][j].getState() + " ");
      }
    }
    System.out.println();
  }

  public String getTitle() {
    return mySimulationTitle;
  }

  private void wait(int ms) {
    try {
      Thread.sleep(ms);
    } catch (InterruptedException ex) {
      Thread.currentThread().interrupt();
    }
  }

  private void loadConfigFile(String file)
      throws ParserConfigurationException, SAXException, IOException {
    NodeIterator iterator = getNodeIterator(file);

    myRuleSelector = getRules(iterator);
    mySimulationTitle = getSimulationName(iterator);

    myGlobalVars = getGlobalVars(iterator);

    myGridWidth = getGridWidth(iterator);
    myGridHeight = getGridHeight(iterator);

    myInitialStateGrid = getInitialStateGrid(iterator);
  }

  private int[][] getInitialStateGrid(NodeIterator iterator) {
    int[][] initialStateGrid = new int[myGridHeight][myGridWidth];
    for (int i = 0; i < myGridHeight; i++) {
      //Get string of values corresponding to a row and store in an array
      String[] rowArray = iterator.nextNode().getTextContent().trim().split(" ");
      skipBlankLine(iterator);
      for (int j = 0; j < myGridWidth; j++) {
        initialStateGrid[i][j] = Integer.parseInt(rowArray[j]);
      }
    }
    return initialStateGrid;
  }

  private int getGridHeight(NodeIterator iterator) {
    int gridHeight = Integer.parseInt(iterator.nextNode().getTextContent().trim());
    skipBlankLine(iterator);
    skipBlankLine(iterator);
    return gridHeight;
  }

  private int getGridWidth(NodeIterator iterator) {
    int gridWidth = Integer.parseInt(iterator.nextNode().getTextContent().trim());
    skipBlankLine(iterator);
    return gridWidth;
  }

  private double[] getGlobalVars(NodeIterator iterator) {
    int numGlobalVars = Integer.parseInt(iterator.nextNode().getTextContent().trim());
    skipBlankLine(iterator);
    skipBlankLine(iterator);
    double[] globalVars = new double[numGlobalVars];
    for (int i = 0; i < numGlobalVars; i++) {
      globalVars[i] = Double.parseDouble(iterator.nextNode().getTextContent().trim());
      skipBlankLine(iterator);
    }
    skipBlankLine(iterator);
    return globalVars;
  }

  private String getRules(NodeIterator iterator) {
    skipBlankLine(iterator);
    String ruleSelector = iterator.nextNode().getTextContent().trim();
    skipBlankLine(iterator);
    return ruleSelector;
  }

  private String getSimulationName(NodeIterator iterator) {
    String simulationTitle = iterator.nextNode().getTextContent().trim();
    skipBlankLine(iterator);
    return simulationTitle;
  }

  private void skipBlankLine(NodeIterator iterator) {
    iterator.nextNode();
  }

  private NodeIterator getNodeIterator(String file)
      throws ParserConfigurationException, SAXException, IOException {
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    DocumentBuilder loader = factory.newDocumentBuilder();
    Document document = loader.parse(file);
    DocumentTraversal traversal = (DocumentTraversal) document;
    return traversal.createNodeIterator(document.getDocumentElement(), NodeFilter.SHOW_TEXT,
        null, true);
  }

  private void setNewRulesClass(String rulesType, double[] myGlobalVars) {
    switch (rulesType) {
      case "fireRules":
        //first global variable should be fire spread probability
        myRuleClass = new FireRules(myGlobalVars[0]);
        break;
      case "gameOfLifeRules":
        myRuleClass = new GameOfLifeRules();
        break;
      case "percolationRules":
        myRuleClass = new PercolationRules();
        break;
      case "predatorPreyRules":
        myRuleClass = new PredatorPreyRules();
        break;
      case "segregationRules":
        myRuleClass = new SegregationRules();
        break;
      default:
        System.out.println("Invalid Rules Class");
        System.exit(0);
    }
  }

  private void fillCellGrid(Rules ruleType) {
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
        //check that cell isn't in top row
        if (i != 0) {
          myCell.setNeighbor(0, myCellGrid[i - 1][j]);
        }
        //check that cell isn't in bottom row
        if (i < myGridHeight - 1) {
          myCell.setNeighbor(4, myCellGrid[i + 1][j]);
        }
        //check that cell isn't in far right column
        if (j < myGridWidth - 1) {
          myCell.setNeighbor(2, myCellGrid[i][j + 1]);
        }
        //check that cell isn't in far left column
        if (j != 0) {
          myCell.setNeighbor(6, myCellGrid[i][j - 1]);
        }
      }
    }
  }
  public void setListener(IUpdate listener){
    this.listener = listener;
  }

  private void alertGUI(){
    listener.simulationUpdate();
  }
}
