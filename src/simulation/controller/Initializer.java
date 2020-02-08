package simulation.controller;

import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;
import simulation.model.Grid;
import simulation.rules.FireRules;
import simulation.rules.GameOfLifeRules;
import simulation.rules.PercolationRules;
import simulation.rules.PredatorPreyRules;
import simulation.rules.RockPaperScissorsRules;
import simulation.rules.Rules;
import simulation.rules.SegregationRules;

class Initializer {

  private String myRulesType;
  private double[] myGlobalVars;

  private boolean myGridIsToroidal;
  private int[][] myInitialStateGrid;
  private Grid myGrid;

  private String mySimulationTitle;
  private String mySimulationAuthor;

  public Initializer(String xmlFileName)
      throws IOException, SAXException, ParserConfigurationException {
    loadConfigFile(xmlFileName);
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

    int[][] tempNeighborhoodShape =
        {   {0, 1, 0},
            {1, -1, 1},
            {0, 1, 0}   };

    myGrid = new Grid(myInitialStateGrid, tempNeighborhoodShape, newRuleClass(), myGridIsToroidal);
  }

  private Rules newRuleClass() {
    Rules myRulesClass = null;
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

  public Grid getGrid() {
    return myGrid;
  }

  public String getSimulationTitle() {
    return mySimulationTitle;
  }

  public String getSimulationAuthor() {
    return mySimulationAuthor;
  }
}
