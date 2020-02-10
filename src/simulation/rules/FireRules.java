package simulation.rules;

import java.util.List;
import java.util.TreeMap;
import javafx.scene.paint.Color;
import simulation.model.State;

public class FireRules extends Rules {

  private static final int EMPTY = 0;
  private static final int TREE = 1;
  private static final int ABLAZE = 2;

  private Double fireSpreadProbability;

  public FireRules(double[] variables) {
    myVariables = new TreeMap<>();
    fireSpreadProbability = variables[0];
    myVariables.put("Fire Spread Probability", new Double[]{0.0, 1.0, fireSpreadProbability});
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

  public void updateVariables() {
    fireSpreadProbability = myVariables.get("Fire Spread Probability")[2];
  }

  public void incrementState(State state) {
    state.setUpdate((state.toInt() + 1) % (ABLAZE + 1));
    state.update();
  }

}
