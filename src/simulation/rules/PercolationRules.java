package simulation.rules;

import java.util.List;
import javafx.collections.ObservableList;
import javafx.scene.paint.Color;
import simulation.model.Grid;
import simulation.model.RectangularGrid;
import simulation.model.State;

public class PercolationRules implements Rules {

  private static final int BLOCKED = 0;
  private static final int EMPTY = 1;
  private static final int FILLED = 2;

  private Grid myGrid;

  public PercolationRules(double[] variables) { }

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
      state.setUpdate((state.equals(FILLED) || doesPercolate) ? FILLED : EMPTY);
    }
  }

  private boolean shouldPercolate(List<State> neighborStates) {
    for (int i = 0; i < neighborStates.size(); i++) {
      if (neighborStates.get(i).equals(FILLED)) {
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

  public void setGrid(Grid grid) {
    myGrid = grid;
  }

  @Override
  public ObservableList<String> getGlobalVarList() {
    return null;
  }

}
