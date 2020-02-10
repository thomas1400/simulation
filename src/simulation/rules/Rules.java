package simulation.rules;

import java.util.List;
import java.util.Map;
import javafx.collections.ObservableList;
import javafx.scene.paint.Color;
import simulation.model.Grid;
import simulation.model.State;

public abstract class Rules {

  protected Map<String, Double[]> myVariables;
  protected Grid myGrid;

  public abstract void calculateUpdate(State state, List<State> neighbors);

  public abstract Color getStateColor(State state);

  public abstract void incrementState(State state);

  public void setGrid(Grid grid) {
    myGrid = grid;
  }

  public Map<String, Double[]> getSettings() {
    return myVariables;
  }

  public void setSetting(String name, double setting) {
    if (myVariables.containsKey(name)) {
      Double[] bounds = myVariables.get(name);
      if (bounds[0] <= setting && setting <= bounds[1]) {
        myVariables.get(name)[2] = setting;
      }
    }
    updateVariables();
  }

  protected abstract void updateVariables();

  public abstract ObservableList<String> getGlobalVarList();
}
