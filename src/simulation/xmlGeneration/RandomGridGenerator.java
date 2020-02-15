package simulation.xmlGeneration;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class RandomGridGenerator {

  private static void generateGridFile(String filepath) {
    double g1 = 0.22;
    double g2 = 0.22;
    double g3 = 0.22;
    double g4 = 0.22;
    double empty = 0.08;

    int width = 100;
    int height = 100;

    try {
      File newGrid = new File(filepath);
      BufferedWriter writer = new BufferedWriter(new FileWriter(newGrid));

      for (int y = 0; y < height; y++) {
        for (int x = 0; x < width; x++) {
          double z = Math.random();
          if (z < g1) {
            writer.append("1 ");
          } else if (z < g1 + g2) {
            writer.append("2 ");
          } else if (z < g1 + g2 + g3) {
            writer.append("3 ");
          } else if (z < g1 + g2 + g3 + g4) {
            writer.append("4 ");
          } else {
            writer.append("0 ");
          }
        }
        writer.append("\n");
      }
      writer.close();
    } catch (IOException e) {
      System.exit(-1);
    }
  }

  public static void main(String[] args) {
    generateGridFile("data/RPSgrid.txt");
  }
}
