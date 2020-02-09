package simulation.xmlGeneration;

import java.io.FileNotFoundException;

import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class XMLGenerator {

  public static void main(String[] argv) throws FileNotFoundException {
    UserInputParser.getUserInput();
    try {
      Document document = getDocument();
      Element root = createSimulationRoot(document);
      addElementsToRoot(document, root);
      createXMLDocument(document);
      System.out.println("Done creating XML File");
    } catch (ParserConfigurationException | TransformerException pce) {
      pce.printStackTrace();
    }
  }

  private static Document getDocument() throws ParserConfigurationException {
    DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
    DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
    return documentBuilder.newDocument();
  }

  private static Element createSimulationRoot(Document document) {
    Element root = document.createElement("simulation");
    document.appendChild(root);
    return root;
  }

  private static void addElementsToRoot(Document document, Element root) {

    addElement(document, root, "rule", UserInputParser.getRuleSelector());
    addElement(document, root, "simulationTitle", UserInputParser.getSimulationTitle());
    addElement(document, root, "simulationAuthor", UserInputParser.getSimulationAuthor());
    addElement(document, root, "numGlobalVars", "" + UserInputParser.getNumGlobalVars());

    addGlobalVarElements(document, root);

    addElement(document, root, "gridWidth", "" + UserInputParser.getGridWidth());
    addElement(document, root, "gridHeight", "" + UserInputParser.getGridHeight());
    addElement(document, root, "isToroidal", "" + UserInputParser.getGridIsToroidal());
    addElement(document, root, "neighborhoodType", "" + UserInputParser.getNeighborhoodType());

    addGridElements(document, root);

  }

  private static void addElement(Document document, Element root, String rule,
      String myRuleSelector) {
    Element ruleClass = document.createElement(rule);
    ruleClass.appendChild(document.createTextNode(myRuleSelector));
    root.appendChild(ruleClass);
  }

  private static void addGlobalVarElements(Document document, Element root) {
    //global vars
    Element globalVars = document.createElement("globalVars");
    root.appendChild(globalVars);

    //each global var
    if (UserInputParser.getNumGlobalVars() == 0) {
      addElement(document, globalVars, "var", "" + 0);
    } else {
      for (int i = 0; i < UserInputParser.getNumGlobalVars(); i++) {
        addElement(document, globalVars, "var" + i, "" + UserInputParser.getGlobalVars()[i]);
      }
    }
  }

  private static void addGridElements(Document document, Element root) {
    //grid rows
    Element gridRows = document.createElement("gridRows");
    root.appendChild(gridRows);

    //build grid
    int[][] initialStateGrid = UserInputParser.getInitialStateGrid();
    for (int i = 0; i < UserInputParser.getGridHeight(); i++) {
      Element row = document.createElement("row" + i);
      StringBuilder rowString = new StringBuilder();
      for (int j = 0; j < UserInputParser.getGridWidth(); j++) {
        rowString.append(initialStateGrid[i][j]).append(" ");
      }
      row.appendChild(document.createTextNode(rowString.toString()));
      gridRows.appendChild(row);
    }
  }

  private static void createXMLDocument(Document document) throws TransformerException {
    Transformer transformer = setDocumentProperties();
    translateDOMtoXML(document, transformer);
  }

  private static Transformer setDocumentProperties() throws TransformerConfigurationException {
    TransformerFactory transformerFactory = TransformerFactory.newInstance();
    Transformer transformer = transformerFactory.newTransformer();
    transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
    transformer.setOutputProperty(OutputKeys.INDENT, "yes");
    return transformer;
  }

  private static void translateDOMtoXML(Document document, Transformer transformer)
      throws TransformerException {
    DOMSource domSource = new DOMSource(document);
    StreamResult streamResult = new StreamResult(new File(UserInputParser.getXmlFilePath()));
    transformer.transform(domSource, streamResult);
  }
}