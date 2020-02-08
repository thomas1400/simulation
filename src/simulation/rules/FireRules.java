package simulation.rules;

import javafx.scene.paint.Color;

public class FireRules extends Rules {

  private static final int EMPTY = 0;
  private static final int TREE = 1;
  private static final int ABLAZE = 2;

  private static final int ABOVE = 0;
  private static final int TO_RIGHT = 2;
  private static final int BELOW = 4;
  private static final int TO_LEFT = 6;

  private double fireSpreadProbability;

  public FireRules(double[] variables) {
    setGlobalVariables(variables);
  }

  /**
   * 0 corresponds to Empty, 1 Corresponds to Alive, 2 Corresponds to Burning
   *
   * @param currentState
   * @param neighbors
   * @return
   */
  @Override
  public int calculateNewState(int currentState, int[] neighbors) {
    if (currentState == TREE && adjacentNeighborIsOnFire(neighbors)
        && Math.random() < fireSpreadProbability) {
      return ABLAZE;
    } else if (currentState == ABLAZE) {
      return EMPTY;
    }
    return currentState;
  }

  private boolean adjacentNeighborIsOnFire(int[] neighbors) {
    for (int neighbor : neighbors) {
      if (neighbor == ABLAZE) {
        return true;
      }
    }
    return false;
  }

  /**
   * Burnt is Black, Alive is Green, Burning is Red
   *
   * @param state
   * @return
   */
  @Override
  public Color getStateColor(int state) {
    if (state == EMPTY) {
      return Color.BLACK;
    } else if (state == 1) {
      return Color.GREEN;
    } else {
      return Color.RED;
    }
  }

  public void setGlobalVariables(double[] variables) {
    fireSpreadProbability = variables[0];
  }

  public int incrementState(int state) {
    return (state + 1) % (ABLAZE+1);
  }
}
