package simulation.model;

import java.lang.reflect.Array;
import javafx.scene.paint.Color;

/**
 * Pass this class if Grid needs to be changed
 */
public class Grid implements ImmutableGrid {

  @Override
  public Color getCellColorByCoordinates() {
    return null;
  }

  @Override
  public Array getEmptyCellsCoordinates() {
    return null;
  }

}
