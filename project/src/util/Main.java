package util;

import java.io.File;

/**
 * The main run class.
 *
 * @author Peijun
 */
public class Main {

  /**
   * The main run class.
   *
   * @param args the path to the event file.
   */
  public static void main(String[] args) {
    if (args.length == 1 && new File(args[0]).exists()) {
      String warehouseFile = "../initial.csv";
      String translationFile = "../translation.csv";
      String traversalFile = "../traversal_table.csv";
      String outFile = "../";
      Simulator mySim = new Simulator(args[0], warehouseFile, translationFile,
          traversalFile, outFile, true);
      mySim.run();
    } else {
      throw new UnsupportedOperationException("Please enter a vaild path to "
          + "the event file.");
    }
  }
}

