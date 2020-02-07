package simulation.model;

import java.lang.reflect.Array;
import javafx.scene.paint.Color;

/**
 * Pass this class if Grid Information simply needs to be ACCESSED
 */
public interface ImmutableGrid {

  //NO GETTER METHODS CAN RETURN ENTIRE GRID, just individual characteristics of it

  public Color getCellColorByCoordinates();

  public Array getEmptyCellsCoordinates();

}
