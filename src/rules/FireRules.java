package rules;

import javafx.scene.paint.Color;

public class FireRules extends Rules {

  @Override
  public int calculateNewState(int currentState, int[] neighbors) {
    return 0;
  }

  @Override
  public Color getStateColor(int state) {
    return null;
  }
}
