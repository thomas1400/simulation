package cellsociety;

import javafx.scene.paint.Color;

public abstract class Rules {

    public abstract int calculateNewState(int currentState, int[] neighbors);

    public abstract Color getStateColo(int state);
}
