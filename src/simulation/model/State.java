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

  /**
   * Sets this State's next state
   * @param update next state
   */
  public void setUpdate(int update) {
    myUpdate = update;
  }

  /**
   * Sets this State's next state from another State object
   * @param update State from which to take next state
   */
  public void setUpdate(State update) {
    myUpdate = update.toInt();
  }

  /**
   * Updates current state with this State's queued update
   */
  public void update() {
    isStatic = (myState == myUpdate);
    myState = myUpdate;
  }

  /**
   * Returns integer representation
   * @return
   */
  public int toInt() {
    return myState;
  }

  /**
   * Compare this state to an integer
   * @param i an int to compare
   * @return true if myState == i
   */
  public boolean equals(int i) {
    return myState == i;
  }

  /**
   * Compare two states
   * @param o another Object
   * @return true if State states are equal
   */
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
