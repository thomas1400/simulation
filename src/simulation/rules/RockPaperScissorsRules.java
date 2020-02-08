package simulation.rules;

import javafx.scene.paint.Color;

public class RockPaperScissorsRules extends Rules {

  private static final int NUM_TYPES = 3;
  private static final int ROCK = 0;
  private static final int PAPER = 1;
  private static final int SCISSORS = 2;

  private int thresholdValue;

  public RockPaperScissorsRules(double[] variables) {
    setGlobalVariables(variables);
  }

  /**
   * State 0 = rock, 1 = paper, 2 = scissors
   * @param currentState the current numerical state of the cell
   * @param neighbors a list of all neighbors
   * @return the cell's new state
   */
  @Override
  public int calculateNewState(int currentState, int[] neighbors) {
    int greatestNeighbor = greatestNeighborType(neighbors);
    int enemy = mortalEnemy(currentState);

    if (greatestNeighbor == enemy && sumNeighborsOfType(greatestNeighbor, neighbors) >= thresholdValue){
        return enemy;
    }

    return currentState;
  }

  private int mortalEnemy(int currentState) {
    int[] enemies = {PAPER, SCISSORS, ROCK};
    return enemies[currentState];
  }

  private int greatestNeighborType(int[] neighbors) {
    int[] quantities = new int[NUM_TYPES];
    for (int neighbor:neighbors){
      for (int i = 0; i < NUM_TYPES; i++){
        if (neighbor == i) {
          quantities[i] ++;
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

  private int sumNeighborsOfType(int type, int[] neighbors){
    int sum = 0;
    for (int neighbor:neighbors){
      if (neighbor == type) {
        sum ++;
      }
    }
    return sum;
  }

  @Override
  public Color getStateColor(int state) {
    if (state == ROCK) {
      return Color.GREEN;
    } else if (state == PAPER) {
      return Color.BLUE;
    } else if (state == SCISSORS) {
      return Color.RED;
    } else {
      throw new IllegalArgumentException("Unexpected cell state");
    }
  }

  public void setGlobalVariables(double[] variables) {
    thresholdValue = (int)variables[0];
  }

  public int incrementState(int state) {
    return (state + 1) % (SCISSORS+1);
  }
}
