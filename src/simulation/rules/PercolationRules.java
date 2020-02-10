package simulation.rules;

import java.util.List;
import java.util.TreeMap;
import javafx.scene.paint.Color;
import simulation.model.State;

public class PercolationRules extends Rules {

  private static final int BLOCKED = 0;
  private static final int EMPTY = 1;
  private static final int FILLED = 2;
  private double percolation_probability = 1.0;

  public PercolationRules(double[] variables) {
    myVariables = new TreeMap<>();
    double v0;
    if (variables.length == 1) {
      v0 = variables[0];
    } else {
      v0 = percolation_probability;
    }
    myVariables.put("Percolation Probability", new Double[]{0.0, 1.0, v0});
    updateVariables();
  }

  /**
   * @param state   is the cell's current state
   * @param neighborStates is the cell's neighbor's states as an array
   */
  @Override
  public void calculateUpdate(State state, List<State> neighborStates) {
    boolean doesPercolate = shouldPercolate(neighborStates);
    if (state.equals(BLOCKED)) {
      state.setUpdate(BLOCKED);
    } else {
      state.setUpdate((state.equals(FILLED) || (doesPercolate && Math.random() < percolation_probability)) ? FILLED : EMPTY);
    }
  }

  private boolean shouldPercolate(List<State> neighborStates) {
    for (State neighborState : neighborStates) {
      if (neighborState.equals(FILLED)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Colors will be "Blue" for filled with water,"White" for empty, and "Black" for blocked
   * @param state
   */
  @Override
  public Color getStateColor(State state) {
    if (state.equals(FILLED)) {
      return Color.BLUE;
    } else if (state.equals(EMPTY)) {
      return Color.LIGHTGRAY;
    } else {
      return Color.BLACK;
    }
  }

  public void incrementState(State state) {
    state.setUpdate((state.toInt() + 1) % (FILLED+1));
    state.update();
  }

  @Override
  protected void updateVariables() {
    percolation_probability = myVariables.get("Percolation Probability")[2];
  }

}
