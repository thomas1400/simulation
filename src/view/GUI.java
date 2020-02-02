package view;

import events.IUpdate;
import javafx.application.Application;
import javafx.scene.Group;
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
    private GridPane group;
    private String xmlFileName;
    //private Simulation simulation;

    // Buttons members
    private Button myHomeButton;
    private Button myResetButton;
    private Button mySlowDownButton;
    private Button myPauseButton;
    private Button mySpeedUpButton;
    private Button myStepButton;
    private Button myPlayButton;

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        loadSimulation();
        setUpWindow(primaryStage);
        simulation.setListener(this);
        mainWindow.show();
    }
    private void update(){
        setUpWindow(mainWindow);
    }
    private void setUpWindow(Stage primaryStage){
        mainWindow = primaryStage;
        mainWindow.setTitle(WINDOW_TITLE);
        Scene gridScene = new Scene(makeMasterGrid(), 512, 512);
        mainWindow.setScene(gridScene);
    }
    private GridPane makeMasterGrid(){
        GridPane mainGrid = new GridPane();
        group = new GridPane();
        gp = new GridPane();
        makeButtons();
        makeGrid();
        mainGrid.add(group, 0,0);
        mainGrid.add(gp, 0, 1);
        return mainGrid;
    }
    private void makeButtons(){
        myHomeButton = new Button("Home");
        group.add(myHomeButton, 0, 0);
        // command for going home

        myResetButton = new Button("Reset");
        group.add(myResetButton, 1, 0);
        // command for reset

        mySlowDownButton = new Button("Slow Down");
        mySlowDownButton.setOnAction(e -> simulation.slowDown());
        group.add(mySlowDownButton, 2, 0);

        myPauseButton = new Button("Pause");
        myPauseButton.setOnAction(e -> simulation.pause());
        group.add(myPauseButton, 3, 0);

        mySpeedUpButton = new Button("Speed Up");
        mySpeedUpButton.setOnAction(e -> simulation.speedUp());
        group.add(mySpeedUpButton, 4,0);

        myStepButton = new Button("Step");
        myStepButton.setOnAction(e -> simulation.step());
        group.add(myStepButton, 5, 0);

        myPlayButton = new Button("Play");
        myPlayButton.setOnAction(e -> simulation.play());
        group.add(myPlayButton, 6,0);
    }
    private void makeGrid(){
        Color[][] colorGrid = simulation.getColorGrid();
        int width = colorGrid.length;
        int height = colorGrid[0].length;
        for(int i = 0; i < height; i++){
            for(int j = 0; j < width; j++){
                Rectangle rec = new Rectangle();
                rec.setFill(colorGrid[i][j]);
                rec.setWidth(25);
                rec.setHeight(25);
                gp.add(rec, i, j+1);
            }
        }
    }

    private void loadSimulation() throws ParserConfigurationException, SAXException, IOException {
        xmlFileName = "data/generatedXML.xml";
        //this line above should be a prompt for the user
        simulation = new Simulation(xmlFileName);
        simulationTitle = simulation.getTitle();
        //you can use simulation.getColorGrid() to get a Color[][] for each cell's state
    }

    /**
     * From IUpdate: method called when the simulation alerts the GUI when the
     * simulation steps
     */
    @Override
    public void simulationUpdate() {
        System.out.println("Updated");
        update();
    }
}
