package simulation.rules;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TreeMap;
import javafx.scene.paint.Color;
import simulation.model.State;

public class FireRules extends Rules {

  private static final int EMPTY = 0;
  private static final int TREE = 1;
  private static final int ABLAZE = 2;

  private Double fireSpreadProbability;

  /**
   * Constructs a Fire Rules object that can be called and referenced in the future
   * @param variables takes in only the first of possible specified settings values
   */
  public FireRules(double[] variables) {
    myVariables = new TreeMap<>();
    fireSpreadProbability = variables[0];
    myVariables.put("Fire Spread Probability", new Double[]{0.0, 1.0, fireSpreadProbability});
  }

  /**
   * determines the new state based on a "cell's" neighbors
   * @param state current state
   * @param neighbors neighbors' states
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
   * Returns the Color of a specific cell based on its specific state
   * @param state current state
   * @return Color the specific color that cell/state should be.
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

  /**
   * Searches the settings/variables map for a new probability adjustment
   */
  public void updateVariables() {
    fireSpreadProbability = myVariables.get("Fire Spread Probability")[2];
  }

  /**
   * Returns a list of all possible cell types native to this simulation
   * @return list of inherent cell types possible
   */
  @Override
  public List<String> getCellTypes() {
    return new ArrayList<>(Arrays.asList("Empty", "Tree", "Ablaze"));
  }

  /**
   * Actually update's the cell's state
   * @param state cell State
   */
  public void incrementState(State state) {
    state.setUpdate((state.toInt() + 1) % (ABLAZE + 1));
    state.update();
  }

  /**
   * Returns the specific string name needed to identify the object
   * @return the rules' name
   */
  @Override
  public String toString() {
    return "fireRules";
  }
}
