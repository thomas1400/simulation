package xmlGeneration;

import java.io.FileNotFoundException;
import java.util.Scanner;

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

  private static String myRuleSelector;
  private static String mySimulationTitle;
  private static String mySimulationAuthor;
  private static int myNumGlobalVars;
  private static double[] myGlobalVars;
  private static int myGridWidth;
  private static int myGridHeight;
  private static boolean myGridIsToroidal;
  private static int[][] myInitialStateGrid;

  public static String xmlFilePath;

  public static void main(String[] argv) throws FileNotFoundException {
    getUserInput();
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



  private static void getUserInput() throws FileNotFoundException {
    setMyFilePath();
    setMyRuleSelector();
    setSimulationTitle();
    setMyNumGlobalVars();
    setMyGlobalVars();
    System.out.print("Would you like to load a grid configuration text document? (y/n): ");
    Scanner input = new Scanner(System.in);
    if (input.next().equals("y")) {
      setInitialGridFromFile(input);
    } else {
      setInitialGridFromUser();
    }
    setMyGridIsToroidal();
  }

  private static void setMyGridIsToroidal() {
    System.out.print("Is this simulation toroidal? Enter true or false: ");
    Scanner input = new Scanner(System.in);
    myGridIsToroidal = Boolean.parseBoolean(input.next());
  }

  private static void setMyFilePath() {
    System.out.print("Please enter the name of the file to be created (ending with .xml): ");
    Scanner input = new Scanner(System.in);
    xmlFilePath = "data/" + input.next();
  }

  private static void setInitialGridFromUser() {
    setMyGridWidth();
    setMyGridHeight();
    setMyInitialStateGrid();
  }

  private static void setInitialGridFromFile(Scanner input) throws FileNotFoundException {
    System.out.print("Please enter the name of the file to load: ");
    String loadFile = "data/" + input.next();

    File file = new File(loadFile);

    setGridHeightFromFile(file);
    setGridWidthFromFile(file);
    setGridStatesFromFile(file);
  }

  private static void setGridStatesFromFile(File file) throws FileNotFoundException {
    Scanner sc;
    myInitialStateGrid = new int[myGridHeight][myGridWidth];
    sc = new Scanner(file);
    for (int i = 0; i < myGridHeight; i++) {
      for (int j = 0; j < myGridWidth; j++) {
        myInitialStateGrid[i][j] = Integer.parseInt(sc.next());
      }
    }
  }

  private static void setGridWidthFromFile(File file) throws FileNotFoundException {
    Scanner sc;
    sc = new Scanner(file);
    int fileWidth = 0;
    while (sc.hasNext()) {
      fileWidth++;
      sc.next();
    }
    myGridWidth = fileWidth / myGridHeight;
  }

  private static void setGridHeightFromFile(File file) throws FileNotFoundException {
    Scanner sc;
    sc = new Scanner(file);
    int fileHeight = 0;
    while (sc.hasNextLine()) {
      fileHeight++;
      sc.nextLine();
    }
    myGridHeight = fileHeight;
  }

  private static void setMyInitialStateGrid() {
    myInitialStateGrid = new int[myGridHeight][myGridWidth];
    Scanner input = new Scanner(System.in);
    for (int i = 0; i < myGridHeight; i++) {
      for (int j = 0; j < myGridWidth; j++) {
        System.out.print("Please enter the state of Cell " + i + ", " + j + ": ");
        if (!input.hasNextInt()) {
          System.out.println("That's not a valid number. Try Again.\n");
          j--;
        } else {
          myInitialStateGrid[i][j] = input.nextInt();
        }
      }
    }
  }

  private static void setMyGridHeight() {
    Scanner input = new Scanner(System.in);
    System.out.print("Please enter the simulation's grid HEIGHT: ");
    if (!input.hasNextInt()) {
      System.out.println("That's not a valid number. Try Again.\n");
      setMyGridHeight();
    } else {
      myGridHeight = input.nextInt();
    }
  }

  private static void setMyGridWidth() {
    Scanner input = new Scanner(System.in);
    System.out.print("Please enter the simulation's grid WIDTH: ");
    if (!input.hasNextInt()) {
      System.out.println("That's not a valid number. Try Again.\n");
      setMyGridWidth();
    } else {
      myGridWidth = input.nextInt();
    }
  }

  private static void setMyNumGlobalVars() {
    Scanner input = new Scanner(System.in);
    System.out.print("Please enter a valid number of global variables: ");
    if (!input.hasNextInt()) {
      System.out.println("That's not a valid number. Try Again.\n");
      setMyNumGlobalVars();
    } else {
      int inputHolder = input.nextInt();
      if (inputHolder > 0 && inputHolder < 25) {
        myNumGlobalVars = inputHolder;
      } else {
        System.out.println("That's not a valid number of variables. Try Again.\n");
        setMyNumGlobalVars();
      }
    }
  }

  private static void setMyGlobalVars() {
    myGlobalVars = new double[myNumGlobalVars];
    for (int i = 0; i < myNumGlobalVars; i++) {
      System.out.print("Please enter global variable " + (i + 1) + ": ");
      Scanner input = new Scanner(System.in);
      if (!input.hasNextDouble()) {
        System.out.println("That's not a valid number. Try Again.\n");
        i--;
      } else {
        myGlobalVars[i] = input.nextDouble();
      }
    }
  }

  private static void setSimulationTitle() {
    Scanner input = new Scanner(System.in);
    System.out.print("Please enter a simulation title: ");
    mySimulationTitle = input.nextLine();
  }

  private static void setMyRuleSelector() {
    Scanner input = new Scanner(System.in);

    System.out.println("Please choose your simulation rules:");
    System.out.println(" 0 - Fire");
    System.out.println(" 1 - Game of Life");
    System.out.println(" 2 - Percolation");
    System.out.println(" 3 - Predator Prey");
    System.out.println(" 4 - Segregation");
    System.out.print("Enter the integer corresponding to your selection: ");

    if (!input.hasNextInt()) {
      System.out.println("That's not a valid number. Try Again.\n");
      setMyRuleSelector();
    } else {
      int selection = input.nextInt();
      if (selection == 0) {
        myRuleSelector = "fireRules";
      } else if (selection == 1) {
        myRuleSelector = "gameOfLifeRules";
      } else if (selection == 2) {
        myRuleSelector = "percolationRules";
      } else if (selection == 3) {
        myRuleSelector = "predatorPreyRules";
      } else if (selection == 4) {
        myRuleSelector = "segregationRules";
      } else {
        System.out.println("Invalid Input, Try Again");
        System.out.println();
        setMyRuleSelector();
      }
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

    addElement(document, root, "rule", myRuleSelector);
    addElement(document, root, "simulationTitle", mySimulationTitle);
    addElement(document, root, "simulationAuthor", mySimulationAuthor);
    addElement(document, root, "numGlobalVars", "" + myNumGlobalVars);

    addGlobalVarElements(document, root);

    addElement(document, root, "gridWidth", "" + myGridWidth);
    addElement(document, root, "gridHeight", "" + myGridHeight);
    addElement(document, root, "isToroidal", "" + myGridIsToroidal);

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
    if (myNumGlobalVars == 0) {
      addElement(document, globalVars, "var", "" + 0);
    } else {
      for (int i = 0; i < myNumGlobalVars; i++) {
        addElement(document, globalVars, "var" + i, "" + myGlobalVars[i]);
      }
    }
  }

  private static void addGridElements(Document document, Element root) {
    //grid rows
    Element gridRows = document.createElement("gridRows");
    root.appendChild(gridRows);

    //build grid
    for (int i = 0; i < myGridHeight; i++) {
      Element row = document.createElement("row" + i);
      StringBuilder rowString = new StringBuilder();
      for (int j = 0; j < myGridWidth; j++) {
        rowString.append(myInitialStateGrid[i][j]).append(" ");
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
    StreamResult streamResult = new StreamResult(new File(xmlFilePath));
    transformer.transform(domSource, streamResult);
  }
}