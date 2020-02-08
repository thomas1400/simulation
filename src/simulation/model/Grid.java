package simulation.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javafx.scene.paint.Color;
import simulation.rules.Rules;

/**
 * Pass this class if Grid needs to be changed
 */
public class Grid {

  public static final int NEIGHBORHOOD_CENTER = -1;
  private State[][] myStates;
  private List<State> myUpdateOrder;
  private int myHeight;
  private int myWidth;

  private Rules myRuleSet;

  private boolean isToroidal;
  private int[][] myNeighborhoodShape;

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
      myNeighborhoodShape[x] = neighborhoodShape[x].clone();
    }

    myRuleSet = ruleSet;
    myRuleSet.setGrid(this);

    isToroidal = toroidal;
  }

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

  private List<State> getNeighborStates(int[] location) {
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
        if (myNeighborhoodShape[i][j] == 1 && inGridBounds(x + xOffset, y + yOffset)) {
          neighborStates.add(myStates[x + xOffset][y + yOffset]);
        }
      }
    }

    return neighborStates;
  }

  private boolean inGridBounds(int x, int y) {
    return (0 <= x && 0 <= y && x < myWidth && y < myHeight);
  }

  /**
   * return 2x2 grid of cellColors
   */
  public Color[][] getColorGrid() {
    Color[][] myColorGrid = new Color[myWidth][myHeight];
    for (int x = 0; x < myWidth; x++) {
      for (int y = 0; y < myHeight; y++) {
        myColorGrid[x][y] = myRuleSet.getStateColor(myStates[x][y]);
      }
    }
    return myColorGrid;
  }

  public int[] getStats() {
    int[] statArray = new int[100];
    for(State[] sa : myStates){
      for(State s : sa){
        statArray[s.toInt()]++;
      }
    }
    return statArray;
  }

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

  public void incrementState(int x, int y){
    myRuleSet.incrementState(myStates[x][y]);
  }

}
