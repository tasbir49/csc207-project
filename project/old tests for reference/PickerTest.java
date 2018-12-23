//package tests;
//
//import static org.junit.Assert.assertEquals;
//
//import java.io.ByteArrayOutputStream;
//import java.io.PrintStream;
//import java.util.Arrays;
//import java.util.LinkedList;
//
//import org.junit.Before;
//import org.junit.Test;
//
//import warehousefloor.Picker;
//import util.SkuTranslator;
//import warehousefloor.WarehouseFloor;
//
//
///**
// * Created by Tasbir on 2017-03-14.
// */
//public class PickerTest {
//  Picker theDonald;
//  ByteArrayOutputStream printStr;
//
//
//  /**
//   * Sets up everything needed for testing this class.
//   */
//  @Before
//  public void setUp() throws Exception {
//    SkuTranslator.setLocations("tests/traversal_table.csv");
//    SkuTranslator.setProperties("tests/translation.csv");
//    WarehouseFloor worksAt = new WarehouseFloor("", "tests", 30);
//    theDonald = new Picker("Donald Trump", worksAt);
//    worksAt.addOrder("Order White S");
//    worksAt.addOrder("Order White SE");
//    worksAt.addOrder("Order White SES");
//    worksAt.addOrder("Order White SEL");
//    worksAt.addPicker(theDonald);
//
//
//  }
//
//  @Test
//  public void tryReady() throws Exception {
//    resetPrint();
//    String displayString = "Picker Donald Trump is tryReady, it will go to "
//        + "locations:\nA,0,0,0\nA,0,0,1\nA,0,0,2\nA,0,0,3\nA,0,1,0\nA,0,1,1\nA,0,1,2\nA,0,1,3"
//        + "\n\r\n";
//    theDonald.tryReady();
//    assertEquals(displayString, printStr.toString());
//  }
//
//  @Test
//  public void getScanOrder() throws Exception {
//    theDonald.tryReady();
//    assertEquals(theDonald.getScanOrder(), new LinkedList<Integer>(Arrays.asList(1, 2, 3,
//        4, 5, 6, 7, 8)));
//
//  }
//
//  @Test
//  public void incorrectScan() throws Exception {
//    theDonald.tryReady();
//    resetPrint();
//    theDonald.scan(4);
//    String displayString = "A fascia of SKU 4 was taken.\r\n"
//        +  "Picker Donald Trump performed a scan action!\r\nScan of SKU 4 did not"
//        + " match with the expected result of SKU 1\r\n";
//    assertEquals(displayString, printStr.toString());
//  }
//
//  @Test
//  public void correctScan() throws Exception {
//    theDonald.tryReady();
//    resetPrint();
//    theDonald.scan(1);
//    String displayString = "A fascia of SKU 1 was taken.\r\n"
//        + "Picker Donald Trump performed a scan action!\r\nScan of SKU 1"
//        + " matched with the expected result\r\n";
//    assertEquals(displayString, printStr.toString());
//    assertEquals(theDonald.getScanCount(), 1);
//
//  }
//
//  @Test
//  public void goToMarshallIncorrect() throws Exception { //If they go when they shouldn't
//    theDonald.tryReady();
//    resetPrint();
//    theDonald.goToMarshall();
//
//    String displayStr = "Picker Donald Trump " + "tried to go to marshalling "
//        + "area with less than 8 fascias picked, the picking request has "
//        + "been sent back to be picked again.\r\n";
//    assertEquals(displayStr, printStr.toString());
//  }
//
//  @Test
//  public void goToMarshallCorrect() throws Exception {
//    theDonald.tryReady();
//    theDonald.scan(1);
//    theDonald.scan(2);
//    theDonald.scan(3);
//    theDonald.scan(4);
//    theDonald.scan(5);
//    theDonald.scan(6);
//    theDonald.scan(7);
//    theDonald.scan(8);
//    resetPrint();
//    theDonald.goToMarshall();
//    String displayStr = "Picker Donald Trump has gone to marshalling area.\r\n";
//    assertEquals(displayStr, printStr.toString());
//  }
//
//  //resets the print stream.
//  private void resetPrint() {
//    printStr = new ByteArrayOutputStream();
//    System.setOut(new PrintStream(printStr));
//  }
//}