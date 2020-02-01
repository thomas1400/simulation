package view;

import events.IUpdate;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import controller.Simulation;

public class GUI extends Application implements IUpdate {
    /**
     * TODO: Change final text variables to be read from file
     */
    final private String WINDOW_TITLE = "SIMULATION";

    // Instance variables for the GUI
    private Stage mainWindow;
    private GridPane gp;
    private Simulation simulation;
    private String simulationTitle;
    //private Simulation simulation;

    // Buttons members
    private Button myHomeButton;
    private Button myResetButton;
    private Button mySlowDownButton;
    private Button myPauseButton;
    private Button mySpeedUpButton;
    private Button myStepButton;

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        setUpWindow(primaryStage);
        loadSimulation();
        simulation.setListener(this);
        mainWindow.show();
    }
    private void update(){
        makeMasterGrid();
    }
    private void setUpWindow(Stage primaryStage){
        mainWindow = primaryStage;
        mainWindow.setTitle(WINDOW_TITLE);

        Scene gridScene = new Scene(makeMasterGrid(), 512, 512);
        mainWindow.setScene(gridScene);

    }
    private GridPane makeMasterGrid(){
        gp = new GridPane();
        makeButtons();
        makeGrid();
        return gp;
    }
    private GridPane makeButtons(){
        myHomeButton = new Button("Home");
        gp.add(myHomeButton, 0,0);
        // command for going home

        myResetButton = new Button("Reset");
        gp.add(myResetButton, 1, 0);
        // command for reset

        mySlowDownButton = new Button("Slow Down");
        mySlowDownButton.setOnAction(e -> simulation.slowDown());
        gp.add(mySlowDownButton, 2, 0);

        myPauseButton = new Button("Pause");
        myPauseButton.setOnAction(e -> simulation.pause());
        gp.add(myPauseButton, 3, 0);

        mySpeedUpButton = new Button("Speed Up");
        mySpeedUpButton.setOnAction(e -> simulation.speedUp());
        gp.add(mySpeedUpButton, 4, 0);

        myStepButton = new Button("Step");
        myStepButton.setOnAction(e -> simulation.step());
        gp.add(myStepButton, 5, 0);

        return gp;
    }
    private GridPane makeGrid(){
        Color[][] colorGrid = simulation.getColorGrid();
        //Color[][] colorGrid = {{Color.BLACK, Color.GREEN, Color.RED},{Color.RED, Color.GREEN, Color.RED}, {Color.RED, Color.RED, Color.RED}};
        int width = colorGrid.length;
        int height = colorGrid[0].length;
        for(int i = 0; i < width; i++){
            for(int j = 0; j < height; j++){
                Rectangle rec = new Rectangle();
                rec.setFill(colorGrid[i][j]);
                rec.setWidth(50);
                rec.setHeight(50);
                gp.add(rec, i, j+1);
            }
        }
        return gp;
    }

    private void loadSimulation() throws ParserConfigurationException, SAXException, IOException {
        simulation = new Simulation("data/generatedXML.xml");
        simulationTitle = simulation.getTitle();
        //you can use simulation.getColorGrid() to get a Color[][] for each cell's state
    }

    /**
     * From IUpdate: method called when the simulation alerts the GUI when the
     * simulation steps
     */
    @Override
    public void simulationUpdate() {
        update();
    }
}
