package cellsociety;

import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.traversal.DocumentTraversal;
import org.w3c.dom.traversal.NodeFilter;
import org.w3c.dom.traversal.NodeIterator;
import org.xml.sax.SAXException;

public class Simulation {
    public Cell[][] myCellGrid;

    private String myRuleSelector;
    private String mySimulationTitle;
    private double[] myGlobalVars;
    private int myGridWidth;
    private int myGridHeight;
    private int[][] myInitialStateGrid;

    public Simulation(String xmlFileName) throws IOException, SAXException, ParserConfigurationException {
        loadConfigFile(xmlFileName);

        Rules myRuleClass = getNewRulesClass(myRuleSelector);

        fillCellGrid(myRuleClass);
        initializeCellPointers();
    }

    public void play(){

    }

    private void loadConfigFile(String file) throws ParserConfigurationException, SAXException, IOException {
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
        for (int i = 0; i < myGridHeight; i++){
            //Get string of values corresponding to a row and store in an array
            String[] rowArray = iterator.nextNode().getTextContent().trim().split(" ");
            skipBlankLine(iterator);
            for (int j = 0; j < myGridWidth; j++){
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
        for (int i = 0; i < numGlobalVars; i++){
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

    private NodeIterator getNodeIterator(String file) throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder loader = factory.newDocumentBuilder();
        Document document = loader.parse(file);
        DocumentTraversal traversal = (DocumentTraversal) document;
        return traversal.createNodeIterator(document.getDocumentElement(), NodeFilter.SHOW_TEXT,
                null, true);
    }

    private Rules getNewRulesClass(String rulesType) {
        if (rulesType.equals("fireRules")){
            return new fireRules();
        } else if (rulesType.equals("gameOfLifeRules")){
            return new gameOfLifeRules();
        } else if (rulesType.equals("percolationRules")){
            return new percolationRules();
        } else if (rulesType.equals("predatorPreyRules")){
            return new predatorPreyRules();
        } else if (rulesType.equals("segregationRules")){
            return new segregationRules();
        } else {
            System.out.println();
            System.exit(0);
            return new fireRules();
        }
    }

    private void fillCellGrid(Rules ruleType) {
        myCellGrid = new Cell[myGridHeight][myGridWidth];
        for (int i = 0; i < myGridHeight; i++){
            for (int j = 0; j < myGridWidth; j++){
                int initialCellState = myInitialStateGrid[i][j];
                myCellGrid[i][j] = new Cell(ruleType, initialCellState);
            }
        }
    }

    private void initializeCellPointers() {
        for (int i = 0; i < myGridHeight; i++){
            for (int j = 0; j < myGridWidth; j++){
                Cell myCell = myCellGrid[i][j];
                if (i != 0){
                    myCell.up = myCellGrid[i-1][j];
                }
                if (i < myGridHeight-1){
                    myCell.down = myCellGrid[i+1][j];
                }
                if (j < myGridWidth-1){
                    myCell.right = myCellGrid[i][j+1];
                }
                if (j != 0){
                    myCell.left = myCellGrid[i][j-1];
                }
            }
        }
    }
}
