package tests;

import java.io.File;
import java.util.ArrayList;
import org.junit.Assert;
import org.junit.Test;
import util.CsvReadWrite;
import util.Simulator;


/**
 * A test for the Simulator class.
 *
 * @author Peijun
 */
public class SimulatorTest {

  /**
   * A test for the simulator run method.
   */
  @Test
  public void run() {
    TestFactory.supressPrint();
    String eventFile = "../events.txt";
    String warehouseFile = "../initial.csv";
    String translationFile = "../translation.csv";
    String traversalFile = "../traversal_table.csv";
    String outFile = "tests/";

    Simulator simulator = new Simulator(eventFile, warehouseFile,
        translationFile, traversalFile, outFile, false);
    simulator.run();

    ArrayList<String> expectedFinal = CsvReadWrite
        .readCsv("tests/expected_final.csv");
    ArrayList<String> expectedOrders = CsvReadWrite
        .readCsv("tests/expected_orders.csv");
    ArrayList<String> actualFinal = CsvReadWrite.readCsv("tests/final.csv");
    ArrayList<String> actualOrders = CsvReadWrite.readCsv("tests/orders.csv");

    File finalFile = new File("tests/final.csv");
    File orderFile = new File("tests/orders.csv");
    if (!(finalFile.delete() && orderFile.delete())) {
      Assert.fail("Something went wrong with deleting the test outputs.");
    } else if (expectedFinal == null || expectedOrders == null || actualFinal
        == null || actualOrders == null) {
      Assert.fail("Something went wrong with reading the test results.");
    } else {
      Assert.assertTrue(expectedFinal.equals(actualFinal) && expectedOrders
          .equals(actualOrders));
    }
  }
}