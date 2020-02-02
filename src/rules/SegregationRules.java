package rules;

import javafx.scene.paint.Color;

public class SegregationRules extends Rules {

  private double segregation_threshold;
  private static Color[] groupColors = {Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW, Color.PURPLE, Color.ORANGE};

  @Override
  public int calculateNewState(int currentState, int[] neighbors) {
    if (percentUnlikeNeighbors(currentState, neighbors) > segregation_threshold) {
      return -1*(int)(Math.random() * 8);
    } else {
      return currentState;
    }
  }

  private double percentUnlikeNeighbors(int currentState, int[] neighbors) {
    if (currentState == 0) {
      return 0;
    }
    int likeNeighbors = 0;
    for (int n : neighbors) {
      if ( n == currentState) {
        likeNeighbors += 1;
      }
    }
    return likeNeighbors / 8.0;
  }

  @Override
  public Color getStateColor(int state) {
    if (state == 0) {
      return Color.WHITE;
    } else {
      return groupColors[state];
    }
  }

  @Override
  public void setGlobalVariables(double[] variables) {
    segregation_threshold = variables[0];
  }
}
