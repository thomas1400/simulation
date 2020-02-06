package rules;

import javafx.scene.paint.Color;

public class RockPaperScissorsRule implements Rule {
  private int thresholdValue;

  /**
   * State 0 = rock, 1 = paper, 2 = scissors
   * @param currentState
   * @param neighbors
   * @return
   */
  @Override
  public int calculateNewState(int currentState, int[] neighbors) {
    int greatestNeighbor = greatestNeighborType(neighbors);
    int enemy = mortalEnemy(currentState);

    if (greatestNeighbor == enemy){
      if (sumNeighborsOfType(greatestNeighbor, neighbors) >= thresholdValue){
        return enemy;
      }
    }

    return currentState;
  }

  private int mortalEnemy(int currentState) {
    int[] enemies = {1,2,0};
    return enemies[currentState];
  }

  private int greatestNeighborType(int[] neighbors) {
    int[] quantities = new int[3];
    for (int neighbor:neighbors){
      for (int i = 0; i < 3; i++){
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
    if (state == 0) {
      return Color.GREEN;
    } else if (state == 1) {
      return Color.BLUE;
    } else if (state == 2) {
      return Color.RED;
    } else {
      throw new IllegalArgumentException("Unexpected cell state");
    }
  }

  @Override
  public void setGlobalVariables(double[] variables) {
    thresholdValue = (int)variables[0];
  }
}
