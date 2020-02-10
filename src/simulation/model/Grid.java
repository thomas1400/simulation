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

  public abstract Pane getGridPane(int MAX_SIZE);

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
            int[] tc = toroidizeCoordinates(x+xOffset, y+yOffset);
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
      c = (c+bound) % bound;
    }
    return c;
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

  public Color dynamicallyIncrement(int x, int y) {
    myRuleSet.incrementState(myStates[x][y]);
    return myRuleSet.getStateColor(myStates[x][y]);
  }

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

  public double getArea() {
    return myWidth * myHeight;
  }

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

  public int[][] toIntArray() {
    int[][] myIntArray = new int[myWidth][myHeight];
    for (int i = 0; i < myWidth; i++){
      for(int j = 0; j < myHeight; j++){
          myIntArray[i][j] = myStates[i][j].toInt();
        }
    }
    return myIntArray;
  }

  public void updateSettings(SimulationSettings settings) {
    settings.setGridWidth(myWidth);
    settings.setGridHeight(myHeight);
    settings.setGridIsToroidal(isToroidal);
  }

}
