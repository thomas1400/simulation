package cellsociety;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

public class SimTester {
    static Simulation mySim;
    static String mySimulationFile = "data/testXML.xml";

    public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {
        mySim = new Simulation(mySimulationFile);
    }
}
