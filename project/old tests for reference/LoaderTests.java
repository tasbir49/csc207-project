//package tests;
//
//import static org.junit.Assert.assertEquals;
//
//import java.io.ByteArrayOutputStream;
//import java.io.PrintStream;
//import java.util.ArrayList;
//import org.junit.After;
//import org.junit.Before;
//import org.junit.Test;
//import warehousefloor.Loader;
//import warehousefloor.Order;
//import warehousefloor.PickingRequest;
//import util.SkuTranslator;
//import warehousefloor.WarehouseFloor;
//
///**
// * The unit tests for the Loader class.
// *
// * @author Chaitanya
// */
//public class LoaderTests {
//
//
//  WarehouseFloor warehousefloor;
//  Loader loader;
//  PickingRequest pick;
//  ArrayList<Order> orders;
//  int[] frontPal = {1, 3, 5, 7};
//  int[] backPal = {2, 4, 6, 8};
//
//  private ByteArrayOutputStream outContent = new ByteArrayOutputStream();
//  private ByteArrayOutputStream errContent = new ByteArrayOutputStream();
//
//
//  @Before
//  public void setUp() throws Exception {
//
//    System.setOut(new PrintStream(outContent));
//    System.setErr(new PrintStream(errContent));
//
//    SkuTranslator.setLocations("tests/translation.csv");
//    SkuTranslator.setProperties("tests/traversal_table.csv");
//    warehousefloor = new WarehouseFloor("tests/initial.csv", "../", 30);
//    orders = new ArrayList<>();
//    for (int i = 0; i < 4; i++) {
//      orders.add(new Order("Order Green SE"));
//      warehousefloor.addOrder("Order Green SE");
//    }
//    pick = new PickingRequest(orders, 1);
//    warehousefloor.sendToLoading(pick, frontPal, backPal);
//    loader = new Loader("Joe", warehousefloor);
//    warehousefloor.addLoader(loader);
//
//  }
//
//  @After
//  public void tearDown() throws Exception {
//
//  }
//
//  @Test
//  public void testReady() {
//    resetOutput();
//    loader.tryReady();
//    assertEquals("Loader Joe is tryReady to load.\r\n", outContent.toString());
//  }
//
//  @Test
//  public void testAddPallets() {
//    int[] frontPal = {1, 3, 5, 7};
//    int[] backPal = {2, 4, 6, 8};
//    loader.setPallets(frontPal, backPal);
//  }
//
//  @Test
//  public void testBadLoad() {
//    resetOutput();
//    loader.load();
//    assertEquals("The loader tried to load an incomplete picking request, "
//            + "the picking request was sent to be re picked instead.\r\n",
//        outContent.toString());
//  }
//
//  @Test
//  public void testGoodLoad() {
//    loader.tryReady();
//    resetOutput();
//    for (int i = 0; i < 8; i++) {
//      loader.addScanCount();
//    }
//    loader.load();
//    assertEquals("Loader Joe could not load picking request 1\n"
//            + "The picking request is sent back to loading area.\r\n",
//        outContent.toString());
//  }
//
//  @Test
//  public void testGoodLoad1() {
//    resetOutput();
//    pick = new PickingRequest(orders, 0);
//    warehousefloor.sendToLoading(pick, frontPal, backPal);
//    loader.tryReady();
//    for (int i = 0; i < 8; i++) {
//      loader.addScanCount();
//    }
//    loader.load();
//    assertEquals("Loader Joe is tryReady to load.\n"
//            + "Loader Joe loaded picking request 0\n",
//        outContent.toString());
//  }
//
//  public void resetOutput() {
//    outContent = new ByteArrayOutputStream();
//    errContent = new ByteArrayOutputStream();
//    System.setOut(new PrintStream(outContent));
//    System.setErr(new PrintStream(errContent));
//  }
//}
