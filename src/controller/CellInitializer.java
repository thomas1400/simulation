package controller;

import rules.FireRule;
import rules.GameOfLifeRule;
import rules.PercolationRule;
import rules.PredatorPreyRule;
import rules.Rule;
import rules.SegregationRule;

public class CellInitializer {
  private String myRulesType;
  private double[] myGlobalVars;

  public CellInitializer(String rulesType, double[] globalVars) {
    myRulesType = rulesType;
    myGlobalVars = globalVars;
  }

  public Rule setNewRulesClass() {
    Rule myRuleClass = null;
    switch (myRulesType) {
      case "fireRules":
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
    return myRuleClass;
  }
}
