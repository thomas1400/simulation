package simulation.rules;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TreeMap;
import javafx.scene.paint.Color;
import simulation.model.State;

public class GameOfLifeRules extends Rules {

  private static final int DEAD = 0;
  private static final int ALIVE = 1;

  private double MIN_TO_DIE = 1;
  private double MAX_TO_DIE = 4;
  private double NUM_TO_REVIVE = 3;

  /**
   * Constructs a game of life class
   * @param variables initial parameters/settings
   */
  public GameOfLifeRules(double[] variables) {
    myVariables = new TreeMap<>();
    double v0, v1, v2;
    if (variables.length == 3) {
      v0 = variables[0];
      v1 = variables[1];
      v2 = variables[2];
    } else {
      v0 = MIN_TO_DIE;
      v1 = MAX_TO_DIE;
      v2 = NUM_TO_REVIVE;
    }
    myVariables.put("Starvation Threshold", new Double[]{0.0, 8.0, v0});
    myVariables.put("Overpopulation Threshold", new Double[]{1.0, 8.0, v1});
    myVariables.put("Birth Threshold", new Double[]{1.0, 8.0, v2});
    updateVariables();
  }

  /**
   * @param state cell state
   * @param neighborStates list of neighbors states
   */
  @Override
  public void calculateUpdate(State state, List<State> neighborStates) {
    int numNeighbors = sum(neighborStates);
    if (state.equals(DEAD)) {
      state.setUpdate((numNeighbors == NUM_TO_REVIVE) ? ALIVE : DEAD);
    } else {
      state.setUpdate((numNeighbors <= MIN_TO_DIE || numNeighbors >= MAX_TO_DIE) ? DEAD : ALIVE);
    }
  }

  private int sum(List<State> states) {
    int sum = 0;
    for (State s : states) {
      sum += s.toInt();
    }
    return sum;
  }

  /**
   * return's state color
   * @param state current state
   * @return Color state color
   */
  @Override
  public Color getStateColor(State state) {
    return (state.equals(ALIVE)) ? Color.BLACK : Color.WHITE;
  }

  /**
   * increments the state
   * @param state cell State
   */
  public void incrementState(State state) {
    state.setUpdate((state.toInt() + 1) % (ALIVE + 1));
    state.update();
  }

  @Override
  protected void updateVariables() {
    MIN_TO_DIE = myVariables.get("Starvation Threshold")[2];
    MAX_TO_DIE = myVariables.get("Overpopulation Threshold")[2];
    NUM_TO_REVIVE = myVariables.get("Birth Threshold")[2];
  }

  /**
   * returns possible cell types
   * @return cell types
   */
  @Override
  public List<String> getCellTypes() {
    return new ArrayList<>(Arrays.asList("Dead", "Alive"));
  }

  /**
   * name of simulation rules
   * @return sim rules
   */
  @Override
  public String toString() {
    return "gameOfLifeRules";
  }
}
