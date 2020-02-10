package simulation.view;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Pair;
import simulation.controller.Simulation;

public class SettingsWindow extends Application {

  private static final double WINDOW_WIDTH = 300;
  private static final double WINDOW_HEIGHT = 400;
  private static final double PADDING = 5;

  private Simulation mySimulation;
  private String myWindowTitle;
  private Stage myStage;

  public SettingsWindow(String simulationName, Simulation simulation) {
    myWindowTitle = simulationName + " Settings";
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

    Map<String, Double[]> settings = mySimulation.getSettings();
    int count = 0;
    for (String key : settings.keySet()) {
      master.add(new Label(key + ": "), 0, count);
      master.add(makeSpinner(key, settings.get(key)), 1, count);
      count += 1;
    }

    master.setPadding(new Insets(PADDING));

    return master;
  }

  private Spinner<Double> makeSpinner(String name, Double[] setting) {
    Spinner<Double> spinner = new Spinner<>();

    SpinnerValueFactory<Double> valueFactory = //
        new SpinnerValueFactory.DoubleSpinnerValueFactory(setting[0], setting[1], setting[2], 0.1);

    spinner.setValueFactory(valueFactory);
    spinner.valueProperty().addListener(e -> mySimulation.setSetting(name, spinner.getValue()));

    return spinner;
  }

  @Override
  public void stop() {
    myStage.close();
  }
}
