package simulation.controller;

import org.xml.sax.SAXException;
import javafx.scene.paint.Color;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

public class SimTester {

  static Simulation mySim;
  static String mySimulationFile = "data/percolation.xml";

  public static void main(String[] args)
      throws ParserConfigurationException, SAXException, IOException {
    mySim = new Simulation(mySimulationFile);
    //mySim.play();
    Color[][] myGrid = mySim.getColorGrid();
    for (Color[] colors : myGrid) {
      for (Color color : colors) {
        System.out.println(color);
      }
    }
  }
}
