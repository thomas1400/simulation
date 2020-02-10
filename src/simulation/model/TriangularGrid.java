package simulation.model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Polygon;
import simulation.rules.Rules;

public class TriangularGrid extends Grid {

  public TriangularGrid(int[][] initialStates, int[][] neighborhoodShape, Rules ruleset, boolean toroidal) {
    super(initialStates, neighborhoodShape, ruleset, toroidal);
  }

  @Override
  public Pane getGridPane(int MAX_SIZE) {
    Pane gridPane = new Pane();

    double tWidth = (float) MAX_SIZE / (myWidth / 2.0 + 0.5);
    double tHeight = (float) MAX_SIZE / myHeight;

    double rowY = 0.0;
    for (int y = 0; y < myHeight; y++) {
      LinkedList<Double[]> trianglePoints = new LinkedList<>();

      int nextDirection = (y % 2 == 0) ? 1 : -1;
      if (y % 2 == 1) {
        rowY += 2 * tHeight;
      }

      trianglePoints.add(new Double[]{0.0, 0.0});
      trianglePoints.add(new Double[]{0.0, rowY});
      trianglePoints.add(new Double[]{tWidth / 2.0, tHeight*nextDirection + rowY});
      nextDirection *= -1;

      for (int x = 0; x < myWidth; x++) {
        trianglePoints.removeFirst();
        Double[] lastPoint = trianglePoints.getLast();
        trianglePoints.addLast(new Double[]{lastPoint[0] + tWidth/2.0, lastPoint[1] + tHeight * nextDirection});
        nextDirection *= -1;

        Polygon triangle = new Polygon();
        triangle.setFill(myRuleSet.getStateColor(myStates[x][y]));
        for (Double[] point : trianglePoints) {
          triangle.getPoints().addAll(point);
        }

        final int finalX = x;
        final int finalY = y;
        triangle.setOnMouseClicked(e -> triangle.setFill(this.dynamicallyIncrement(finalX, finalY)));

        gridPane.getChildren().add(triangle);
      }
    }

    gridPane.autosize();

    return gridPane;
  }

  @Override
  protected List<State> getNeighborStates(int[] location) {
    ArrayList<State> neighborStates = new ArrayList<>();

    int x = location[0], y = location[1];

    int centerX = myNeighborhoodShape[0].length / 2;
    int centerY = myNeighborhoodShape.length / 2;
    for (int ny = 0; ny < myNeighborhoodShape.length; ny++) {
      for (int nx = 0; nx < myNeighborhoodShape[ny].length; nx++) {
        if (myNeighborhoodShape[ny][nx] == NEIGHBORHOOD_CENTER) {
          centerX = nx;
          centerY = ny;
          break;
        }
      }
    }

    int flip = ((location[0] + location[1]) % 2 == 0) ? -1 : 1;
    int xOffset, yOffset;
    for (int ny = 0; ny < myNeighborhoodShape.length; ny++) {
      for (int nx = 0; nx < myNeighborhoodShape[ny].length; nx++) {
        xOffset = nx - centerX;
        yOffset = ny - centerY;
        if (myNeighborhoodShape[ny][nx] == 1) {
          if (inGridBounds(x + flip*xOffset, y + flip*yOffset)) {
            neighborStates.add(myStates[x + flip*xOffset][y + flip*yOffset]);
          } else if (isToroidal) {
            int[] tc = toroidizeCoordinates(x+flip*xOffset, y+flip*yOffset);
            neighborStates.add(myStates[tc[0]][tc[1]]);
          }
        }
      }
    }

    return neighborStates;
  }

}
