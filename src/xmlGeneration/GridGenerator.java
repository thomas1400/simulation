package xmlGeneration;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class GridGenerator {

  private static void generateGridFile(String filepath) {
    double g1 = 0.45;
    double g2 = 0.40;
    double empty = 0.05;

    int width = 80;
    int height = 80;

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
          } else {
            writer.append("0 ");
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
    generateGridFile("data/segregationgrid.txt");
  }
}
