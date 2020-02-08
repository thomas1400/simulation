package simulation.rules;

import javafx.scene.paint.Color;

public class SegregationRules extends Rules {

  private static final int EMPTY = 0;
  private static final int SWITCHING = -1;

  private double segregation_threshold;
  private static Color[] groupColors = {Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW,
      Color.PURPLE, Color.ORANGE};
  private static final int MY_MAX_GROUPS = 6;

  public SegregationRules(double[] variables) {
    setGlobalVariables(variables);
  }

  @Override
  public int calculateNewState(int currentState, int[] neighbors) {
    if (percentLikeNeighbors(currentState, neighbors) < segregation_threshold
        || currentState == EMPTY) {
      return SWITCHING;
    } else {
      return currentState;
    }
  }

  private double percentLikeNeighbors(int currentState, int[] neighbors) {
    if (currentState == EMPTY) {
      return EMPTY;
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
    if (state == EMPTY) {
      return Color.WHITE;
    } else {
      return groupColors[state-1];
    }
  }

  public void setGlobalVariables(double[] variables) {
    if (variables.length != 1) {
      throw new IllegalArgumentException(
          "Unexpected number of variables for PredatorPreyRules. Expected 1 but got "
              + variables.length
      );
    }
    segregation_threshold = variables[0];
  }

  public int incrementState(int state) {
    return (state + 1) % (MY_MAX_GROUPS+1);
  }
}
