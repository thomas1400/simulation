package simulation.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import simulation.rules.Rules;
import simulation.xmlGeneration.SimulationSettings;

/**
 * This class embodies my understanding of and appreciation for inheritance
 * hierarchies and abstraction as a tool for enabling extension.
 *
 * Grid is an abstract class that represents the grid of a simulation. It contains
 * nearly all of the functionality of a grid, encoded as a 2D array. In order to
 * extend this project and add a new grid type, like hexagonal tiles, all that's
 * required is to create a new class and extend Grid, then implement the single
 * method getGridPane(). This makes adding new features of this kind extremely easy.
 *
 * Grid also hides its implementation from every other class. It only ever passes
 * back some statistics about the states in the grid (for graphing) and the
 * graphical representation of the grid itself, which will not affect the grid
 * in any way. It cannot be changed except through appropriate method calls.
 */
public abstract class Grid {

  protected static final int NEIGHBORHOOD_CENTER = -1;

  protected State[][] myStates;
  private List<State> myUpdateOrder;
  protected int myHeight;
  protected int myWidth;
  protected boolean isToroidal;
  protected int[][] myNeighborhoodShape;

  protected Rules myRuleSet;

  /**
   * Creates a new grid
   * @param initialStates the initial states of the grid
   * @param neighborhoodShape the shape of the cells' neighborhood
   * @param ruleSet the Rules of this simulation
   * @param toroidal true if the grid is toroidal, false otherwise
   */
  public Grid(int[][] initialStates, int[][] neighborhoodShape, Rules ruleSet, boolean toroidal) {
    myWidth = initialStates.length;
    myHeight = initialStates[0].length;

    initializeStates(initialStates);
    initializeNeighborhood(neighborhoodShape);

    myRuleSet = ruleSet;
    myRuleSet.setGrid(this);

    isToroidal = toroidal;
  }

  private void initializeNeighborhood(int[][] neighborhoodShape) {
    myNeighborhoodShape = new int[neighborhoodShape.length][neighborhoodShape[0].length];
    for (int x = 0; x < neighborhoodShape.length; x++) {
      System.arraycopy(neighborhoodShape[x], 0, myNeighborhoodShape[x], 0,
          neighborhoodShape[x].length);
    }
  }

  private void initializeStates(int[][] initialStates) {
    myStates = new State[myWidth][myHeight];
    myUpdateOrder = new ArrayList<>();
    for (int x = 0; x < myWidth; x++) {
      for (int y = 0; y < myHeight; y++) {
        State newState = new State(initialStates[x][y], new int[]{x, y});
        myStates[x][y] = newState;
        myUpdateOrder.add(newState);
      }
    }
  }

  /**
   * Returns a graphical representation of this Grid.
   * @param MAX_SIZE the maximum width/height of the grid
   * @return a new Pane
   */
  public abstract Pane getGridPane(int MAX_SIZE);

  /**
   * Updates the simulation by one step.
   */
  public void step() {
    Collections.shuffle(myUpdateOrder);
    for (State s : myUpdateOrder) {
      List<State> neighborStates = getNeighborStates(s);
      myRuleSet.calculateUpdate(s, neighborStates);
    }
    for (State s : myUpdateOrder) {
      s.update();
    }
  }

  protected List<State> getNeighborStates(State state) {
    List<State> neighborStates = new ArrayList<>();

    int x = state.getX(), y = state.getY();

    int[] center = findNeighborhoodCenter();

    int xOffset, yOffset;
    for (int i = 0; i < myNeighborhoodShape.length; i++) {
      for (int j = 0; j < myNeighborhoodShape[0].length; j++) {
        xOffset = i - center[0];
        yOffset = j - center[1];
        if (myNeighborhoodShape[i][j] == 1) {
          addNewNeighbor(neighborStates, x + xOffset, y + yOffset);
        }
      }
    }

    return neighborStates;
  }

  private void addNewNeighbor(List<State> neighborStates, int x, int y) {
    if (inGridBounds(x, y)) {
      neighborStates.add(myStates[x][y]);
    } else if (isToroidal) {
      int[] tc = toroidizeCoordinates(x, y);
      neighborStates.add(myStates[tc[0]][tc[1]]);
    }
  }

  protected int[] findNeighborhoodCenter() {
    int[] center = {myNeighborhoodShape.length / 2, myNeighborhoodShape[0].length / 2};
    for (int i = 0; i < myNeighborhoodShape.length; i++) {
      for (int j = 0; j < myNeighborhoodShape[0].length; j++) {
        if (myNeighborhoodShape[i][j] == NEIGHBORHOOD_CENTER) {
          center[0] = j;
          center[1] = i;
          break;
        }
      }
    }
    return center;
  }

  protected boolean inGridBounds(int x, int y) {
    return (0 <= x && 0 <= y && x < myWidth && y < myHeight);
  }

  protected int[] toroidizeCoordinates(int x, int y) {
    return new int[]{normalize(x, myWidth), normalize(y, myHeight)};
  }

  protected int normalize(int c, int bound) {
    if (c < 0 || c >= bound) {
      c = (c + bound) % bound;
    }
    return c;
  }

  /**
   * Returns a List of all grid cells that are 'empty', state == 0
   * @return a List of States with state == 0
   */
  public List<State> getEmptyStates() {
    ArrayList<State> emptyStates = new ArrayList<>();
    for (State[] sa : myStates) {
      for (State s : sa) {
        if (s.isEmpty()) {
          emptyStates.add(s);
        }
      }
    }
    return emptyStates;
  }

  /**
   * Increments a cell's state and update its graphical representation
   * @param x the cell's x coordinate
   * @param y the cell's y coordinate
   * @return the new Color of the cell
   */
  public Color dynamicallyIncrement(int x, int y) {
    myRuleSet.incrementState(myStates[x][y]);
    return myRuleSet.getStateColor(myStates[x][y]);
  }

  /**
   * Checks if this simulation has reached stasis, i.e., is unchanging
   * @return true if no cells updated last step
   */
  public boolean isStatic() {
    for (State[] sa : myStates) {
      for (State s : sa) {
        if (!s.isStatic()) {
          return false;
        }
      }
    }
    return true;
  }

  /**
   * Returns the area of this grid
   * @return area
   */
  public double getArea() {
    return myWidth * myHeight;
  }

  /**
   * Gets counts of each cell type present in this grid
   * @return a Map of cell type to count
   */
  public Map<Integer, Integer> getCellCounts() {
    HashMap<Integer, Integer> counts = new HashMap<>();
    for (State[] sa : myStates) {
      for (State s : sa) {
        counts.putIfAbsent(s.toInt(), 0);
        counts.put(s.toInt(), counts.get(s.toInt()) + 1);
      }
    }
    return counts;
  }

  /**
   * Convert this Grid to an int array for textual representation
   * @return an array of ints
   */
  public int[][] toIntArray() {
    int[][] myIntArray = new int[myWidth][myHeight];
    for (int i = 0; i < myWidth; i++) {
      for (int j = 0; j < myHeight; j++) {
        myIntArray[i][j] = myStates[i][j].toInt();
      }
    }
    return myIntArray;
  }

  /**
   * Updates simulation settings
   * @param settings new settings
   */
  public void updateSettings(SimulationSettings settings) {
    settings.setGridWidth(myWidth);
    settings.setGridHeight(myHeight);
    settings.setGridIsToroidal(isToroidal);
  }

}
