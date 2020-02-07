package simulation.xmlGeneration;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

class UserInputParser {
  
  private static String xmlFilePath;
  private static String myRuleSelector;
  private static String mySimulationTitle;
  private static String mySimulationAuthor;
  private static int myNumGlobalVars;
  private static double[] myGlobalVars;
  private static int myGridWidth;
  private static int myGridHeight;
  private static boolean myGridIsToroidal;
  private static int[][] myInitialStateGrid;

  public static void getUserInput() throws FileNotFoundException {
    askForFilePath();
    askForRuleSelector();
    askForSimulationTitle();
    askForSimulationAuthor();
    askForNumGlobalVars();
    askForGlobalVars();
    askForSimulationGrid();
    askForGridIsToroidal();
  }

  private static void askForFilePath() {
    System.out.print("Please enter the name of the file to be created: ");
    Scanner input = new Scanner(System.in);
    xmlFilePath = "data/" + input.next();
  }

  private static void askForRuleSelector() {
    Scanner input = new Scanner(System.in);

    displaySimulationTypePrompt();

    if (!input.hasNextInt()) {
      displayInvalidNumInput();
      askForRuleSelector();
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
      } else if (selection == 5) {
        myRuleSelector = "rockPaperScissorsRules";
      } else {
        System.out.println("Invalid Input, Try Again");
        System.out.println();
        askForRuleSelector();
      }
    }
  }

  private static void displaySimulationTypePrompt() {
    System.out.println("Please choose your simulation simulation.rules:");
    System.out.println(" 0 - Fire");
    System.out.println(" 1 - Game of Life");
    System.out.println(" 2 - Percolation");
    System.out.println(" 3 - Predator Prey");
    System.out.println(" 4 - Segregation");
    System.out.println(" 5 - Rock, Paper, Scissors");
    System.out.print("Enter the integer corresponding to your selection: ");
  }

  private static void askForSimulationTitle() {
    Scanner input = new Scanner(System.in);
    System.out.print("Please enter a simulation title: ");
    mySimulationTitle = input.nextLine();
  }

  private static void askForSimulationAuthor() {
    Scanner input = new Scanner(System.in);
    System.out.print("Please enter the simulation authors: ");
    mySimulationAuthor = input.nextLine();
  }

  private static void askForNumGlobalVars() {
    Scanner input = new Scanner(System.in);
    System.out.print("Please enter a valid number of global variables: ");
    if (!input.hasNextInt()) {
      displayInvalidNumInput();
      askForNumGlobalVars();
    } else {
      int inputHolder = input.nextInt();
      if (inputHolder > 0 && inputHolder < 25) {
        myNumGlobalVars = inputHolder;
      } else {
        System.out.println("That's not a valid number of variables. Try Again.\n");
        askForNumGlobalVars();
      }
    }
  }

  private static void displayInvalidNumInput() {
    System.out.println("That's not a valid number. Try Again.\n");
  }

  private static void askForGlobalVars() {
    myGlobalVars = new double[myNumGlobalVars];
    for (int i = 0; i < myNumGlobalVars; i++) {
      System.out.print("Please enter global variable " + (i + 1) + ": ");
      Scanner input = new Scanner(System.in);
      if (!input.hasNextDouble()) {
        displayInvalidNumInput();
        i--;
      } else {
        myGlobalVars[i] = input.nextDouble();
      }
    }
  }
    
  private static void askForSimulationGrid() throws FileNotFoundException {
    System.out.print("Would you like to load a grid configuration text document? (y/n): ");
    Scanner input = new Scanner(System.in);
    String normalizedChoice = input.next().toLowerCase();
    if (normalizedChoice.equals("y") || normalizedChoice.equals("yes")) {
      askForInitialGridFromFile(input);
    } else {
      askForInitialGridFromUser();
    }
  }

  private static void askForGridIsToroidal() {
    System.out.print("Is this simulation toroidal? Enter true or false: ");
    Scanner input = new Scanner(System.in);
    myGridIsToroidal = Boolean.parseBoolean(input.next());
  }

  private static void askForInitialGridFromFile(Scanner input) throws FileNotFoundException {
    System.out.print("Please enter the name of the file to load: ");
    String loadFile = "data/" + input.next();

    File file = new File(loadFile);

    askForGridHeightFromFile(file);
    askForGridWidthFromFile(file);
    askForGridStatesFromFile(file);
  }

  private static void askForGridWidthFromFile(File file) throws FileNotFoundException {
    Scanner sc = new Scanner(file);
    int fileWidth = 0;
    while (sc.hasNext()) {
      fileWidth++;
      sc.next();
    }
    myGridWidth = fileWidth / myGridHeight;
  }

  private static void askForGridHeightFromFile(File file) throws FileNotFoundException {
    Scanner sc = new Scanner(file);
    int fileHeight = 0;
    while (sc.hasNextLine()) {
      fileHeight++;
      sc.nextLine();
    }
    myGridHeight = fileHeight;
  }

  private static void askForGridStatesFromFile(File file) throws FileNotFoundException {
    Scanner sc = new Scanner(file);
    myInitialStateGrid = new int[myGridHeight][myGridWidth];
    for (int i = 0; i < myGridHeight; i++) {
      for (int j = 0; j < myGridWidth; j++) {
        myInitialStateGrid[i][j] = Integer.parseInt(sc.next());
      }
    }
  }

  private static void askForInitialGridFromUser() {
    askForGridWidth();
    askForGridHeight();
    askForInitialStateGrid();
  }

  private static void askForGridWidth() {
    Scanner input = new Scanner(System.in);
    System.out.print("Please enter the simulation's grid WIDTH: ");
    if (!input.hasNextInt()) {
      displayInvalidNumInput();
      askForGridWidth();
    } else {
      myGridWidth = input.nextInt();
    }
  }

  private static void askForGridHeight() {
    Scanner input = new Scanner(System.in);
    System.out.print("Please enter the simulation's grid HEIGHT: ");
    if (!input.hasNextInt()) {
      displayInvalidNumInput();
      askForGridHeight();
    } else {
      myGridHeight = input.nextInt();
    }
  }

  private static void askForInitialStateGrid() {
    myInitialStateGrid = new int[myGridHeight][myGridWidth];
    Scanner input = new Scanner(System.in);
    for (int i = 0; i < myGridHeight; i++) {
      for (int j = 0; j < myGridWidth; j++) {
        System.out.print("Please enter the state of Cell " + i + ", " + j + ": ");
        if (!input.hasNextInt()) {
          displayInvalidNumInput();
          j--;
        } else {
          myInitialStateGrid[i][j] = input.nextInt();
        }
      }
    }
  }

  public static String getXmlFilePath() {
    return xmlFilePath;
  }

  public static String getRuleSelector() {
    return myRuleSelector;
  }

  public static String getSimulationTitle() {
    return mySimulationTitle;
  }

  public static String getSimulationAuthor() {
    return mySimulationAuthor;
  }

  public static int getNumGlobalVars() {
    return myNumGlobalVars;
  }

  public static double[] getGlobalVars() {
    return myGlobalVars;
  }

  public static int getGridWidth() {
    return myGridWidth;
  }

  public static int getGridHeight() {
    return myGridHeight;
  }

  public static boolean getGridIsToroidal() {
    return myGridIsToroidal;
  }

  public static int[][] getInitialStateGrid() {
    return myInitialStateGrid;
  }
}
