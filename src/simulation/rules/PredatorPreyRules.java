package simulation.rules;

import java.util.List;
import java.util.TreeMap;
import javafx.scene.paint.Color;
import simulation.model.State;

public class PredatorPreyRules implements Rules {

  private double prey_death_probability;
  private double predator_death_probability;
  private double predator_birth_probability;
  private static final int NUM_EXPECTED_VARIABLES = 3;

  private static final Color[] stateColors = {Color.WHITE, Color.GREEN, Color.ORANGE};
  private static final int EMPTY = 0;
  private static final int PREY = 1;
  private static final int PREDATOR = 2;

  public PredatorPreyRules(double[] variables) {
    myVariables = new TreeMap<>();
    double v0, v1, v2;
    if (variables.length == NUM_EXPECTED_VARIABLES) {
      v0 = variables[0];
      v1 = variables[1];
      v2 = variables[2];
    } else {
      v0 = prey_death_probability;
      v1 = predator_death_probability;
      v2 = predator_birth_probability;
    }
    myVariables.put("Predator Birth Probability", new Double[]{0.0, 1.0, v2});
    myVariables.put("Predator Death Probability", new Double[]{0.0, 1.0, v1});
    myVariables.put("Prey Death Probability", new Double[]{0.0, 1.0, v0});
    updateVariables();
  }


  /**
   * @param state the current state of the cell
   * @param neighbors    the states of the neighbors
   */
  @Override
  public void calculateUpdate(State state, List<State> neighbors) {
    if (state.equals(EMPTY)) {
      state.setUpdate(calculateEmptyCellNewState(neighbors));
    } else if (state.equals(PREY)) {
      state.setUpdate(calculatePreyCellNewState(neighbors));
    } else if (state.equals(PREDATOR)) {
      state.setUpdate(calculatePredatorCellNewState(neighbors));
    } else {
      throw new IllegalArgumentException("Unexpected cell state");
    }
  }

  private int calculateEmptyCellNewState(List<State> neighbors) {
    return (hasStateAsNeighbor(PREY, neighbors) && !hasStateAsNeighbor(PREDATOR, neighbors)) ? PREY : EMPTY;
  }

  private int calculatePreyCellNewState(List<State> neighbors) {
    if (hasStateAsNeighbor(PREDATOR, neighbors)) {
      return (Math.random() < predator_birth_probability) ? PREDATOR : PREY;
    } else {
      return (Math.random() < prey_death_probability) ? EMPTY : PREY;
    }
  }


  private int calculatePredatorCellNewState(List<State> neighbors) {
    return (Math.random() < predator_death_probability || !hasStateAsNeighbor(PREY, neighbors)) ? EMPTY : PREDATOR;
  }

  private boolean hasStateAsNeighbor(int state, List<State> neighbors) {
    for (State n : neighbors) {
      if (n.equals(state)) {
        return true;
      }
    }
    return false;
  }

  @Override
  public Color getStateColor(State state) {
    return stateColors[state.toInt()];
  }

  public void incrementState(State state) {
    state.setUpdate((state.toInt() + 1) % (PREDATOR+1));
    state.update();
  }

  @Override
  protected void updateVariables() {
    prey_death_probability = myVariables.get("Prey Death Probability")[2];
    predator_death_probability = myVariables.get("Predator Death Probability")[2];
    predator_birth_probability = myVariables.get("Predator Birth Probability")[2];
  }

  @Override
  public ObservableList<String> getGlobalVarList() {
    return FXCollections.observableArrayList("Prey Death %", "Predator Death %", "Predator Birth %");
  }
}
