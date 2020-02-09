package simulation.rules;

import java.util.List;
import javafx.scene.paint.Color;
import simulation.model.Grid;
import simulation.model.State;

public class GameOfLifeRules extends Rules {

  private static final int DEAD = 0;
  private static final int ALIVE = 1;
  private static final int MY_MAX_STATE = 1;

  private static final int MIN_TO_DIE = 1;
  private static final int MAX_TO_DIE = 4;
  private static final int NUM_TO_REVIVE = 3;
  private Grid myGrid;

  public GameOfLifeRules(double[] variables) { }

  /**
   * @param state
   * @param neighborStates
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

  @Override
  public Color getStateColor(State state) {
    return (state.equals(ALIVE)) ? Color.BLACK : Color.WHITE;
  }

  public void incrementState(State state) {
    state.setUpdate((state.toInt() + 1) % (ALIVE+1));
    state.update();
  }


  public void setGrid(Grid grid) {
    myGrid = grid;
  }
}
