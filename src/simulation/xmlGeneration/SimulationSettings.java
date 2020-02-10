package simulation.xmlGeneration;

public class SimulationSettings {

  private String xmlFilePath;
  private String myRuleSelector;
  private String mySimulationTitle;
  private String mySimulationAuthor;
  private int myNumGlobalVars;
  private double[] myGlobalVars;
  private int myGridWidth;
  private int myGridHeight;
  private boolean myGridIsToroidal;
  private int[][] myInitialStateGrid;
  private int myNeighborhoodType;
  private String myGridType;


  public String getFilePath() {
    return xmlFilePath;
  }

  public void setFilePath(String filePath) {
    xmlFilePath = filePath;
  }

  public String getRuleSelector() {
    return myRuleSelector;
  }

  public void setRuleSelector(String ruleSelector) {
    myRuleSelector = ruleSelector;
  }

  public String getSimulationTitle() {
    return mySimulationTitle;
  }

  public void setSimulationTitle(String simulationTitle) {
    mySimulationTitle = simulationTitle;
  }

  public String getSimulationAuthor() {
    return mySimulationAuthor;
  }

  public void setSimulationAuthor(String simulationAuthor) {
    mySimulationAuthor = simulationAuthor;
  }

  public int getNumGlobalVars() {
    return myNumGlobalVars;
  }

  public void setNumGlobalVars(int numGlobalVars) {
    myNumGlobalVars = numGlobalVars;
  }

  public double[] getGlobalVars() {
    return myGlobalVars;
  }

  public void setGlobalVars(double[] globalVars) {
    myGlobalVars = globalVars;
  }

  public int getGridWidth() {
    return myGridWidth;
  }

  public void setGridWidth(int gridWidth) {
    myGridWidth = gridWidth;
  }

  public int getGridHeight() {
    return myGridHeight;
  }

  public void setGridHeight(int gridHeight) {
    myGridHeight = gridHeight;
  }

  public boolean getGridIsToroidal() {
    return myGridIsToroidal;
  }

  public void setGridIsToroidal(boolean gridIsToroidal) {
    myGridIsToroidal = gridIsToroidal;
  }

  public int[][] getInitialStateGrid() {
    return myInitialStateGrid;
  }

  public void setInitialStateGrid(int[][] initialStateGrid) {
    myInitialStateGrid = initialStateGrid;
  }

  public int getNeighborhoodType() {
    return myNeighborhoodType;
  }

  public void setNeighborhoodType(int neighborhoodType) {
    myNeighborhoodType = neighborhoodType;
  }

  public String getGridType() {
    return myGridType;
  }

  public void setGridType(String gridType) {
    myGridType = gridType;
  }

}