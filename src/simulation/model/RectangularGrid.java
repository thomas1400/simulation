package simulation.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import simulation.rules.Rules;

public class RectangularGrid extends Grid {

  public RectangularGrid(int[][] initialStates, int[][] neighborhoodShape, Rules ruleSet, boolean toroidal) {
    super(initialStates, neighborhoodShape, ruleSet, toroidal);
  }

  @Override
  protected List<State> getNeighborStates(int[] location) {
    ArrayList<State> neighborStates = new ArrayList<>();

    int x = location[0], y = location[1];

    int centerX = myNeighborhoodShape.length / 2;
    int centerY = myNeighborhoodShape[0].length / 2;
    for (int i = 0; i < myNeighborhoodShape.length; i++) {
      for (int j = 0; j < myNeighborhoodShape[0].length; j++) {
        if (myNeighborhoodShape[i][j] == NEIGHBORHOOD_CENTER) {
          centerX = i;
          centerY = j;
          break;
        }
      }
    }

    int xOffset, yOffset;
    for (int i = 0; i < myNeighborhoodShape.length; i++) {
      for (int j = 0; j < myNeighborhoodShape[0].length; j++) {
        xOffset = i - centerX;
        yOffset = j - centerY;
        if (myNeighborhoodShape[i][j] == 1) {
          if (inGridBounds(x + xOffset, y + yOffset)) {
            neighborStates.add(myStates[x + xOffset][y + yOffset]);
          } else if (isToroidal) {
            int[] tc = toroidizeCoordinates(x+xOffset, y+yOffset);
            neighborStates.add(myStates[tc[0]][tc[1]]);
          }
        }
      }
    }

    return neighborStates;
  }

  @Override
  public GridPane getGridPane(int MAX_WIDTH, int MAX_HEIGHT) {
    final double CELL_GAP = 0.25;

    GridPane gridGroup = new GridPane();
    gridGroup.setHgap(CELL_GAP);
    gridGroup.setVgap(CELL_GAP);

    int squareSize;
    if (myWidth > myHeight) {
      squareSize = (int)((float) MAX_WIDTH / myWidth - CELL_GAP);
    } else {
      squareSize = (int)((float) MAX_HEIGHT / myHeight - CELL_GAP);
    }

    for (int x = 0; x < myWidth; x++) {
      for (int y = 0; y < myHeight; y++) {
        Rectangle rect = new Rectangle();
        rect.setFill(myRuleSet.getStateColor(myStates[x][y]));
        rect.setWidth(squareSize);
        rect.setHeight(squareSize);

        final int finalX = x;
        final int finalY = y;
        rect.setOnMouseClicked(e -> rect.setFill(this.dynamicallyIncrement(finalX, finalY)));

        gridGroup.add(rect, x, y);
      }
    }

    return gridGroup;
  }
}
