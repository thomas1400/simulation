package simulation.model;

import javafx.scene.paint.Color;
import simulation.rules.Rules;

/**
 * Pass this class if Grid needs to be changed
 */
public class Grid {

  public static final int NEIGHBORHOOD_CENTER = -1;
  private int[][] myStates;
  private int[][] myUpdates;
  private int myHeight;
  private int myWidth;

  private Rules myRuleSet;

  private boolean isToroidal;
  private int[][] myNeighborhoodShape;

  public Grid(int[][] initialStates, int[][] neighborhoodShape, Rules ruleSet, boolean toroidal) {
    myWidth = initialStates.length;
    myHeight = initialStates[0].length;

    myStates = new int[myWidth][myHeight];
    for (int x = 0; x < myWidth; x++) {
      myStates[x] = initialStates[x].clone();
    }

    myUpdates = new int[myWidth][myHeight];

    myNeighborhoodShape = new int[neighborhoodShape.length][neighborhoodShape[0].length];
    for (int x = 0; x < neighborhoodShape.length; x++) {
      myNeighborhoodShape[x] = neighborhoodShape[x].clone();
    }

    myRuleSet = ruleSet;

    isToroidal = toroidal;
  }

  public void step() {
    for (int x = 0; x < myWidth; x++) {
      for (int y = 0; y < myHeight; y++) {
        int[] neighborStates = getNeighborStates(x, y);
        myUpdates[x][y] = myRuleSet.calculateNewState(myStates[x][y], neighborStates);
      }
    }
    for (int x = 0; x < myWidth; x++) {
      myStates[x] = myUpdates[x].clone();
    }
  }

  private int[] getNeighborStates(int x, int y) {
    int[] neighborStates = new int[myNeighborhoodShape.length*myNeighborhoodShape[0].length-1];

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
    int counter = 0;
    for (int i = 0; i < myNeighborhoodShape.length; i++) {
      for (int j = 0; j < myNeighborhoodShape[0].length; j++) {
        xOffset = i - centerX;
        yOffset = j - centerY;
        if ((xOffset != 0 || yOffset != 0) && inGridBounds(x + xOffset, y + yOffset)) {
          neighborStates[counter] = myStates[x + xOffset][y + yOffset] * myNeighborhoodShape[i][j];
          counter += 1;
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
    Color[][] myColorGrid = new Color[myHeight][myWidth];
    for (int i = 0; i < myHeight; i++) {
      for (int j = 0; j < myWidth; j++) {
        myColorGrid[i][j] = myRuleSet.getStateColor(myStates[i][j]);
      }
    }
    return myColorGrid;
  }

  public int[] getStats() {
    int[] statArray = new int[100];
    for(int[] ca : myStates){
      for(int c : ca){
        statArray[c]++;
      }
    }
    return statArray;
  }

  public void incrementState(int x, int y){
    myStates[x][y] = myRuleSet.incrementState(myStates[x][y]);
  }

}
