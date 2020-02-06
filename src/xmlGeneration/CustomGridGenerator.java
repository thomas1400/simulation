package xmlGeneration;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class CustomGridGenerator {

  private static void generateGridFile(String filepath) {
    int width = 80;
    int height = 80;

    try {
      File newGrid = new File(filepath);
      BufferedWriter writer = new BufferedWriter(new FileWriter(newGrid));

      for (int y = 0; y < height; y++) {
        for (int x = 0; x < width; x++) {
          if (x < width/3) {
            writer.append("0 ");
          } else if (x < 2*width/3) {
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
