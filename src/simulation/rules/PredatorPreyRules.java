package simulation.rules;

import javafx.scene.paint.Color;

public class PredatorPreyRules extends Rules {

  private double prey_death_probability;
  private double predator_death_probability;
  private double predator_birth_probability;

  private static final Color[] stateColors = {Color.WHITE, Color.GREEN, Color.ORANGE};
  private static final int NUM_EXPECTED_VARIABLES = 3;
  private static final int EMPTY = 0;
  private static final int PREY = 1;
  private static final int PREDATOR = 2;

  public PredatorPreyRules(double[] variables) {
    setGlobalVariables(variables);
  }


  /**
   * @param currentState the current state of the cell
   * @param neighbors    the states of the neighbors
   * @return the next state
   */
  @Override
  public int calculateNewState(int currentState, int[] neighbors) {
    if (currentState == EMPTY) {
      return calculateEmptyCellNewState(neighbors);
    } else if (currentState == PREY) {
      return calculatePreyCellNewState(neighbors);
    } else if (currentState == PREDATOR) {
      return calculatePredatorCellNewState(neighbors);
    } else {
      throw new IllegalArgumentException("Unexpected cell state");
    }
  }

  private int calculateEmptyCellNewState(int[] neighbors) {
    return (hasStateAsNeighbor(PREY, neighbors) && !hasStateAsNeighbor(PREDATOR, neighbors)) ? PREY : EMPTY;
  }

  private int calculatePreyCellNewState(int[] neighbors) {
    if (hasStateAsNeighbor(PREDATOR, neighbors)) {
      return (Math.random() < predator_birth_probability) ? PREDATOR : PREY;
    } else {
      return (Math.random() < prey_death_probability) ? EMPTY : PREY;
    }
  }


  private int calculatePredatorCellNewState(int[] neighbors) {
    return (Math.random() < predator_death_probability || !hasStateAsNeighbor(PREY, neighbors)) ? EMPTY : PREDATOR;
  }

  private boolean hasStateAsNeighbor(int state, int[] neighbors) {
    for (int n : neighbors) {
      if (n == state) {
        return true;
      }
    }
    return false;
  }

  @Override
  public Color getStateColor(int state) {
    return stateColors[state];
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

  public int incrementState(int state) {
    return (state + 1) % (PREDATOR+1);
  }
}
