package cellsociety;

import javafx.scene.paint.Color;

public abstract class Rules {

    public abstract int calculateNewState(int currentState, int[] neighbors);

    public abstract Color getStateColor(int state);

    public abstract void setGlobalVariables(double[] variables);
}
