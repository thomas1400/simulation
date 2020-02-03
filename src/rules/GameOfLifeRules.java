package rules;

import javafx.scene.paint.Color;

public class GameOfLifeRules extends Rules {

    public GameOfLifeRules() {
        super();
    };

    /**
     * 0 is Dead, 1 is Alive
     * @param currentState
     * @param neighborStates
     * @return
     */
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
            if (i != -1) {
                sum += i;
            }
        }
        System.out.println(sum);
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

    @Override
    public void setGlobalVariables(double[] variables) { }
}
