package simulation.model;

public class State {

  private int myState;
  private int myUpdate;
  private int[] myLocation;
  private boolean isStatic;

  public State(int initialState, int[] location) {
    myState = initialState;
    myUpdate = 0;
    myLocation = location;
    isStatic = false;
  }

  public void setUpdate(int update) {
    myUpdate = update;
  }

  public void setUpdate(State update) {
    myUpdate = update.toInt();
  }

  public void update() {
    isStatic = (myState == myUpdate);
    myState = myUpdate;
  }

  public int toInt() {
    return myState;
  }

  public boolean equals(int i) {
    return myState == i;
  }

  @Override
  public boolean equals(Object o) {
    return (o instanceof State && ((State) o).myState == myState);
  }

  public boolean isEmpty() {
    return (myState == 0 && myUpdate == 0);
  }

  public int[] getLocation() {
    return myLocation;
  }

  public boolean isStatic() {
    return isStatic;
  }
}
