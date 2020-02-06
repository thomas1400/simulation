package simulation.view;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class Home extends Application {

  /**
   * Main method for the application - entry point
   */
  public static void main(String[] args) {
    Application.launch(args);
  }

  @Override
  public void start(Stage primaryStage) {
    Scene myHomeScene = new Scene(makeHomeGrid(), 512, 512);
    primaryStage.setScene(myHomeScene);
    primaryStage.show();
  }

  private GridPane makeHomeGrid(){
    GridPane homeGridPane = new GridPane();
    Button btn = new Button("Launch");
    btn.setOnAction(e -> launchSim());
    homeGridPane.add(btn, 0,0);
    return homeGridPane;
  }
  private void launchSim() {
    GUI gui = new GUI();
    gui.start(new Stage());
  }
}
