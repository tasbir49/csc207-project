package tests;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.LinkedList;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import util.MasterSystem;
import warehousefloor.WarehouseFloor;
import worker.Replenisher;

/**
 * Unit test for Replenisher class.
 */
public class ReplenisherTest {

  private static TestFactory factory;
  private static MasterSystem system;
  private static Field needed;
  private static Field sku;
  private static Field toBeReplenished;
  private static Field inventory;
  private Replenisher replenisher;
  private boolean neededBool;
  private String skuStr;
  private LinkedList<String> replenList;
  private HashMap<String, Integer> inventoryMap;

  /**
   * Set up static variables once before all the tests.
   */
  @BeforeClass
  public static void setUpBeforeClass() throws NoSuchFieldException {
    factory = new TestFactory();
    needed = Replenisher.class.getDeclaredField("needed");
    needed.setAccessible(true);
    sku = Replenisher.class.getDeclaredField("sku");
    sku.setAccessible(true);
    toBeReplenished = WarehouseFloor.class.getDeclaredField("toBeReplenished");
    toBeReplenished.setAccessible(true);
    inventory = WarehouseFloor.class.getDeclaredField("inventory");
    inventory.setAccessible(true);
    TestFactory.supressPrint();
  }

  /**
   * Get a new test subject Replenisher for each test.
   */
  @Before
  public void setUp() throws IllegalAccessException {
    system = factory.getTestEnviroment();
    replenisher = new Replenisher("KingGuppie", system);
  }

  /**
   * Test for getName method.
   */
  @Test
  public void getName() {
    Assert.assertEquals("KingGuppie", replenisher.getName());
  }

  /**
   * Test for when a scan is successful.
   */
  @Test
  public void scanSuccess() throws IllegalAccessException {
    String expected = "memes";
    sku.set(replenisher, expected);
    replenisher.scan(expected);
    updateVals();
    Assert.assertTrue(neededBool);
  }

  /**
   * Test for when a scan failed.
   */
  @Test
  public void scanFail() throws IllegalAccessException {
    String expected = "dank";
    sku.set(replenisher, null);//Check for null for safty
    replenisher.scan("memes");
    sku.set(replenisher, expected);
    replenisher.scan("memes");
    updateVals();
    Assert.assertFalse(neededBool);
  }

  /**
   * Test for ready is successful.
   */
  @Test
  public void readySuccess() {
    replenisher.ready();
    updateVals();
    boolean stringMatch = skuStr.equals("19");
    boolean sizeCheck = replenList.size() == 0;
    Assert.assertTrue(stringMatch && sizeCheck);
  }

  /**
   * Test for ready is not successful.
   */
  @Test
  public void readyFail() {
    system.getWarehouseFloor().getFirstReplenishRequest();
    replenisher.ready();
    updateVals();
    Assert.assertNull(skuStr);
    Assert.assertFalse(neededBool);
  }

  /**
   * Test for when a finish is needed.
   */
  @Test
  public void finishNeeded() throws IllegalAccessException {
    replenisher.ready();
    replenisher.scan("19");
    replenisher.finish();
    updateVals();
    int actual = inventoryMap.get("19");
    Assert.assertEquals(29, actual);
    Assert.assertNull(skuStr);
    Assert.assertFalse(neededBool);
  }

  /**
   * Test for when a finish is not needed.
   */
  @Test
  public void finishNotNeeded() {
    replenisher.ready();
    replenisher.scan("WHOOPS");
    replenisher.finish();
    updateVals();
    int actual = inventoryMap.get("19");
    Assert.assertEquals(4, actual);
    Assert.assertNull(skuStr);
    Assert.assertFalse(neededBool);
    Assert.assertEquals(1, replenList.size());
  }

  /**
   * Update the vaules of the non static variables.
   */
  private void updateVals() {
    try {
      neededBool = (Boolean) needed.get(replenisher);
      skuStr = (String) sku.get(replenisher);
      replenList = (LinkedList<String>)
          toBeReplenished.get(system.getWarehouseFloor());
      inventoryMap = (HashMap<String, Integer>)
          inventory.get(system.getWarehouseFloor());
    } catch (IllegalAccessException ex) {
      System.out.println("Access is denied to one of the fields.");
    }
  }
}