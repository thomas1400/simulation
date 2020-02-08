package simulation.rules;

import java.util.List;
import javafx.scene.paint.Color;
import simulation.model.Grid;
import simulation.model.State;

public class RockPaperScissorsRules extends Rules {

  private static final int NUM_TYPES = 3;
  private static final int ROCK = 0;
  private static final int PAPER = 1;
  private static final int SCISSORS = 2;

  private int thresholdValue;
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
    int greatestNeighbor = greatestNeighborType(neighbors);
    int enemy = mortalEnemy(state);

    if (greatestNeighbor == enemy && sumNeighborsOfType(greatestNeighbor, neighbors) >= thresholdValue){
      state.setUpdate(enemy);
    } else {
      state.setUpdate(state.toInt());
    }
  }

  private int mortalEnemy(State state) {
    int[] enemies = {PAPER, SCISSORS, ROCK};
    return enemies[state.toInt()];
  }

  private int greatestNeighborType(List<State> neighbors) {
    int[] quantities = new int[NUM_TYPES];
    for (State neighbor : neighbors){
      for (int i = 0; i < NUM_TYPES; i++){
        if (neighbor.equals(i)) {
          quantities[i] += 1;
        }
      }
    }
    return indexOfMax(quantities);
  }

  private int indexOfMax(int[] quantities) {
    int largest = 0;
    for ( int i = 1; i < quantities.length; i++ ) {
      if ( quantities[i] > quantities[largest] ) {
        largest = i;
      }
    }
    return largest;
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
}
