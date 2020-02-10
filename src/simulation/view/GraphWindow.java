package simulation.view;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import simulation.controller.Simulation;

public class GraphWindow extends Application {

  private static final double WINDOW_WIDTH = 550;
  private static final double WINDOW_HEIGHT = 400;
  private static final double PADDING = 5;

  private Simulation mySimulation;
  private String myWindowTitle;
  private Stage myStage;
  private LineChart<Number, Number> graph;
  private List<Series> data;

  private int step;

  public GraphWindow(String simulationName, Simulation simulation) {
    myWindowTitle = simulationName + " Data";
    mySimulation = simulation;
  }

  @Override
  public void start(Stage primaryStage) {
    myStage = primaryStage;
    myStage.setTitle(myWindowTitle);
    Scene gridScene = new Scene(makeMasterGrid(), WINDOW_WIDTH, WINDOW_HEIGHT);

    String style = getClass().getResource("/resources/stylesheet.css").toExternalForm();
    gridScene.getStylesheets().add(style);

    myStage.setScene(gridScene);
    myStage.show();
  }

  private GridPane makeMasterGrid() {
    GridPane master = new GridPane();
    master.setHgap(5);
    master.setVgap(10);

    setupGraph();
    master.add(graph, 0, 0);

    master.setPadding(new Insets(PADDING));

    return master;
  }

  private void setupGraph() {
    NumberAxis xAxis = new NumberAxis();
    xAxis.setLabel("Time");
    xAxis.setAutoRanging(true);
    NumberAxis yAxis = new NumberAxis();
    yAxis.setLabel("Count");
    yAxis.setAutoRanging(true);
    graph = new LineChart<>(xAxis, yAxis);

    data = new ArrayList<>();
    int cellTypes = mySimulation.getCellTypes().size();
    for (int i = 0; i < cellTypes; i++) {
      Series series = new Series();
      series.setName(mySimulation.getCellTypes().get(i));
      data.add(series);
      graph.getData().add(series);
    }

    step = 0;
  }

  public void updateGraph() {
    Map<Integer, Integer> cellCounts = mySimulation.getCellCounts();
    ArrayList<Integer> keys = new ArrayList<>(cellCounts.keySet());
    Collections.sort(keys);
    for (int i = 0; i < cellCounts.keySet().size(); i++) {
      data.get(i).getData().add(new XYChart.Data(step, cellCounts.get(keys.get(i))));
    }
    step += 1;
  }

  @Override
  public void stop() {
    myStage.close();
  }
}