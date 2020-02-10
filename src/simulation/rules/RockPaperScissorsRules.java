package simulation.rules;

import java.util.List;
import java.util.TreeMap;
import javafx.scene.paint.Color;
import simulation.model.State;

public class RockPaperScissorsRules implements Rules {

  private static final int ROCK = 0;
  private static final int PAPER = 1;
  private static final int SCISSORS = 2;

  private int thresholdValue = 3;
  private int randomThresholdAdjustment = 2;

  public RockPaperScissorsRules(double[] variables) {
    myVariables = new TreeMap<>();
    double v0, v1;
    if (variables.length == 2) {
      v0 = variables[0];
      v1 = variables[1];
    } else {
      v0 = thresholdValue;
      v1 = randomThresholdAdjustment;
    }
    myVariables.put("Random Threshold Adjustment", new Double[]{0.0, 4.0, v1});
    myVariables.put("Loss Threshold", new Double[]{0.0, 8.0, v0});
    updateVariables();
  }

  /**
   * State 0 = rock, 1 = paper, 2 = scissors
   * @param state the current numerical state of the cell
   * @param neighbors a list of all neighbors
   */
  @Override
  public void calculateUpdate(State state, List<State> neighbors) {
    int enemy = mortalEnemy(state);
    int numberOfEnemies = sumNeighborsOfType(enemy, neighbors);

    if (numberOfEnemies >= thresholdValue + (int)(Math.random() * randomThresholdAdjustment)){
      state.setUpdate(enemy);
    } else {
      state.setUpdate(state.toInt());
    }
  }

  private int mortalEnemy(State state) {
    int[] enemies = {PAPER, SCISSORS, ROCK};
    return enemies[state.toInt()];
  }

  private int sumNeighborsOfType(int type, List<State> neighbors){
    int sum = 0;
    for (State neighbor : neighbors){
      if (neighbor.equals(type)) {
        sum ++;
      }
    }
    return sum;
  }

  @Override
  public Color getStateColor(State state) {
    if (state.equals(ROCK)) {
      return Color.GREEN;
    } else if (state.equals(PAPER)) {
      return Color.BLUE;
    } else if (state.equals(SCISSORS)) {
      return Color.RED;
    } else {
      throw new IllegalArgumentException("Unexpected cell state");
    }
  }

  public void setGlobalVariables(double[] variables) {
    thresholdValue = (int)variables[0];
    randomThresholdAdjustment = (int)variables[1];
  }

  public void incrementState(State state) {
    state.setUpdate((state.toInt() + 1) % (SCISSORS+1));
    state.update();
  }

  @Override
  protected void updateVariables() {
    thresholdValue = (int) myVariables.get("Loss Threshold")[2].doubleValue();
    randomThresholdAdjustment = (int) myVariables.get("Random Threshold Adjustment")[2].doubleValue();
  }

  @Override
  public ObservableList<String> getGlobalVarList() {
    return FXCollections.observableArrayList("Threshold Value");
  }
}
