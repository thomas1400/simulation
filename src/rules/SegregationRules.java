package rules;

import javafx.scene.paint.Color;

public class SegregationRules extends Rules {

  private double segregation_threshold;
  private static Color[] groupColors = {Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW, Color.PURPLE, Color.ORANGE};

  @Override
  public int calculateNewState(int currentState, int[] neighbors) {
    if (percentLikeNeighbors(currentState, neighbors) < segregation_threshold || currentState == 0) {
      System.out.println(percentLikeNeighbors(currentState, neighbors));
      return -1;
    } else {
      return currentState;
    }
  }

  private double percentLikeNeighbors(int currentState, int[] neighbors) {
    if (currentState == 0) {
      return 0;
    }
    int likeNeighbors = 0;
    float numNeighbors = 0;
    for (int n : neighbors) {
      if (n == currentState) {
        likeNeighbors += 1;
      }
      if (n > 0) {
        numNeighbors += 1;
      }
    }
    return likeNeighbors / numNeighbors;
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
    if (variables.length != 1) {
      throw new IllegalArgumentException(
              "Unexpected number of variables for PredatorPreyRules. Expected 1 but got " + variables.length
      );
    }
    segregation_threshold = variables[0];
  }
}
