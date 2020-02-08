package simulation.view;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

public class Home extends Application {

  private static final int WINDOW_WIDTH = 512;
  private static final int WINDOW_HEIGHT = 512;

  /**
   * Main method for the application - entry point
   */
  public static void main(String[] args) {
    Application.launch(args);
  }

  @Override
  public void start(Stage primaryStage) throws IOException {
    FXMLLoader loader = new FXMLLoader();
    String fxmlPath = "src/simulation/view/HomeView.fxml";
    System.out.println(System.getProperty("user.dir"));
    FileInputStream fis = new FileInputStream(fxmlPath);
    GridPane ap = loader.load(fis);
    Scene myHomeScene = new Scene(ap, WINDOW_WIDTH, WINDOW_HEIGHT);
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

  @FXML
  private void launchSim() {
    GUI gui = new GUI();
    gui.start(new Stage());
  }
}
