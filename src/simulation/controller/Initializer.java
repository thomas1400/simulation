package simulation.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.ParserConfigurationException;
import simulation.model.Cell;
import org.xml.sax.SAXException;
import simulation.rules.FireRule;
import simulation.rules.GameOfLifeRule;
import simulation.rules.PercolationRule;
import simulation.rules.PredatorPreyRule;
import simulation.rules.RockPaperScissorsRule;
import simulation.rules.Rule;
import simulation.rules.SegregationRule;

class Initializer {
  //neighbor indexes in clockwise order
  private static final int TOP_MIDDLE = 0;
  private static final int TOP_RIGHT = 1;
  private static final int MIDDLE_RIGHT = 2;
  private static final int BOTTOM_RIGHT = 3;
  private static final int BOTTOM_MIDDLE = 4;
  private static final int BOTTOM_LEFT = 5;
  private static final int MIDDLE_LEFT = 6;
  private static final int TOP_LEFT = 7;

  private String myRulesType;
  private double[] myGlobalVars;
  private int myGridWidth;
  private int myGridHeight;
  private boolean myGridIsToroidal;
  private int[][] myInitialStateGrid;
  private Cell[][] myCellGrid;
  private List<Cell> myCells;

  private String mySimulationTitle;
  private String mySimulationAuthor;

  public Initializer(String xmlFileName)
      throws IOException, SAXException, ParserConfigurationException {
    loadConfigFile(xmlFileName);
    fillCellGrid(newRuleClass());
    initializeCellPointers();
  }

  private void loadConfigFile(String file)
      throws ParserConfigurationException, SAXException, IOException {

    XMLReader xmlReader = new XMLReader(file);

    myRulesType = xmlReader.getRule();
    mySimulationTitle = xmlReader.getSimulationTitle();
    mySimulationAuthor = xmlReader.getSimulationAuthor();
    myGlobalVars = xmlReader.getGlobalVars();
    myGridWidth = xmlReader.getGridWidth();
    myGridHeight = xmlReader.getGridHeight();
    myGridIsToroidal = xmlReader.getIsToroidal();
    myInitialStateGrid = xmlReader.getGrid();
  }

  private Rule newRuleClass() {
    Rule myRuleClass = null;
    switch (myRulesType) {
      case "fireRules":
        myRuleClass = new FireRule(myGlobalVars);
        break;
      case "gameOfLifeRules":
        myRuleClass = new GameOfLifeRule(myGlobalVars);
        break;
      case "percolationRules":
        myRuleClass = new PercolationRule(myGlobalVars);
        break;
      case "predatorPreyRules":
        myRuleClass = new PredatorPreyRule(myGlobalVars);
        break;
      case "segregationRules":
        myRuleClass = new SegregationRule(myGlobalVars);
        break;
      case "rockPaperScissorsRules":
        myRuleClass = new RockPaperScissorsRule(myGlobalVars);
        break;
      default:
        System.out.println("Invalid Rules Class");
        System.exit(0);
    }
    return myRuleClass;
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
        assignNeighbors(i, j, myCell);
      }
    }
  }

  private void assignNeighbors(int i, int j, Cell myCell) {
    int index = 0;
    //position indexes, 1-8 from the top middle rotating clockwise, ordered in parsing order
    int[] indices = new int[]{TOP_LEFT, TOP_MIDDLE, TOP_RIGHT, MIDDLE_LEFT, MIDDLE_RIGHT,
        BOTTOM_LEFT, BOTTOM_MIDDLE, BOTTOM_RIGHT};
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

  public Cell[][] getCellGrid() {
    return myCellGrid;
  }

  public List<Cell> getCells() {
    return myCells;
  }

  public String getSimulationTitle() {
    return mySimulationTitle;
  }

  public String getSimulationAuthor() {
    return mySimulationAuthor;
  }
}