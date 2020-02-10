package simulation.events;

import exceptions.MalformedXMLException;

/**
 * Interface for classes interested in knowing when the simulation updates itself
 */
public interface IUpdate {

  void simulationUpdate(int newX, int newY) throws MalformedXMLException;

  void errorAlert();
}
