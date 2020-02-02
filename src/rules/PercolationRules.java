package rules;

import javafx.scene.paint.Color;

public class PercolationRules extends Rules {

  /**
   * State 2 corresponds with Filled, 1 with Empty, and 0 with Blocked
   *
   * @param currentState is the cell's current state
   * @param neighborStates is the cell's neighbor's states as an array
   * @return returns new state of cell
   */
  @Override
  public int calculateNewState(int currentState, int[] neighborStates) {
    boolean doesPercolate = shouldPercolate(neighborStates);
    if (currentState == 0) {
      return 0;
    } else if (currentState == 2 || doesPercolate) {
      return 2;
    } else {
      return 1;
    }
  }

  private boolean shouldPercolate(int[] neighborStates) {
    for (int neighborState : neighborStates){
      if (neighborState == 2) {
        return true;
      }
    }
    return false;
  }

  /**
   * Colors will be "Blue" for filled with water,"White" for empty, and "Black" for blocked
   */
  @Override
  public Color getStateColor(int state) {
    if (state == 2) {
      return Color.BLUE;
    } else if (state == 1) {
      return Color.LIGHTGRAY;
    } else {
      return Color.BLACK;
    }
  }

  @Override
  public void setGlobalVariables(double[] variables) {

  }
}
