package simulation.rules;

import javafx.scene.paint.Color;

public class PercolationRule implements Rule {

  private static final int BLOCKED = 0;
  private static final int EMPTY = 1;
  private static final int FILLED = 2;

  public PercolationRule(double[] variables) { }

  /**
   * @param currentState   is the cell's current state
   * @param neighborStates is the cell's neighbor's states as an array
   * @return returns new state of cell
   */
  @Override
  public int calculateNewState(int currentState, int[] neighborStates) {
    boolean doesPercolate = shouldPercolate(neighborStates);
    if (currentState == BLOCKED) {
      return BLOCKED;
    } else {
      return (currentState == FILLED || doesPercolate) ? FILLED : EMPTY;
    }
  }

  private boolean shouldPercolate(int[] neighborStates) {
    // Only check directly adjacent squares, not diagonals (indices 0, 2, 4, 8)
    for (int i = 0; i < neighborStates.length; i += 2) {
      if (neighborStates[i] == FILLED) {
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
    if (state == FILLED) {
      return Color.BLUE;
    } else if (state == EMPTY) {
      return Color.LIGHTGRAY;
    } else {
      return Color.BLACK;
    }
  }

}
