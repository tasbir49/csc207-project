package tests;

import fascia.Order;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import util.MasterSystem;
import worker.Picker;
import worker.Worker;

/**
 * Unit test for Picker class.
 */
public class PickerTest {

  private static MasterSystem masterSystem;
  private static TestFactory testFactory;
  private Picker steamyMeme;
  private static Field scanCount;
  private static Field currPickingReq;
  private static Field toBeScanned;

  /**
   * Set up some used variable once before the tests start.
   *
   * @throws NoSuchFieldException when reflection goes wrong
   */
  @BeforeClass
  public static void setUpBeforeClass() throws NoSuchFieldException {
    testFactory = new TestFactory();
    masterSystem = testFactory.getTestEnviroment();
    scanCount = Worker.class.getDeclaredField("scanCount");
    scanCount.setAccessible(true);
    currPickingReq = Worker.class.getDeclaredField("currPickingReq");
    currPickingReq.setAccessible(true);
    toBeScanned = Worker.class.getDeclaredField("toBeScanned");
    toBeScanned.setAccessible(true);
    TestFactory.supressPrint();
  }

  /**
   * Create a new picker before each test.
   */
  @Before
  public void setUp() {
    steamyMeme = new Picker("steamyMeme", masterSystem);
  }

  /**
   * Test when a scan isnt needed.
   *
   * @throws IllegalAccessException when reflection goes wrong
   */
  @Test
  public void scanNotNeeded() throws IllegalAccessException {
    steamyMeme.scan("3");
    Assert.assertEquals(0, scanCount.get(steamyMeme));
    Assert.assertNull(currPickingReq.get(steamyMeme));
  }

  /**
   * Test for when a scan is successful.
   */
  @Test
  public void scanSuccess() throws IllegalAccessException {
    ArrayList<Order> orders = testFactory.randomOrders(4);
    String expectedSku = orders.get(0).getSkus()[0];
    for (Order order : orders) {
      masterSystem.getPickingRequestManager().addOrder(order);
    }
    steamyMeme.tryReady();
    steamyMeme.scan(expectedSku);
    Assert.assertEquals(1, scanCount.get(steamyMeme));
    Assert.assertEquals(7,
        ((LinkedList<String>) toBeScanned.get(steamyMeme)).size());
  }

  /**
   * Test when the scan didnt match the sku needed.
   */
  @Test
  public void scanMismatch() throws IllegalAccessException {
    ArrayList<Order> orders = testFactory.randomOrders(4);
    ArrayList<String> excludedSkus = new ArrayList<>();
    for (Order order : orders) {
      masterSystem.getPickingRequestManager().addOrder(order);
      excludedSkus.add(order.getSkus()[0]);
      excludedSkus.add(order.getSkus()[1]);
    }
    ArrayList<String> mismatchSkus = masterSystem.getSkuTranslator()
        .getAllSku();
    mismatchSkus.removeAll(excludedSkus);
    Random random = new Random();
    String scanSku = mismatchSkus.get(random.nextInt(mismatchSkus.size()));
    steamyMeme.tryReady();
    steamyMeme.scan("asdsad");
    steamyMeme.scan(scanSku);
    Assert.assertEquals(0, scanCount.get(steamyMeme));
    Assert.assertEquals(8,
        ((LinkedList<String>) toBeScanned.get(steamyMeme)).size());
  }
}