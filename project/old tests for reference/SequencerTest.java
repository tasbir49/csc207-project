//package tests;
//
//import static org.junit.Assert.fail;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.LinkedList;
//import org.junit.After;
//import org.junit.Assert;
//import org.junit.Before;
//import org.junit.Test;
//import warehousefloor.Loader;
//import warehousefloor.Order;
//import warehousefloor.PickingRequest;
//import warehousefloor.Sequencer;
//import util.SkuTranslator;
//import warehousefloor.WarehouseFloor;
//
///**
// * The unit tests for the Sequencer class.
// *
// * @author Andrew
// */
//public class SequencerTest {
//
//  WarehouseFloor warehousefloor;
//  Loader loader;
//  PickingRequest pickreq;
//  PickingRequest pickreq2;
//  PickingRequest pickreq3;
//  ArrayList<Order> orders;
//  ArrayList<Order> orders2;
//  ArrayList<Order> orders3;
//  Sequencer sequencer;
//  LinkedList<Integer> scanOrder;
//
//  int[] frontPal = {1, 3, 5, 7};
//  int[] backPal = {2, 4, 6, 8};
//
//  /**
//   * Instantiates all the above variables to allow tests for Sequencer.
//   *
//   * @throws IOException File not found, probably
//   */
//  @Before
//  public void setUp() throws Exception {
//
//    SkuTranslator.setLocations("tests/translation.csv");
//    SkuTranslator.setProperties("tests/traversal_table.csv");
//    warehousefloor = new WarehouseFloor("tests/initial.csv", "../", 30);
//    orders = new ArrayList<>();
//    orders2 = new ArrayList<>();
//    orders3 = new ArrayList<>();
//    scanOrder = new LinkedList<>();
//
//    scanOrder.add(1);
//    scanOrder.add(2);
//    scanOrder.add(3);
//    scanOrder.add(4);
//
//    orders.add(new Order("Order Green S"));
//    orders.add(new Order("Order Green SE"));
//    orders.add(new Order("Order Green SES"));
//    orders.add(new Order("Order Green SEL"));
//
//    orders2.add(new Order("Order White S"));
//    orders2.add(new Order("Order White SE"));
//    orders2.add(new Order("Order White SES"));
//    orders2.add(new Order("Order White SEL"));
//
//    orders3.add(new Order("Order Red S"));
//    orders3.add(new Order("Order Red SE"));
//    orders3.add(new Order("Order Red SES"));
//    orders3.add(new Order("Order Red SEL"));
//
//    warehousefloor.addOrder("Order Green S");
//    warehousefloor.addOrder("Order Green SE");
//    warehousefloor.addOrder("Order Green SES");
//    warehousefloor.addOrder("Order Green SEL");
//
//    warehousefloor.addOrder("Order White S");
//    warehousefloor.addOrder("Order White SE");
//    warehousefloor.addOrder("Order White SES");
//    warehousefloor.addOrder("Order White SEL");
//
//    warehousefloor.addOrder("Order Red S");
//    warehousefloor.addOrder("Order Red SE");
//    warehousefloor.addOrder("Order Red SES");
//    warehousefloor.addOrder("Order Red SEL");
//
//    pickreq = new PickingRequest(orders, 0);
//    pickreq2 = new PickingRequest(orders2, 1);
//
//
//    loader = new Loader("Phil", warehousefloor);
//    sequencer = new Sequencer("Billy", warehousefloor);
//
//    warehousefloor.addSequencer(sequencer);
//    warehousefloor.addLoader(loader);
//
//    warehousefloor.sendToMarshalling(pickreq);
//    warehousefloor.sendToMarshalling(pickreq2);
//
//    sequencer.tryReady();
//    sequencer.setToBeScanned(scanOrder);
//  }
//
//  /**
//   * Destroys these variables for a fresh unit test for Sequencer.
//   *
//   * @throws IOException File not found, probably
//   */
//  @After
//  public void tearDown() throws Exception {
//    warehousefloor = null;
//    orders = null;
//    pickreq = null;
//    sequencer = null;
//    loader = null;
//  }
//
//  @Test
//  public void testReady() {
//    boolean actual = false;
//    if (sequencer.getScanCount() == 0
//        && sequencer.getCurrPickingReq().equals(pickreq)
//        && sequencer.getToBeScanned().equals(scanOrder)) {
//      actual = true;
//    }
//
//    Assert.assertEquals(true, actual);
//  }
//
//  @Test
//  public void testSequencer() {
//    boolean actual = false;
//    if (sequencer.getName().equals("Billy")
//        && sequencer.getWorksAt().equals(warehousefloor)) {
//      actual = true;
//    }
//
//    Assert.assertEquals(true, actual);
//  }
//
//  @Test
//  public void testSequence() {
//
//    PickingRequest newPick = new PickingRequest(orders2, 1);
//
//    warehousefloor.sendToMarshalling(newPick);
//
//    sequencer.addScanCount();
//    sequencer.addScanCount();
//    sequencer.addScanCount();
//    sequencer.addScanCount();
//    sequencer.addScanCount();
//    sequencer.addScanCount();
//    sequencer.addScanCount();
//    sequencer.addScanCount();
//
//    sequencer.setCurrPickingReq(newPick);
//    sequencer.sequence();
//
//    loader.tryReady();
//    PickingRequest expected = loader.getCurrPickingReq();
//
//    Assert.assertEquals(newPick, expected);
//  }
//
//  @Test
//  public void testWorker() {
//    boolean actual = false;
//    if (sequencer.getName().equals("Billy")
//        && sequencer.getWorksAt().equals(warehousefloor)) {
//      actual = true;
//    }
//
//    Assert.assertEquals(true, actual);
//  }
//
//  @Test
//  public void testGetScanOrder() {
//    LinkedList<Integer> expected = new
//        LinkedList<>(sequencer.getCurrPickingReq().getProperSkus());
//
//    Assert.assertEquals(expected, sequencer.getScanOrder());
//  }
//
//  @Test
//  public void testSetCurrPickingReq() {
//    sequencer.setCurrPickingReq(pickreq);
//
//    PickingRequest expected = pickreq;
//
//    Assert.assertEquals(expected, sequencer.getCurrPickingReq());
//  }
//
//  @Test
//  public void testScanResult() {
//
//    boolean actual = false;
//    if (sequencer.scanResult(1, sequencer.getToBeScanned().pop())) {
//      actual = true;
//    }
//
//    Assert.assertEquals(true, actual);
//  }
//
//  @Test
//  public void testScan() {
//
//    int actual = sequencer.getScanCount() + 1;
//    sequencer.scan(1);
//    sequencer.scan(9999999);
//
//    int expected = sequencer.getScanCount();
//
//    Assert.assertEquals(expected, actual);
//  }
//
//  @Test
//  public void testGetWorksAt() {
//    WarehouseFloor actual = sequencer.getWorksAt();
//    WarehouseFloor expected = warehousefloor;
//    Assert.assertEquals(expected, actual);
//  }
//
//  @Test
//  public void testGetCurrPickingReq() {
//    sequencer.setCurrPickingReq(pickreq2);
//    PickingRequest expected = pickreq2;
//    PickingRequest actual = sequencer.getCurrPickingReq();
//    Assert.assertEquals(expected, actual);
//  }
//
//  @Test
//  public void testGetToBeScanned() {
//    LinkedList<Integer> expected = new LinkedList<>();
//    sequencer.setToBeScanned(expected);
//    LinkedList<Integer> actual = sequencer.getToBeScanned();
//    Assert.assertEquals(expected, actual);
//  }
//
//  @Test
//  public void testSetToBeScanned() {
//    LinkedList<Integer> expected = new LinkedList<>();
//    sequencer.setToBeScanned(expected);
//    LinkedList<Integer> actual = sequencer.getToBeScanned();
//    Assert.assertEquals(expected, actual);
//  }
//
//  @Test
//  public void testGetName() {
//    String name = "Billy";
//    Assert.assertEquals(name, sequencer.getName());
//  }
//
//  @Test
//  public void testAddScanCount() {
//    sequencer.addScanCount();
//    int scanAmount = sequencer.getScanCount();
//
//    Assert.assertEquals(1, scanAmount);
//  }
//
//  @Test
//  public void testGetScanCount() {
//    int scanAmount = sequencer.getScanCount();
//
//    Assert.assertEquals(0, scanAmount);
//  }
//
//  @Test
//  public void testResetScanCount() {
//    sequencer.resetScanCount();
//    int scanAmount = sequencer.getScanCount();
//
//    Assert.assertEquals(0, scanAmount);
//  }
//
//}
