package simulation.rules;

import javafx.scene.paint.Color;

public class FireRule implements Rule {

  private double fireSpreadProbability;

  public FireRule() {
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
    if (currentState == 1 && adjacentNeighborIsOnFire(neighbors)
        && Math.random() < fireSpreadProbability) {
      return 2;
    } else if (currentState == 2) {
      return 0;
    }
    return currentState;
  }

  private boolean adjacentNeighborIsOnFire(int[] neighbors) {
    return (neighbors[0] == 2 || neighbors[2] == 2 || neighbors[4] == 2 || neighbors[6] == 2);
  }

  /**
   * Burnt is Black, Alive is Green, Burning is Red
   *
   * @param state
   * @return
   */
  @Override
  public Color getStateColor(int state) {
    if (state == 0) {
      return Color.BLACK;
    } else if (state == 1) {
      return Color.GREEN;
    } else {
      return Color.RED;
    }
  }

  @Override
  public void setGlobalVariables(double[] variables) {
    fireSpreadProbability = variables[0];
  }
}
