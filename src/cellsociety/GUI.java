package cellsociety;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.layout.TilePane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

public class GUI extends Application {
    /**
     * TODO: Change final text variables to be read from file
     */
    final private String WINDOW_TITLE = "SIMULATION";

    // Instance variables for the GUI
    private Stage mainWindow;
    private Group root;
    private Scene scene;
    private Canvas canvas;
    private Simulation simulation;
    //private Simulation simulation;
    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        setUpWindow(primaryStage);
        loadSimulation();
        GridAnimation ga = new GridAnimation();
    }
    private void makeButton(){
        Button btn = new Button();
        btn.setText("Test");
        root.getChildren().add(btn);
    }
    private void setUpWindow(Stage primaryStage){
        mainWindow = primaryStage;
        mainWindow.setTitle(WINDOW_TITLE);
        root = new Group();
        scene = new Scene(root);
        mainWindow.setScene(scene);
        canvas = new Canvas(512, 512);
        root.getChildren().add(canvas);
    }
    private void loadSimulation() throws ParserConfigurationException, SAXException, IOException {
        simulation = new Simulation("data/generatedXML.xml");
    }
    private class GridAnimation extends AnimationTimer{

        @Override
        public void handle(long now) {
            TilePane tp = new TilePane();
            Rectangle rec = new Rectangle();
            Rectangle rec2 = new Rectangle();
            Rectangle rec3 = new Rectangle();
            Rectangle rec4 = new Rectangle();
            tp.getChildren().add(rec);
            tp.getChildren().add(rec2);
            tp.getChildren().add(rec3);
            tp.getChildren().add(rec4);

            //get grid
            //draw grid
        }
    }
    private Color[][] parseGridColor(char[][] grid){
        int gWidth = grid[0].length;
        int gLength = grid.length;
        Color[][] colorGrid = new Color[gLength][gWidth];
        for(int i = 0; i < gWidth; i++){
            for(int j = 0; j < gWidth; j++){
                switch (grid[i][j]){
                    case 'b':
                        colorGrid[i][j] = Color.BLUE;
                    case 'r':
                        colorGrid[i][j] = Color.RED;
                    case 'g':
                        colorGrid[i][j] = Color.GREEN;
                }
            }
        }
        return colorGrid;
    }
}
