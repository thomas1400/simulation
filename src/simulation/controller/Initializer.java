package simulation.controller;

import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;
import simulation.model.Grid;
import simulation.model.RectangularGrid;
import simulation.model.TriangularGrid;
import simulation.rules.FireRules;
import simulation.rules.GameOfLifeRules;
import simulation.rules.PercolationRules;
import simulation.rules.PredatorPreyRules;
import simulation.rules.RockPaperScissorsRules;
import simulation.rules.Rules;
import simulation.rules.SegregationRules;
import simulation.xmlGeneration.SimulationSettings;

public class Initializer {

  private static final int[][][] NEIGHBORHOOD_SHAPES = {
      {{1, 1, 1}, {1, -1, 1}, {1, 1, 1}},
      {{0, 1, 0}, {1, -1, 1}, {0, 1, 0}},
      {{1, 0, 1}, {0, -1, 0}, {1, 0, 1}},
      {{0, 1, 1, 1, 0}, {1, 1, -1, 1, 1}, {1, 1, 1, 1, 1}}
  };
  private String myRulesType;
  private Rules myRulesClass;
  private double[] myGlobalVars;
  private String myGridType;
  private boolean myGridIsToroidal;
  private int myNeighborhoodType;
  private SimulationSettings mySimulationSettings;
  private int[][] myInitialStateGrid;
  private Grid myGrid;

  private String mySimulationTitle;
  private String mySimulationAuthor;

  /**
   * Creates a new Initializer
   * @param xmlFileName the file to read from
   * @throws IOException on Malformed XML
   * @throws SAXException on Malformed XML
   * @throws ParserConfigurationException on Malformed XML
   */
  public Initializer(String xmlFileName)
      throws IOException, SAXException, ParserConfigurationException {
    loadConfigFile(xmlFileName);
    mySimulationSettings = new SimulationSettings();

    mySimulationSettings.setNumGlobalVars(myGlobalVars.length);
    mySimulationSettings.setGlobalVars(myGlobalVars);
    mySimulationSettings.setNeighborhoodType(myNeighborhoodType);
    mySimulationSettings.setGridType(myGridType);
  }

  private void loadConfigFile(String file)
      throws ParserConfigurationException, SAXException, IOException {

    XMLReader xmlReader = new XMLReader(file);

    myRulesType = xmlReader.getRule();
    mySimulationTitle = xmlReader.getSimulationTitle();
    mySimulationAuthor = xmlReader.getSimulationAuthor();
    myGlobalVars = xmlReader.getGlobalVars();
    myGridIsToroidal = xmlReader.getIsToroidal();
    myInitialStateGrid = xmlReader.getGrid();
    myNeighborhoodType = xmlReader.getNeighborhoodType();
    myGridType = xmlReader.getGridType();

    makeMyGrid();
  }

  private void makeMyGrid() {
    switch (myGridType.toLowerCase()) {
      case "rectangular":
        myGrid = new RectangularGrid(
            myInitialStateGrid, NEIGHBORHOOD_SHAPES[myNeighborhoodType - 1],
            newRuleClass(), myGridIsToroidal);
        break;
      case "triangular":
        myGrid = new TriangularGrid(
            myInitialStateGrid, NEIGHBORHOOD_SHAPES[myNeighborhoodType - 1],
            newRuleClass(), myGridIsToroidal);
    }
  }

  private Rules newRuleClass() {
    switch (myRulesType) {
      case "fireRules":
        myRulesClass = new FireRules(myGlobalVars);
        break;
      case "gameOfLifeRules":
        myRulesClass = new GameOfLifeRules(myGlobalVars);
        break;
      case "percolationRules":
        myRulesClass = new PercolationRules(myGlobalVars);
        break;
      case "predatorPreyRules":
        myRulesClass = new PredatorPreyRules(myGlobalVars);
        break;
      case "segregationRules":
        myRulesClass = new SegregationRules(myGlobalVars);
        break;
      case "rockPaperScissorsRules":
        myRulesClass = new RockPaperScissorsRules(myGlobalVars);
        break;
      default:
        System.out.println("Invalid Rules Class");
        System.exit(0);
    }
    return myRulesClass;
  }

  /**
   * Gets the grid for this Initializer
   * @return the Grid read from an XML
   */
  public Grid getGrid() {
    return myGrid;
  }

  /**
   * Gets the simulation title for this Initializer
   * @return the title read from an XML
   */
  public String getSimulationTitle() {
    return mySimulationTitle;
  }

  /**
   * Gets the simulation author for this Initializer
   * @return the author read from an XML
   */
  public String getSimulationAuthor() {
    return mySimulationAuthor;
  }

  /**
   * Gets the rules for this Initializer's simulation
   * @return the Rules corresponding to the type read from an XML
   */
  public Rules getRules() {
    return myRulesClass;
  }

  /**
   * Gets a SimulationSettings object for this instance's simulation
   * @return a new SimulationSettings object with all settings
   */
  public SimulationSettings getSimulationSettings() {
    return mySimulationSettings;
  }
}
