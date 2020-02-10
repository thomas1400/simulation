package simulation.model;

import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import simulation.rules.Rules;

public class RectangularGrid extends Grid {

  public RectangularGrid(int[][] initialStates, int[][] neighborhoodShape, Rules ruleSet, boolean toroidal) {
    super(initialStates, neighborhoodShape, ruleSet, toroidal);
  }

  @Override
  public Pane getGridPane(int MAX_SIZE) {
    final double CELL_GAP = 0.25;

    GridPane gridGroup = new GridPane();
    gridGroup.setHgap(CELL_GAP);
    gridGroup.setVgap(CELL_GAP);

    int squareSize;
    if (myWidth > myHeight) {
      squareSize = (int)((float) MAX_SIZE / myWidth - CELL_GAP);
    } else {
      squareSize = (int)((float) MAX_SIZE / myHeight - CELL_GAP);
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
