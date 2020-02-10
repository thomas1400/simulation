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

public abstract class Grid {

  protected static final int NEIGHBORHOOD_CENTER = -1;

  protected State[][] myStates;
  protected List<State> myUpdateOrder;
  protected int myHeight;
  protected int myWidth;

  protected Rules myRuleSet;

  protected boolean isToroidal;
  protected int[][] myNeighborhoodShape;

  public Grid(int[][] initialStates, int[][] neighborhoodShape, Rules ruleSet, boolean toroidal) {
    myWidth = initialStates.length;
    myHeight = initialStates[0].length;

    myStates = new State[myWidth][myHeight];
    myUpdateOrder = new ArrayList<>();
    for (int x = 0; x < myWidth; x++) {
      for (int y = 0; y < myHeight; y++) {
        State newState = new State(initialStates[x][y], new int[]{x, y});
        myStates[x][y] = newState;
        myUpdateOrder.add(newState);
      }
    }

    myNeighborhoodShape = new int[neighborhoodShape.length][neighborhoodShape[0].length];
    for (int x = 0; x < neighborhoodShape.length; x++) {
      for (int y = 0; y < neighborhoodShape[x].length; y++) {
        myNeighborhoodShape[x][y] = neighborhoodShape[x][y];
      }
    }

    myRuleSet = ruleSet;
    myRuleSet.setGrid(this);

    isToroidal = toroidal;
  }

  /**
   * Returns a graphical representation of this Grid.
   * @param MAX_SIZE the maximum size of the grid
   * @return a new Pane
   */
  public abstract Pane getGridPane(int MAX_SIZE);

  /**
   * Updates the simulation by one step.
   */
  public void step() {
    Collections.shuffle(myUpdateOrder);
    for (State s : myUpdateOrder) {
      int[] location = s.getLocation();
      List<State> neighborStates = getNeighborStates(location);
      myRuleSet.calculateUpdate(s, neighborStates);
    }
    for (State s : myUpdateOrder) {
      s.update();
    }
  }

  protected List<State> getNeighborStates(int[] location) {
    ArrayList<State> neighborStates = new ArrayList<>();

    int x = location[0], y = location[1];

    int centerX = myNeighborhoodShape.length / 2;
    int centerY = myNeighborhoodShape[0].length / 2;
    for (int i = 0; i < myNeighborhoodShape.length; i++) {
      for (int j = 0; j < myNeighborhoodShape[0].length; j++) {
        if (myNeighborhoodShape[i][j] == NEIGHBORHOOD_CENTER) {
          centerX = i;
          centerY = j;
          break;
        }
      }
    }

    int xOffset, yOffset;
    for (int i = 0; i < myNeighborhoodShape.length; i++) {
      for (int j = 0; j < myNeighborhoodShape[0].length; j++) {
        xOffset = i - centerX;
        yOffset = j - centerY;
        if (myNeighborhoodShape[i][j] == 1) {
          if (inGridBounds(x + xOffset, y + yOffset)) {
            neighborStates.add(myStates[x + xOffset][y + yOffset]);
          } else if (isToroidal) {
            int[] tc = toroidizeCoordinates(x + xOffset, y + yOffset);
            neighborStates.add(myStates[tc[0]][tc[1]]);
          }
        }
      }
    }

    return neighborStates;
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
