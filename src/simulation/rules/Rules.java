package simulation.rules;

import javafx.scene.paint.Color;
import simulation.model.Grid;

public abstract class Rules {

  private Grid myGrid;

  public abstract int calculateNewState(int currentState, int[] neighbors);

  public abstract Color getStateColor(int state);

  public abstract int incrementState(int state);
}
