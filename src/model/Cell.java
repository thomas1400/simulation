package model;

import javafx.scene.paint.Color;
import rules.Rule;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class Cell {
    private Cell[] myNeighbors; // Indexed starting with 'up' neighbor and moving clockwise.
    private Rule myRules;

    private int myState; // convert to enum
    private int myNextState;
    private Color myColor;

    public Cell(Rule ruleType, int state){
        myRules = ruleType;
        myState = state;
        myColor = myRules.getStateColor(state);
        myNeighbors = new Cell[8];
    }

    // 0: up, 1: up-right, 2: right, 3: down-right, 4: down, 5: down-left; 6: left; 7: up-left
    public void setNeighbor(int index, Cell pointer) {
        myNeighbors[index] = pointer;
    }

    public void getNextState(){
        int[] neighborStates = new int[myNeighbors.length];
        for (int i = 0; i < myNeighbors.length; i++) {
            if (myNeighbors[i] != null) {
                neighborStates[i] = myNeighbors[i].myState;
            }
        }

        myNextState = myRules.calculateNewState(myState, neighborStates);
    }

    public void updateState(){
        if (myNextState < 0) {
            ArrayList<Cell> shuffled = new ArrayList<>(Arrays.asList(myNeighbors));
            Collections.shuffle(shuffled);

            for (Cell n : shuffled) {
                if (n != null && n.myNextState < 0) {
                    int temp = myState;
                    myState = n.myState;
                    n.myState = temp;
                    n.myNextState = temp;
                    n.myColor = myRules.getStateColor(n.myState);
                    myColor = myRules.getStateColor(myState);
                    break;
                }
            }
        } else {
            myState = myNextState;
            myColor = myRules.getStateColor(myState);
        }
    }

    public int getState() {
        return myState;
    }

    public Color getColor() {
        return myColor;
    }
}

