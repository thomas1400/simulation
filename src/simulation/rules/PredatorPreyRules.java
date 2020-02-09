package simulation.rules;

import java.util.List;
import javafx.scene.paint.Color;
import simulation.model.Grid;
import simulation.model.RectangularGrid;
import simulation.model.State;

public class PredatorPreyRules extends Rules {

  private double prey_death_probability;
  private double predator_death_probability;
  private double predator_birth_probability;

  private static final Color[] stateColors = {Color.WHITE, Color.GREEN, Color.ORANGE};
  private static final int NUM_EXPECTED_VARIABLES = 3;
  private static final int EMPTY = 0;
  private static final int PREY = 1;
  private static final int PREDATOR = 2;

  private Grid myGrid;

  public PredatorPreyRules(double[] variables) {
    setGlobalVariables(variables);
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

  public void setGlobalVariables(double[] variables) {
    if (variables.length != NUM_EXPECTED_VARIABLES) {
      throw new IllegalArgumentException(
          "Unexpected number of variables for PredatorPreyRules. Expected 3 but got "
              + variables.length
      );
    }
    prey_death_probability = variables[0];
    predator_death_probability = variables[1];
    predator_birth_probability = variables[2];
  }

  public void incrementState(State state) {
    state.setUpdate((state.toInt() + 1) % (PREDATOR+1));
    state.update();
  }

  public void setGrid(Grid grid) {
    myGrid = grid;
  }
}
