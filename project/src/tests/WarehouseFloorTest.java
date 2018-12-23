package tests;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import util.CsvReadWrite;
import util.MasterSystem;
import warehousefloor.Truck;
import warehousefloor.WarehouseFloor;

/**
 * Unit test for WarehouseFloor class.
 */
public class WarehouseFloorTest {

  private static TestFactory factory;
  private static Field inventory;
  private static Field trucks;
  private static Field toBeReplenished;
  private MasterSystem system;
  private WarehouseFloor warehouseFloor;
  private HashMap<String, Integer> inventoryMap;
  private ArrayList<Truck> truckArrayList;
  private LinkedList<String> replenList;

  /**
   * Set up static vairables once before all tests.
   */
  @BeforeClass
  public static void setUpBeforeClass() throws NoSuchFieldException {
    factory = new TestFactory();
    inventory = WarehouseFloor.class.getDeclaredField("inventory");
    inventory.setAccessible(true);
    trucks = WarehouseFloor.class.getDeclaredField("trucks");
    trucks.setAccessible(true);
    toBeReplenished = WarehouseFloor.class.getDeclaredField("toBeReplenished");
    toBeReplenished.setAccessible(true);
    TestFactory.supressPrint();
  }

  /**
   * Get a new MasterSystem instance for each test case.
   */
  @Before
  public void setUp() throws IllegalAccessException {
    system = factory.getTestEnviroment();
    warehouseFloor = system.getWarehouseFloor();
    inventoryMap = (HashMap<String, Integer>) inventory.get(warehouseFloor);
    truckArrayList = (ArrayList<Truck>) trucks.get(warehouseFloor);
    replenList = (LinkedList<String>)
        toBeReplenished.get(warehouseFloor);
  }

  /**
   * A helper method to generate the default inventory of warehouseFloor.
   */
  private HashMap<String, Integer> generateDefaultInventory() {
    HashMap<String, Integer> expected = new HashMap<>();
    for (int i = 1; i < 49; i++) {
      expected.put(String.valueOf(i), i == 19 ? 4 : 30);
    }
    return expected;
  }

  /**
   * A helper method to assert the default inventory.
   */
  private void assertDefault() {
    Assert.assertEquals(generateDefaultInventory(), inventoryMap);
  }

  /**
   * Test the setInventory method. Since the method is already called in the
   * factory.getTestEnviroment() method, just assert the result.
   */
  @Test
  public void setInventory() {
    assertDefault();
  }

  /**
   * Test for the addSku method when the add amout + the current stock
   * level is over the limit of max stock level.
   */
  @Test
  public void addFacsiaUnderLimit() {
    system.getWarehouseFloor().addSku("19", 3);
    int actual = inventoryMap.get("19");
    Assert.assertEquals(7, actual);
  }

  /**
   * Test for the addSku method when the add amout + the current stock
   * level is under the limit of max stock level.
   */
  @Test
  public void addFacsiaOverLimit() {
    system.getWarehouseFloor().addSku("19", 3030303);
    int actual = inventoryMap.get("19");
    Assert.assertEquals(30, actual);
  }

  /**
   * Test for the addSku method when the sku is not found.
   */
  @Test
  public void addFacsiaSkuNotFound() {
    system.getWarehouseFloor().addSku("asdasd", 3030303);
    assertDefault();
  }

  /**
   * Test for the removeSku method when the sku is not found.
   */
  @Test
  public void removeFasciaNotFound() {
    Assert.assertFalse(warehouseFloor.removeSku("ASDSAD"));
    assertDefault();
  }

  /**
   * Test for the removeSku method when the stock level of that sku is empty.
   */
  @Test
  public void removeFasciaEmpty() {
    inventoryMap.put("19", 0);
    Assert.assertFalse(warehouseFloor.removeSku("19"));
  }

  /**
   * Test for the removeSku method when it's successful.
   */
  @Test
  public void removeFasciaSuccess() {
    warehouseFloor.removeSku("3");
    int actual = inventoryMap.get("3");
    HashMap<String, Integer> expectedMap = generateDefaultInventory();
    expectedMap.put("3", 29);
    Assert.assertEquals(29, actual);
    Assert.assertEquals(expectedMap, inventoryMap);
  }

  /**
   * Test for the removeSku method when it's successful and triggers a
   * replenish request.
   */
  @Test
  public void removeFasciaSuccessTriggerReplenishRequest()
      throws NoSuchFieldException, IllegalAccessException {
    inventoryMap.put("5", 5);
    warehouseFloor.removeSku("5");
    int actual = inventoryMap.get("5");
    HashMap<String, Integer> expectedMap = generateDefaultInventory();
    expectedMap.put("5", 4);
    Assert.assertEquals(4, actual);
    Assert.assertEquals(expectedMap, inventoryMap);
    Assert.assertTrue(replenList.contains("5"));
  }

  /**
   * Test for the addTruck method.
   */
  @Test
  public void addTruck() {
    Truck truck = new Truck(0);
    warehouseFloor.addTruck(truck);
    Assert.assertEquals(truck, truckArrayList.get(0));
    Assert.assertEquals(1, truckArrayList.size());
  }

  /**
   * Test for the getFirstNonFullTruck method.
   */
  @Test
  public void getFirstNonFullTruck() {
    Truck truck = new Truck(0);
    truckArrayList.add(truck);
    Assert.assertEquals(truck, warehouseFloor.getFirstNonFullTruck());
    Assert.assertEquals(1, truckArrayList.size());
  }

  /**
   * Test for the writeInventoryQuantities method.
   */
  @Test
  public void outPutResult() throws FileNotFoundException {
    warehouseFloor.writeLoadedOrders("TEST");
    warehouseFloor.createOutputFiles();
    final File finalcsv = new File("tests/final.csv");
    final File orderscsv = new File("tests/orders.csv");
    ArrayList<String> actualFinal = CsvReadWrite.readCsv("tests/final.csv");
    final ArrayList<String> actualOrders = CsvReadWrite.readCsv("tests/orders.csv");
    ArrayList<String> expectedFinal = new ArrayList<>();
    expectedFinal.add("A,1,1,2,4");
    ArrayList<String> expectedOrders = new ArrayList<>();
    expectedOrders.add("TEST");
    Assert.assertEquals(expectedFinal, actualFinal);
    Assert.assertEquals(expectedOrders, actualOrders);
    if (!finalcsv.delete() || !orderscsv.delete()) {
      throw new FileNotFoundException();
    }
  }

  /**
   * Test for writeLoadedOrders method.
   */
  @Test
  public void logLoading() {
    warehouseFloor.writeLoadedOrders("Ayy Lmao");
    ArrayList<String> actual = system.getFileSystem()
        .getWritingFileForEdit("tests/orders.csv");
    ArrayList<String> expected = new ArrayList<>();
    expected.add("Ayy Lmao");
    Assert.assertEquals(expected, actual);
  }

  /**
   * Test for popReplenished method when it is successful.
   */
  @Test
  public void popReplenishedSuccess() {
    Assert.assertEquals("19", warehouseFloor.getFirstReplenishRequest());
    Assert.assertTrue(replenList.isEmpty());
  }

  /**
   * Test for when popReplenished method when it fails.
   */
  @Test
  public void popReplenishedFail() {
    replenList.remove(0);
    Assert.assertNull(warehouseFloor.getFirstReplenishRequest());
    Assert.assertTrue(replenList.isEmpty());
  }
}