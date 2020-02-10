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

  /*
  public  void main(String[] argv) throws FileNotFoundException {
    SimulationSettings settings = UserInputParser.getUserInput();
    generateXML(settings);
  }
   */

  public void generateXML(SimulationSettings settings) throws FileNotFoundException {
    try {
      Document document = getDocument();
      Element root = createSimulationRoot(document);
      addElementsToRoot(document, root, settings);
      createXMLDocument(document, settings);
      System.out.println("Done creating XML File");
    } catch (ParserConfigurationException | TransformerException pce) {
      pce.printStackTrace();
    }
  }

  private  Document getDocument() throws ParserConfigurationException {
    DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
    DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
    return documentBuilder.newDocument();
  }

  private  Element createSimulationRoot(Document document) {
    Element root = document.createElement("simulation");
    document.appendChild(root);
    return root;
  }

  private  void addElementsToRoot(Document document, Element root, SimulationSettings settings) {

    addElement(document, root, "rule", settings.getRuleSelector());
    addElement(document, root, "simulationTitle", settings.getSimulationTitle());
    addElement(document, root, "simulationAuthor", settings.getSimulationAuthor());
    addElement(document, root, "numGlobalVars", "" + settings.getNumGlobalVars());

    addGlobalVarElements(document, root, settings);

    addElement(document, root, "gridWidth", "" + settings.getGridWidth());
    addElement(document, root, "gridHeight", "" + settings.getGridHeight());
    addElement(document, root, "gridType", "" + settings.getGridType());
    addElement(document, root, "isToroidal", "" + settings.getGridIsToroidal());
    addElement(document, root, "neighborhoodType", "" + settings.getNeighborhoodType());

    addGridElements(document, root, settings);

  }

  private  void addElement(Document document, Element root, String rule,
      String myRuleSelector) {
    Element ruleClass = document.createElement(rule);
    ruleClass.appendChild(document.createTextNode(myRuleSelector));
    root.appendChild(ruleClass);
  }

  private  void addGlobalVarElements(Document document, Element root, SimulationSettings settings) {
    //global vars
    Element globalVars = document.createElement("globalVars");
    root.appendChild(globalVars);

    //each global var
    if (settings.getNumGlobalVars() == 0) {
      addElement(document, globalVars, "var", "" + 0);
    } else {
      for (int i = 0; i < settings.getNumGlobalVars(); i++) {
        addElement(document, globalVars, "var" + i, "" + settings.getGlobalVars()[i]);
      }
    }
  }

  private  void addGridElements(Document document, Element root, SimulationSettings settings) {
    //grid rows
    Element gridRows = document.createElement("gridRows");
    root.appendChild(gridRows);

    //build grid
    int[][] initialStateGrid = settings.getInitialStateGrid();
    for (int i = 0; i < settings.getGridHeight(); i++) {
      Element row = document.createElement("row" + i);
      StringBuilder rowString = new StringBuilder();
      for (int j = 0; j < settings.getGridWidth(); j++) {
        rowString.append(initialStateGrid[i][j]).append(" ");
      }
      row.appendChild(document.createTextNode(rowString.toString()));
      gridRows.appendChild(row);
    }
  }

  private  void createXMLDocument(Document document, SimulationSettings settings) throws TransformerException {
    Transformer transformer = setDocumentProperties();
    translateDOMtoXML(document, transformer, settings);
  }

  private  Transformer setDocumentProperties() throws TransformerConfigurationException {
    TransformerFactory transformerFactory = TransformerFactory.newInstance();
    Transformer transformer = transformerFactory.newTransformer();
    transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
    transformer.setOutputProperty(OutputKeys.INDENT, "yes");
    return transformer;
  }

  private  void translateDOMtoXML(Document document, Transformer transformer,
      SimulationSettings settings)
      throws TransformerException {
    DOMSource domSource = new DOMSource(document);
    StreamResult streamResult = new StreamResult(new File(settings.getFilePath()));
    transformer.transform(domSource, streamResult);
  }
}