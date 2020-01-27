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
    Cell[][] cellGrid;
    static int gridWidth;
    static int gridHeight;

    public Simulation(String xmlFileName) throws IOException, SAXException, ParserConfigurationException {
        loadConfigFile(xmlFileName);
    }


    public static void loadConfigFile(String file) throws ParserConfigurationException,
            SAXException, IOException {

        String ruleClass;
        String simulationTitle;
        int numGlobalVars;
        double[] globalVars;
        int[][] grid;

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder loader = factory.newDocumentBuilder();
        Document document = loader.parse(file);

        DocumentTraversal traversal = (DocumentTraversal) document;

        NodeIterator iterator = traversal.createNodeIterator(
                document.getDocumentElement(), NodeFilter.SHOW_TEXT, null, true);

        iterator.nextNode();
        ruleClass = iterator.nextNode().getTextContent().trim();
        iterator.nextNode();
        simulationTitle = iterator.nextNode().getTextContent().trim();
        iterator.nextNode();
        numGlobalVars = Integer.parseInt(iterator.nextNode().getTextContent().trim());
        iterator.nextNode();
        iterator.nextNode();
        globalVars = new double[numGlobalVars];
        for (int i = 0; i < numGlobalVars; i++){
            globalVars[i] = Double.parseDouble(iterator.nextNode().getTextContent().trim());
            iterator.nextNode();
        }
        iterator.nextNode();
        gridWidth = Integer.parseInt(iterator.nextNode().getTextContent().trim());
        iterator.nextNode();
        gridHeight = Integer.parseInt(iterator.nextNode().getTextContent().trim());
        iterator.nextNode();
        iterator.nextNode();
        grid = new int[gridHeight][gridWidth];
        for (int i = 0; i < gridHeight; i++){
            String[] rowArray = iterator.nextNode().getTextContent().trim().split(" ");
        iterator.nextNode();
            for (int j = 0; j < gridWidth; j++){
                grid[i][j] = Integer.parseInt(rowArray[j]);
            }
        }
    }
}
