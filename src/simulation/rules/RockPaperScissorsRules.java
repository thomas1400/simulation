package simulation.rules;

import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.paint.Color;
import simulation.model.Grid;
import simulation.model.State;

public class RockPaperScissorsRules implements Rules {

  private static final int NUM_TYPES = 3;
  private static final int ROCK = 0;
  private static final int PAPER = 1;
  private static final int SCISSORS = 2;

  private int thresholdValue;
  private int randomThresholdAdjustment;
  private Grid myGrid;

  public RockPaperScissorsRules(double[] variables) {
    setGlobalVariables(variables);
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
  }

  public void incrementState(State state) {
    state.setUpdate((state.toInt() + 1) % (SCISSORS+1));
    state.update();
  }

  public void setGrid(Grid grid) {
    myGrid = grid;
  }

  @Override
  public ObservableList<String> getGlobalVarList() {
    return FXCollections.observableArrayList("Threshold Value");
  }
}
