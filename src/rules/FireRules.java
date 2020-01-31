package rules;

import javafx.scene.paint.Color;

public class FireRules extends Rules {
  private double fireSpreadProbability;

  public FireRules(double spreadProb) {
    super();
    fireSpreadProbability = spreadProb;

  }
  /**
   * 0 corresponds to Burnt, 1 Corresponds to Alive, 2 Corresponds to Burning
   * @param currentState
   * @param neighbors
   * @return
   */
  @Override
  public int calculateNewState(int currentState, int[] neighbors) {
    //TODO
    // - add code to calculate new state of cell

    return 0;
  }

  /**
   * Burnt is Black, Alive is Green, Burning is Red
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
}
