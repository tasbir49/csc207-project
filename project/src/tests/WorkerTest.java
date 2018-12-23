package tests;

import static org.junit.Assert.assertEquals;

import fascia.Order;
import fascia.PickingRequest;
import fascia.PickingRequestManager;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedList;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import util.MasterSystem;
import worker.Sequencer;
import worker.Worker;

/**
 * Unit test for Worker class.
 */
public class WorkerTest {

  private TestFactory testFactory;
  private MasterSystem masterSystem;
  private static Field currPickingReq;
  private static Field scanCount;
  private static Field toBeScanned;
  private static Field marshallingArea;
  private Worker worker;

  /**
   * Sets up all the items needed for testing this class.
   **/
  @Before
  public void setUp() throws NoSuchFieldException {
    testFactory = new TestFactory();
    currPickingReq = Worker.class.getDeclaredField("currPickingReq");
    currPickingReq.setAccessible(true);
    scanCount = Worker.class.getDeclaredField("scanCount");
    scanCount.setAccessible(true);
    toBeScanned = Worker.class.getDeclaredField("toBeScanned");
    toBeScanned.setAccessible(true);
    marshallingArea = PickingRequestManager.class
        .getDeclaredField("marshallingArea");
    marshallingArea.setAccessible(true);
    masterSystem = testFactory.getTestEnviroment();
    worker = new Sequencer("Bob", masterSystem);
  }

  @Test
  public void scanEmpty()
      throws IllegalArgumentException, IllegalAccessException {
    worker.scan("234");
    Assert.assertEquals(0, scanCount.get(worker));
  }

  /**
   * Test for when a scan is successful.
   */
  @Test
  public void scanExpectedSku() throws IllegalAccessException {
    ArrayList<Order> orders = testFactory.randomOrders(4);
    String expectedSku = orders.get(0).getSkus()[0];
    // currPickingReq.set(worker, );
    ((LinkedList<PickingRequest>) marshallingArea
        .get(masterSystem.getPickingRequestManager()))
        .add(new PickingRequest(orders, 0,
            masterSystem.getPickingRequestManager()));
    worker.tryReady();
    worker.scan(expectedSku);
    Assert.assertEquals(1, scanCount.get(worker));
  }

  /**
   * Test for when a scan is successful.
   */
  @Test
  public void scanUnxpectedSku() throws IllegalAccessException {
    ArrayList<Order> orders = testFactory.randomOrders(4);
    ((LinkedList<PickingRequest>) marshallingArea
        .get(masterSystem.getPickingRequestManager()))
        .add(new PickingRequest(orders, 0,
            masterSystem.getPickingRequestManager()));
    worker.tryReady();
    worker.scan("Bad");
    Assert.assertEquals(0, scanCount.get(worker));
  }

  @Test
  public void rescan() throws IllegalArgumentException, IllegalAccessException {
    ArrayList<Order> orders = testFactory.randomOrders(4);
    currPickingReq.set(worker,
        new PickingRequest(orders, 0, masterSystem.getPickingRequestManager()));
    worker.rescan();
    Assert.assertEquals(0, scanCount.get(worker));
  }

  @Test
  public void getName() {
    assertEquals(worker.getName(), "Bob");
  }

}
