package simulation.rules;

import javafx.scene.paint.Color;

public class FireRule implements Rule {

  private static final int EMPTY = 0;
  private static final int TREE = 1;
  private static final int ABLAZE = 2;

  private double fireSpreadProbability;

  public FireRule(double[] variables) {
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
    int above = 0;
    int toRight = 2;
    int below = 4;
    int toLeft = 6;

    return (neighbors[above] == ABLAZE || neighbors[toRight] == ABLAZE
        || neighbors[below] == ABLAZE || neighbors[toLeft] == ABLAZE);
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
}
