package cellsociety;

public class Cell {
    public Cell up;
    public Cell down;
    public Cell right;
    public Cell left;
    public int myState;
    Rules myRules;

    public Cell(Rules ruleType, int state){
        myRules = ruleType;
        myState = state;
    }
}

