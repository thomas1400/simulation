package rules;

import javafx.scene.paint.Color;

public class PredatorPreyRule implements Rule {

  private double prey_death_probability;
  private double predator_death_probability;
  private double predator_birth_probability;

  /**
   * State: 0 for empty, 1 for prey, 2 for predator
   *
   * @param currentState the current state of the cell
   * @param neighbors    the states of the neighbors
   * @return the next state
   */
  @Override
  public int calculateNewState(int currentState, int[] neighbors) {
    if (currentState == 0) {
      if (hasStateAsNeighbor(1, neighbors) && !hasStateAsNeighbor(2, neighbors)) {
        return 1;
      } else {
        return 0;
      }
    } else if (currentState == 1) {
      if (hasStateAsNeighbor(2, neighbors)) {
        if (Math.random() < predator_birth_probability) {
          return 2;
        } else {
          return 0;
        }
      } else if (Math.random() < prey_death_probability) {
        return 0;
      } else {
        return 1;
      }
    } else if (currentState == 2) {
      if (Math.random() < predator_death_probability || !hasStateAsNeighbor(1, neighbors)) {
        return 0;
      } else {
        return 2;
      }
    } else {
      throw new IllegalArgumentException("Unexpected cell state");
    }
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
    Color[] stateColors = {Color.WHITE, Color.GREEN, Color.ORANGE};
    return stateColors[state];
  }

  @Override
  public void setGlobalVariables(double[] variables) {
    if (variables.length != 3) {
      throw new IllegalArgumentException(
          "Unexpected number of variables for PredatorPreyRules. Expected 3 but got "
              + variables.length
      );
    }
    prey_death_probability = variables[0];
    predator_death_probability = variables[1];
    predator_birth_probability = variables[2];
  }
}
