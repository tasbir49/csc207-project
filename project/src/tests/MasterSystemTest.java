package tests;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import util.MasterSystem;
import warehousefloor.WarehouseFloor;

/**
 * Unit test for MasterSystem class.
 */
public class MasterSystemTest {

  private static MasterSystem masterSystem;

  /**
   * Initialize the masterSystem object needed for the tests once.
   */
  @BeforeClass
  public static void setUpBeforeClass() {
    masterSystem = new TestFactory().getTestEnviroment();
  }

  /**
   * Since the setAll method is called in TestFactory, just need to confirm
   * the stock levels of the warehouse floor.
   */
  @Test
  public void setAll()
      throws NoSuchFieldException, IllegalAccessException {
    WarehouseFloor warehouseFloor = masterSystem.getWarehouseFloor();

    Field inventory = WarehouseFloor.class.getDeclaredField("inventory");
    inventory.setAccessible(true);

    Field toBeReplenished = WarehouseFloor.class
        .getDeclaredField("toBeReplenished");
    toBeReplenished.setAccessible(true);
    try {
      HashMap<String, Integer> actualInventory = (HashMap<String, Integer>)
          inventory.get(warehouseFloor);
      LinkedList<String> actualToBeReplenished = (LinkedList<String>)
          toBeReplenished.get(warehouseFloor);

      LinkedList<String> expectedToBeReplenished = new LinkedList<>(
          Collections.singletonList("19"));
      boolean amountTrue = actualInventory.get("19") == 4;
      boolean sizeTrue = actualInventory.size() == 48;
      boolean replenTrue = expectedToBeReplenished
          .equals(actualToBeReplenished);
      Assert.assertTrue(amountTrue && sizeTrue && replenTrue);
    } catch (ClassCastException cce) {
      Assert.fail();
    }
  }

  /**
   * Test for getWarehouseFloor method.
   */
  @Test
  public void getWarehouseFloor() {
    Assert.assertNotNull(masterSystem.getWarehouseFloor());
  }

  /**
   * Test for getWorkerManager method.
   */
  @Test
  public void getWorkerManager() {
    Assert.assertNotNull(masterSystem.getWorkerManager());
  }

  /**
   * Test for getPickingRequestManager method.
   */
  @Test
  public void getPickingRequestManager() {
    Assert.assertNotNull(masterSystem.getPickingRequestManager());
  }

  /**
   * Test for getFileSystem method.
   */
  @Test
  public void getFileSystem() {
    Assert.assertNotNull(masterSystem.getFileSystem());
  }

  /**
   * Test for getSkuTranslator method.
   */
  @Test
  public void getSkuTranslator() {
    Assert.assertNotNull(masterSystem.getSkuTranslator());
  }

}