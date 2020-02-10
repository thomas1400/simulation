package simulation.rules;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TreeMap;
import javafx.scene.paint.Color;
import simulation.model.State;

public class SegregationRules extends Rules {

  private static final int EMPTY = 0;
  private static Color[] groupColors = {Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW,
      Color.PURPLE, Color.ORANGE};
  private static final int MY_MAX_GROUPS = groupColors.length;

  private double segregation_threshold = 3;

  public SegregationRules(double[] variables) {
    myVariables = new TreeMap<>();
    double v0;
    if (variables.length == 1) {
      v0 = variables[0];
    } else {
      v0 = segregation_threshold;
    }
    myVariables.put("Segregation Threshold", new Double[]{0.0, 8.0, v0});
    updateVariables();
  }

  @Override
  public void calculateUpdate(State state, List<State> neighbors) {
    if (percentLikeNeighbors(state, neighbors) < segregation_threshold) {
      moveToEmptyCell(state);
    } else if (!state.equals(EMPTY)) {
      state.setUpdate(state);
    }
  }

  private void moveToEmptyCell(State state) {
    List<State> emptyStates = myGrid.getEmptyStates();
    if (emptyStates.size() > 0) {
      int i = (int) (Math.random() * emptyStates.size());
      emptyStates.get(i).setUpdate(state);
      emptyStates.get(i).update();
      state.setUpdate(EMPTY);
      state.update();
    }
  }

  private double percentLikeNeighbors(State state, List<State> neighbors) {
    if (state.toInt() == EMPTY) {
      return 0;
    }
    int likeNeighbors = 0;
    float numNeighbors = 0;
    for (State n : neighbors) {
      if (n.equals(state)) {
        likeNeighbors += 1;
      }
      if (!n.equals(EMPTY)) {
        numNeighbors += 1;
      }
    }
    return likeNeighbors / numNeighbors;
  }

  @Override
  public Color getStateColor(State state) {
    if (state.equals(EMPTY)) {
      return Color.WHITE;
    } else {
      return groupColors[state.toInt()-1];
    }
  }

  public void setGlobalVariables(double[] variables) {
    if (variables.length != 1) {
      throw new IllegalArgumentException(
          "Unexpected number of variables for PredatorPreyRules. Expected 1 but got "
              + variables.length
      );
    }
    segregation_threshold = variables[0];
  }

  public void incrementState(State state) {
    state.setUpdate((state.toInt() + 1) % (MY_MAX_GROUPS+1));
    state.update();
  }

  @Override
  protected void updateVariables() {
    segregation_threshold = myVariables.get("Segregation Threshold")[2];
  }

  @Override
  public List<String> getCellTypes() {
    return new ArrayList<>(
        Arrays.asList("Empty", "Group 1", "Group 2", "Group 3", "Group 4", "Group 5", "Group 6"));
  }

  @Override
  public String toString() {
    return "segregationRules";
  }
}
