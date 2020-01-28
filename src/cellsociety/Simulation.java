package cellsociety;

import java.io.IOException;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.traversal.DocumentTraversal;
import org.w3c.dom.traversal.NodeFilter;
import org.w3c.dom.traversal.NodeIterator;
import org.xml.sax.SAXException;

public class Simulation {

  public ArrayList<Cell> myCells = new ArrayList<>();
  public Rules myRuleClass;

  private int simulationSpeed = 1000;
  private boolean simulationRunning;

  private String myRuleSelector;
  private String mySimulationTitle;
  private double[] myGlobalVars;
  private int myGridWidth;
  private int myGridHeight;
  private int[][] myCurrentStateGrid;
  private Cell[][] myCellGrid;

  public Simulation(String xmlFileName)
      throws IOException, SAXException, ParserConfigurationException {
    loadConfigFile(xmlFileName);
    setNewRulesClass(myRuleSelector);
    fillCellGrid(myRuleClass);
    initializeCellPointers();
  }

  /**
   * Runs the simulation with the set settings
   */
  public void play() {
    simulationRunning = true;
    while (simulationRunning) {
      step();
      wait(simulationSpeed);
    }
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
  public int[][] getCellStates() {
    for (int i = 0; i < myCurrentStateGrid.length; i++) {
      for (int j = 0; j < myCurrentStateGrid[i].length; j++) {
        myCurrentStateGrid[i][j] = myCellGrid[i][j].myState;
      }
    }
    return myCurrentStateGrid;
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

    myCurrentStateGrid = getInitialStateGrid(iterator);
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

  private void setNewRulesClass(String rulesType) {
    switch (rulesType) {
      case "fireRules":
        myRuleClass = new fireRules();
        break;
      case "gameOfLifeRules":
        myRuleClass = new gameOfLifeRules();
        break;
      case "percolationRules":
        myRuleClass = new percolationRules();
        break;
      case "predatorPreyRules":
        myRuleClass = new predatorPreyRules();
        break;
      case "segregationRules":
        myRuleClass = new segregationRules();
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
        int initialCellState = myCurrentStateGrid[i][j];
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
        //check that cell isn't in top row
        if (i != 0) {
          myCell.up = myCellGrid[i - 1][j];
        }
        //check that cell isn't in bottom row
        if (i < myGridHeight - 1) {
          myCell.down = myCellGrid[i + 1][j];
        }
        //check that cell isn't in far right column
        if (j < myGridWidth - 1) {
          myCell.right = myCellGrid[i][j + 1];
        }
        //check that cell isn't in far left column
        if (j != 0) {
          myCell.left = myCellGrid[i][j - 1];
        }
      }
    }
  }
}
