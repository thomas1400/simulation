package cellsociety;

import javafx.scene.paint.Color;

public class GameOfLifeRules extends Rules {

    public GameOfLifeRules() {};

    @Override
    public int calculateNewState(int currentState, int[] neighborStates) {
        int numNeighbors = sum(neighborStates);
        if (currentState == 0) {
            if (numNeighbors == 3) {
                return 1;
            }
            return 0;
        }
        if (numNeighbors <= 1 || numNeighbors >= 4) {
            return 0;
        }
        return 1;
    }

    private int sum(int[] intArray) {
        int sum = 0;
        for (int i : intArray) {
            sum += i;
        }
        return sum;
    }

    @Override
    public Color getStateColor(int state) {
        if (state == 1) {
            return Color.BLACK;
        } else {
            return Color.WHITE;
        }
    }
}
