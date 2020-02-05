package controller;

public class XMLContent {
  private String myRule;
  private String mySimulationTitle;
  private String mySimulationAuthor;
  private int myNumGlobalVars;
  private double[] myGlobalVars;
  private int myGridWidth;
  private int myGridHeight;
  private boolean myIsToroidal;
  private int[][] myGrid;

  public XMLContent(String rule, String simulationTitle, String simulationAuthor, int numGlobalVars,
      double[] globalVars, int gridWidth, int gridHeight, boolean isToroidal, int[][] grid) {
    myRule = rule;
    mySimulationTitle = simulationTitle;
    mySimulationAuthor = simulationAuthor;
    myNumGlobalVars = numGlobalVars;
    myGlobalVars = globalVars;
    myGridWidth = gridWidth;
    myGridHeight = gridHeight;
    myIsToroidal = isToroidal;
    myGrid = grid;
  }

  public String getRule() {
    return myRule;
  }

  public String getSimulationTitle() {
    return mySimulationTitle;
  }

  public String getSimulationAuthor() {
    return mySimulationAuthor;
  }

  public int getNumGlobalVars() {
    return myNumGlobalVars;
  }

  public double[] getGlobalVars() {
    return myGlobalVars;
  }

  public int getGridWidth() {
    return myGridWidth;
  }

  public int getGridHeight() {
    return myGridHeight;
  }

  public boolean getIsToroidal() {
    return myIsToroidal;
  }

  public int[][] getGrid() {
    return myGrid;
  }
}
