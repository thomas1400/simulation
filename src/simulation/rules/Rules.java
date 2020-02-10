package simulation.rules;

import java.util.List;
import javafx.collections.ObservableList;
import javafx.scene.paint.Color;
import simulation.model.Grid;
import simulation.model.State;

public interface Rules {
  
  void calculateUpdate(State state, List<State> neighbors);

  Color getStateColor(State state);

  void incrementState(State state);

  void setGrid(Grid grid);

  ObservableList<String> getGlobalVarList();
}
