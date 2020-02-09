package simulation.rules;

import java.util.List;
import javafx.scene.paint.Color;
import simulation.model.Grid;
import simulation.model.State;

public abstract class Rules {
  
  public abstract void calculateUpdate(State state, List<State> neighbors);

  public abstract Color getStateColor(State state);

  public abstract void incrementState(State state);

  public abstract void setGrid(Grid grid);
}
