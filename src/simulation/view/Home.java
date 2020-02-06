package simulation.view;

import java.io.IOException;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

public class Home extends Application {

  private final int WINDOW_WIDTH = 512;
  private final int WINDOW_HEIGHT = 512;

  /**
   * Main method for the application - entry point
   */
  public static void main(String[] args) {
    Application.launch(args);
  }

  @Override
  public void start(Stage primaryStage) {
    Scene myHomeScene = new Scene(makeHomeGrid(), WINDOW_WIDTH, WINDOW_HEIGHT);
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
