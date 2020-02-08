package simulation.rules;

import java.util.List;
import javafx.scene.paint.Color;
import simulation.model.Grid;
import simulation.model.State;

public class FireRules extends Rules {

  private static final int EMPTY = 0;
  private static final int TREE = 1;
  private static final int ABLAZE = 2;

  private double fireSpreadProbability;
  private Grid myGrid;

  public FireRules(double[] variables) {
    setGlobalVariables(variables);
  }

  /**
   * 0 corresponds to Empty, 1 Corresponds to Alive, 2 Corresponds to Burning
   *
   * @param state
   * @param neighbors
   */
  @Override
  public void calculateUpdate(State state, List<State> neighbors) {
    if (state.equals(TREE) && adjacentNeighborIsOnFire(neighbors)
        && Math.random() < fireSpreadProbability) {
      state.setUpdate(ABLAZE);
    } else if (state.equals(ABLAZE)) {
      state.setUpdate(EMPTY);
    } else {
      state.setUpdate(state.toInt());
    }
  }

  private boolean adjacentNeighborIsOnFire(List<State> neighbors) {
    for (State neighbor : neighbors) {
      if (neighbor.equals(ABLAZE)) {
        return true;
      }
    }
    return false;
  }

  /**
   * @param state
   * @return
   */
  @Override
  public Color getStateColor(State state) {
    if (state.equals(EMPTY)) {
      return Color.BLACK;
    } else if (state.equals(TREE)) {
      return Color.GREEN;
    } else {
      return Color.RED;
    }
  }

  public void setGlobalVariables(double[] variables) {
    fireSpreadProbability = variables[0];
  }

  public void incrementState(State state) {
    state.setUpdate((state.toInt() + 1) % (ABLAZE+1));
    state.update();
  }

  public void setGrid(Grid grid) {
    myGrid = grid;
  }
}
