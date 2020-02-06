package simulation.rules;

import javafx.scene.paint.Color;

public interface Rule {

  int calculateNewState(int currentState, int[] neighbors);

  Color getStateColor(int state);

  void setGlobalVariables(double[] variables);

}
