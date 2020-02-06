package view;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class Home extends Application{
  private Stage myHomeWindow;
  private Scene myHomeScene;

  /**
   * Main method for the application - entry point
   * @param args
   */
  public static void main(String[] args) {
    Application.launch(args);
  }

  @Override
  public void start(Stage primaryStage) throws Exception {
    myHomeWindow = primaryStage;
    myHomeScene = new Scene(makeHomeGrid(), 512, 512);
    myHomeWindow.setScene(myHomeScene);
    myHomeWindow.show();
  }

  private GridPane makeHomeGrid(){
    GridPane homeGridPane = new GridPane();
    Button btn = new Button("Launch");
    btn.setOnAction(e -> {
      try {
        launchSim();
      } catch (Exception ex) {
        ex.printStackTrace();
      }
    });
    homeGridPane.add(btn, 0,0);
    return homeGridPane;
  }
  private void launchSim() throws Exception {
    GUI gui = new GUI();
    gui.start(new Stage());
  }
}
