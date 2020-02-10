package simulation.xmlGeneration;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class UserInputParser {

  private static int myGridWidth;
  private static int myGridHeight;
  private static int myNumGlobalVars;

  private static SimulationSettings mySettings;

  public static SimulationSettings getUserInput() throws FileNotFoundException {
    mySettings = new SimulationSettings();

    askForFilePath();
    askForRuleSelector();
    askForSimulationTitle();
    askForSimulationAuthor();
    askForNumGlobalVars();
    askForGlobalVars();
    askForGridType();
    askForGridIsToroidal();
    askForNeighborhoodType();
    askForSimulationGrid();

    return mySettings;
  }

  private static void askForFilePath() {
    System.out.print("Please enter the name of the file to be created: ");
    Scanner input = new Scanner(System.in);
    mySettings.setFilePath("data/" + input.next());
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
        mySettings.setRuleSelector("fireRules");
      } else if (selection == 1) {
        mySettings.setRuleSelector("gameOfLifeRules");
      } else if (selection == 2) {
        mySettings.setRuleSelector("percolationRules");
      } else if (selection == 3) {
        mySettings.setRuleSelector("predatorPreyRules");
      } else if (selection == 4) {
        mySettings.setRuleSelector("segregationRules");
      } else if (selection == 5) {
        mySettings.setRuleSelector("rockPaperScissorsRules");
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
    mySettings.setSimulationTitle(input.nextLine());
  }

  private static void askForSimulationAuthor() {
    Scanner input = new Scanner(System.in);
    System.out.print("Please enter the simulation authors: ");
    mySettings.setSimulationAuthor(input.nextLine());
  }

  private static void askForNumGlobalVars() {
    Scanner input = new Scanner(System.in);
    System.out.print("Please enter a valid number of global variables: ");
    if (!input.hasNextInt()) {
      displayInvalidNumInput();
      askForNumGlobalVars();
    } else {
      int inputHolder = input.nextInt();
      if (inputHolder > 0) {
        myNumGlobalVars = inputHolder;
        mySettings.setNumGlobalVars(myNumGlobalVars);
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
    double[] myGlobalVars = new double[myNumGlobalVars];
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
    mySettings.setGlobalVars(myGlobalVars);
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
    System.out.print("Is this simulation toroidal? (true/false): ");
    Scanner input = new Scanner(System.in);
    mySettings.setGridIsToroidal(Boolean.parseBoolean(input.next()));
  }

  private static void askForGridType() {
    System.out.print("What type of grid will this simulation have? Enter \"Rectangular\" or \"Triangular\": ");
    Scanner input = new Scanner(System.in);
    mySettings.setGridType(input.next());
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
    mySettings.setGridWidth(myGridWidth);
  }

  private static void askForGridHeightFromFile(File file) throws FileNotFoundException {
    Scanner sc = new Scanner(file);
    int fileHeight = 0;
    while (sc.hasNextLine()) {
      fileHeight++;
      sc.nextLine();
    }
    myGridHeight = fileHeight;
    mySettings.setGridHeight(myGridHeight);
  }

  private static void askForGridStatesFromFile(File file) throws FileNotFoundException {
    Scanner sc = new Scanner(file);
    int[][] myInitialStateGrid = new int[myGridHeight][myGridWidth];
    for (int i = 0; i < myGridHeight; i++) {
      for (int j = 0; j < myGridWidth; j++) {
        myInitialStateGrid[i][j] = Integer.parseInt(sc.next());
      }
    }
    mySettings.setInitialStateGrid(myInitialStateGrid);
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
      mySettings.setGridWidth(myGridWidth);
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
      mySettings.setGridHeight(myGridHeight);
    }
  }

  private static void askForInitialStateGrid() {
    int[][] myInitialStateGrid = new int[myGridHeight][myGridWidth];
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
    mySettings.setInitialStateGrid(myInitialStateGrid);
  }

  private static void askForNeighborhoodType() {
    Scanner input = new Scanner(System.in);
    System.out.print("Please enter the simulation's grid neighborhood type (1-4): ");
    if (!input.hasNextInt()) {
      displayInvalidNumInput();
      askForNeighborhoodType();
    } else {
      mySettings.setNeighborhoodType(input.nextInt());
    }
  }

}
