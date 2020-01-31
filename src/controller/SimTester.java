package controller;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

public class SimTester {

  static Simulation mySim;
  static String mySimulationFile = "data/generatedXML.xml";

  public static void main(String[] args)
      throws ParserConfigurationException, SAXException, IOException {
    mySim = new Simulation(mySimulationFile);
    mySim.play();
  }
}
