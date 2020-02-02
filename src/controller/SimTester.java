package controller;

import org.xml.sax.SAXException;
import javafx.scene.paint.Color;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

public class SimTester {

  static Simulation mySim;
  static String mySimulationFile = "data/generatedXML.xml";

  public static void main(String[] args)
      throws ParserConfigurationException, SAXException, IOException {
    mySim = new Simulation(mySimulationFile);
    //mySim.play();
    Color[][] myGrid = mySim.getColorGrid();
    for (int i = 0; i < myGrid.length; i++) {
      for (int j = 0; j < myGrid[i].length; j++) {
        System.out.println(myGrid[i][j]);
      }
    }
  }
}
