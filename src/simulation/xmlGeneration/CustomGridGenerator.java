package simulation.xmlGeneration;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class CustomGridGenerator {
  private static final int WIDTH = 80;
  private static final int HEIGHT = 80;

  private static void generateGridFile(String filepath) {
    try {
      File newGrid = new File(filepath);
      BufferedWriter writer = new BufferedWriter(new FileWriter(newGrid));

      for (int y = 0; y < HEIGHT; y++) {
        for (int x = 0; x < WIDTH; x++) {
          if (x < WIDTH/3) {
            writer.append("0 ");
          } else if (x < 2*WIDTH/3) {
            writer.append("1 ");
          } else {
            writer.append("2 ");
          }
        }
        writer.append("\n");
      }
      writer.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static void main(String[] args) {
    generateGridFile("data/RPSgrid.txt");
  }
}
