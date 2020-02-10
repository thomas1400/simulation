package simulation.xmlGeneration;

public class SimulationSettings {

  private  String xmlFilePath;
  private  String myRuleSelector;
  private  String mySimulationTitle;
  private  String mySimulationAuthor;
  private  int myNumGlobalVars;
  private  double[] myGlobalVars;
  private  int myGridWidth;
  private  int myGridHeight;
  private  boolean myGridIsToroidal;
  private  int[][] myInitialStateGrid;
  private  int myNeighborhoodType;
  private  String myGridType;


  public  String getFilePath() {
    return xmlFilePath;
  }

  public  String getRuleSelector() {
    return myRuleSelector;
  }

  public  String getSimulationTitle() {
    return mySimulationTitle;
  }

  public  String getSimulationAuthor() {
    return mySimulationAuthor;
  }

  public  int getNumGlobalVars() {
    return myNumGlobalVars;
  }

  public  double[] getGlobalVars() {
    return myGlobalVars;
  }

  public  int getGridWidth() {
    return myGridWidth;
  }

  public  int getGridHeight() {
    return myGridHeight;
  }

  public  boolean getGridIsToroidal() {
    return myGridIsToroidal;
  }

  public  int[][] getInitialStateGrid() {
    return myInitialStateGrid;
  }

  public  int getNeighborhoodType() {
    return myNeighborhoodType;
  }

  public  String getGridType() {
    return myGridType;
  }

  public  void setFilePath(String xmlFilePath) {
    xmlFilePath = xmlFilePath;
  }

  public  void setRuleSelector(String ruleSelector) {
    myRuleSelector = ruleSelector;
  }

  public  void setSimulationTitle(String simulationTitle) {
    mySimulationTitle = simulationTitle;
  }

  public  void setSimulationAuthor(String simulationAuthor) {
    mySimulationAuthor = simulationAuthor;
  }

  public  void setNumGlobalVars(int numGlobalVars) {
    myNumGlobalVars = numGlobalVars;
  }

  public  void setGlobalVars(double[] globalVars) {
    myGlobalVars = globalVars;
  }

  public  void setGridWidth(int gridWidth) {
    myGridWidth = gridWidth;
  }

  public  void setGridHeight(int gridHeight) {
    myGridHeight = gridHeight;
  }

  public  void setGridIsToroidal(boolean gridIsToroidal) {
    myGridIsToroidal = gridIsToroidal;
  }

  public  void setInitialStateGrid(int[][] initialStateGrid) {
    myInitialStateGrid = initialStateGrid;
  }

  public  void setNeighborhoodType(int neighborhoodType) {
    myNeighborhoodType = neighborhoodType;
  }

  public  void setGridType(String gridType) {
    myGridType = gridType;
  }

}