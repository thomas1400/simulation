package simulation.rules;

import java.util.List;
import java.util.Map;
import javafx.scene.paint.Color;
import simulation.model.Grid;
import simulation.model.State;

public abstract class Rules {

  protected Map<String, Double[]> myVariables;
  protected Grid myGrid;

  /**
   * Calculates a cell's next state given its current state and neighbors' states
   * @param state current state
   * @param neighbors neighbors' states
   */
  public abstract void calculateUpdate(State state, List<State> neighbors);

  /**
   * Gets a color for a given state
   * @param state current state
   * @return Color for the state
   */
  public abstract Color getStateColor(State state);

  /**
   * Increments a cell's state by 1
   * @param state cell State
   */
  public abstract void incrementState(State state);

  public void setGrid(Grid grid) {
    myGrid = grid;
  }

  /**
   * Gets a list of the applicable settings for this Rules object
   * @return Map of setting name to Double[]: [lowerBound, upperBound, currentSetting]
   */
  public Map<String, Double[]> getSettings() {
    return myVariables;
  }

  /**
   * Sets a given setting for this Rules object
   * @param name the setting to change
   * @param setting the new value
   */
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

  /**
   * Gets a list of the cell types in this Rules class
   * @return List of cell type names
   */
  public abstract List<String> getCellTypes();

}
