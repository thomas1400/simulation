package simulation.controller;

import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.traversal.DocumentTraversal;
import org.w3c.dom.traversal.NodeFilter;
import org.w3c.dom.traversal.NodeIterator;
import org.xml.sax.SAXException;

class XMLReader {
  //File Attributes
  private String myRule;
  private String mySimulationTitle;
  private String mySimulationAuthor;
  private int myNumGlobalVars;
  private double[] myGlobalVars;
  private int myGridWidth;
  private int myGridHeight;
  private boolean myIsToroidal;
  private int[][] myGrid;
  private int myNeighborhoodType;

  public XMLReader(String file) throws IOException, SAXException, ParserConfigurationException {
    NodeIterator iterator = getNodeIterator(file);
    readFile(iterator);
  }

  private NodeIterator getNodeIterator(String file)
      throws ParserConfigurationException, IOException, SAXException {
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    DocumentBuilder loader = factory.newDocumentBuilder();
    Document document = loader.parse(file);
    DocumentTraversal traversal = (DocumentTraversal) document;
    return traversal.createNodeIterator(document.getDocumentElement(), NodeFilter.SHOW_TEXT,
        null, true);
  }

  private void readFile(NodeIterator iterator) {
    myRule = updateRules(iterator);
    mySimulationTitle = updateSimulationTitle(iterator);
    mySimulationAuthor = updateSimulationAuthor(iterator);

    myNumGlobalVars = updateNumGlobalVars(iterator);
    myGlobalVars = updateGlobalVars(iterator);

    myGridWidth = updateGridWidth(iterator);
    myGridHeight = updateGridHeight(iterator);
    myIsToroidal = updateIsToroidal(iterator);
    myNeighborhoodType = updateNeighborhoodType(iterator);

    myGrid = updateInitialStateGrid(iterator);
  }

  private String updateRules(NodeIterator iterator) {
    skipBlankLine(iterator);
    String ruleSelector = iterator.nextNode().getTextContent().trim();
    skipBlankLine(iterator);
    return ruleSelector;
  }

  private String updateSimulationTitle(NodeIterator iterator) {
    String simulationTitle = iterator.nextNode().getTextContent().trim();
    skipBlankLine(iterator);
    return simulationTitle;
  }

  private String updateSimulationAuthor(NodeIterator iterator) {
    String simulationAuthor = iterator.nextNode().getTextContent().trim();
    skipBlankLine(iterator);
    return simulationAuthor;
  }

  private int updateNumGlobalVars(NodeIterator iterator) {
    int numGlobalVars = Integer.parseInt(iterator.nextNode().getTextContent().trim());
    skipBlankLine(iterator);
    skipBlankLine(iterator);
    return numGlobalVars;
  }

  private double[] updateGlobalVars(NodeIterator iterator) {
    double[] globalVars = new double[myNumGlobalVars];
    for (int i = 0; i < myNumGlobalVars; i++) {
      globalVars[i] = Double.parseDouble(iterator.nextNode().getTextContent().trim());
      skipBlankLine(iterator);
    }
    skipBlankLine(iterator);
    return globalVars;
  }

  private int updateGridWidth(NodeIterator iterator) {
    int gridWidth = Integer.parseInt(iterator.nextNode().getTextContent().trim());
    skipBlankLine(iterator);
    return gridWidth;
  }

  private int updateGridHeight(NodeIterator iterator) {
    int gridHeight = Integer.parseInt(iterator.nextNode().getTextContent().trim());
    skipBlankLine(iterator);
    return gridHeight;
  }

  private boolean updateIsToroidal(NodeIterator iterator) {
    boolean toroidal = Boolean.parseBoolean(iterator.nextNode().getTextContent().trim());
    skipBlankLine(iterator);
    return toroidal;
  }

  private int updateNeighborhoodType(NodeIterator iterator) {
    int neighborhoodType = Integer.parseInt(iterator.nextNode().getTextContent().trim());
    skipBlankLine(iterator);
    skipBlankLine(iterator);
    return neighborhoodType;
  }

  private int[][] updateInitialStateGrid(NodeIterator iterator) {
    int[][] initialStateGrid = new int[myGridHeight][myGridWidth];
    for (int i = 0; i < myGridHeight; i++) {
      //Get string of values corresponding to a row and store in an array
      String[] rowArray = iterator.nextNode().getTextContent().trim().split(" ");
      skipBlankLine(iterator);
      //place each value in the row in it's corresponding grid position
      for (int j = 0; j < myGridWidth; j++) {
        initialStateGrid[i][j] = Integer.parseInt(rowArray[j]);
      }
    }
    return initialStateGrid;
  }

  private void skipBlankLine(NodeIterator iterator) {
    iterator.nextNode();
  }

  /**
   * These getters are used to communicate with Simulation the characteristics of the simulation
   * as read from the XML file
   * @return Values of each specific element of the XML File
   */
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

  public int getNeighborhoodType() {
    return myNeighborhoodType;
  }

  public int[][] getGrid() {
    return myGrid;
  }

}
