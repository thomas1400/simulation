package rules;

import javafx.scene.paint.Color;

public class SegregationRules extends Rules {

  private double segregation_threshold;

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
    return null;
  }

  @Override
  public void setGlobalVariables(double[] variables) {
    segregation_threshold = variables[0];
  }
}
