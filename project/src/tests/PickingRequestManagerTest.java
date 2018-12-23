package tests;

import fascia.Order;
import fascia.PickingRequest;
import fascia.PickingRequestManager;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.logging.Logger;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import warehousefloor.Location;

/**
 * Unit test for PickingRequestManager class.
 */
public class PickingRequestManagerTest {

  private PickingRequestManager manager;
  private PickingRequest request;
  private static TestFactory factory;
  private static Field outStandingPickingRequests;
  private static Field marshallingArea;
  private static Field loadingArea;
  private static Field orders;
  private static Field pallets;
  private LinkedList<PickingRequest> pickingRequests;
  private LinkedList<PickingRequest> marshallRequests;
  private LinkedList<PickingRequest> loadRequests;
  private LinkedList<Order> ordersList;
  private HashMap<Integer, String[][]> palletsMap;

  /**
   * Set up the static variables once before the tests start.
   */
  @BeforeClass
  public static void setUpBeforeClass() throws NoSuchFieldException {
    factory = new TestFactory();
    outStandingPickingRequests = PickingRequestManager.class
        .getDeclaredField("outStandingPickingRequests");
    marshallingArea = PickingRequestManager.class
        .getDeclaredField("marshallingArea");
    loadingArea = PickingRequestManager.class.getDeclaredField("loadingArea");
    outStandingPickingRequests.setAccessible(true);
    marshallingArea.setAccessible(true);
    loadingArea.setAccessible(true);
    orders = PickingRequestManager.class.getDeclaredField("orders");
    orders.setAccessible(true);
    pallets = PickingRequestManager.class.getDeclaredField("pallets");
    pallets.setAccessible(true);
    TestFactory.supressPrint();
  }

  /**
   * Create a new manager and a new request instance before each test case.
   * Then get values from the three fields using the manager instance.
   */
  @Before
  public void setUp() throws IllegalAccessException {
    manager = new PickingRequestManager();
    request = new PickingRequest(factory.randomOrders(4), 0, manager);
    pickingRequests = (LinkedList<PickingRequest>)
        outStandingPickingRequests.get(manager);
    marshallRequests = (LinkedList<PickingRequest>)
        marshallingArea.get(manager);
    loadRequests = (LinkedList<PickingRequest>) loadingArea.get(manager);
    ordersList = (LinkedList<Order>) orders.get(manager);
    palletsMap = (HashMap<Integer, String[][]>) pallets.get(manager);
  }

  /**
   * Test update method when the argument is Location.pick
   */
  @Test
  public void updatePick() throws IllegalAccessException {
    request.updateLocation(Location.pick);
    Assert.assertEquals(1, pickingRequests.size());
    Assert.assertEquals(request, pickingRequests.get(0));
  }

  /**
   * Test update method when the argument is Location.marshall
   */
  @Test
  public void updateMarshall() throws IllegalAccessException {
    request.updateLocation(Location.marshall);
    Assert.assertEquals(1, marshallRequests.size());
    Assert.assertEquals(request, marshallRequests.get(0));
  }

  /**
   * Test update method when the argument is Location.load
   */
  @Test
  public void updateLoad() throws IllegalAccessException {
    request.updateLocation(Location.load);
    Assert.assertEquals(1, loadRequests.size());
    Assert.assertEquals(request, loadRequests.get(0));
  }

  /**
   * Test popRequest method when the argument is Location.pick
   */
  @Test
  public void popRequestPick() {
    request.updateLocation(Location.pick);
    PickingRequest firstRes = manager.popRequest(Location.pick);
    PickingRequest secondRes = manager.popRequest(Location.pick);
    Assert.assertEquals(request, firstRes);
    Assert.assertNull(secondRes);
    Assert.assertEquals(0, pickingRequests.size());
  }

  /**
   * Test popRequest method when the argument is Location.marshall
   */
  @Test
  public void popRequestmarshall() {
    request.updateLocation(Location.marshall);
    PickingRequest firstRes = manager.popRequest(Location.marshall);
    PickingRequest secondRes = manager.popRequest(Location.marshall);
    Assert.assertEquals(request, firstRes);
    Assert.assertNull(secondRes);
    Assert.assertEquals(0, marshallRequests.size());
  }

  /**
   * Test popRequest method when the argument is Location.load
   */
  @Test
  public void popRequestload() {
    request.updateLocation(Location.load);
    PickingRequest firstRes = manager.popRequest(Location.load);
    PickingRequest secondRes = manager.popRequest(Location.load);
    Assert.assertEquals(request, firstRes);
    Assert.assertNull(secondRes);
    Assert.assertEquals(0, loadRequests.size());
  }

  /**
   * Since the popRequest with Location.pick argument is already tested,
   * Only need to test the case where there're no outStandingPickingRequests.
   * Test when there are >= 4 orders in the system.
   */
  @Test
  public void getForPickingSuccess() {
    ArrayList<Order> tmpOrders = factory.randomOrders(4);
    for (Order order : tmpOrders) {
      manager.addOrder(order);
    }
    PickingRequest tempRequest = manager.getUnpickedRequest();
    Assert.assertNotNull(tempRequest);
    Assert.assertEquals(0, pickingRequests.size());
    Assert.assertEquals(0, ordersList.size());
  }

  /**
   * Since the popRequest with Location.pick argument is already tested,
   * Only need to test the case where there're no outStandingPickingRequests.
   * Test when there are < 4 orders in the system.
   */
  @Test
  public void getForPickingFail() {
    ArrayList<Order> tmpOrders = factory.randomOrders(2);
    for (Order order : tmpOrders) {
      manager.addOrder(order);
    }
    assert (pickingRequests.isEmpty() && ordersList.size() == 2);
    Assert.assertNull(manager.getUnpickedRequest());
  }

  /**
   * Test for addOrder method.
   */
  @Test
  public void addOrder() {
    ArrayList<Order> tmpOrders = factory.randomOrders(2);
    for (Order order : tmpOrders) {
      manager.addOrder(order);
    }
    Assert.assertEquals(tmpOrders.get(0), ordersList.get(0));
    Assert.assertEquals(tmpOrders.get(1), ordersList.get(1));
  }

  /**
   * Test for addPallets method.
   */
  @Test
  public void putPalletes() {
    String[][] pallets = factory.generatePallets(request);
    manager.addPallets(pallets, 0);
    Assert.assertArrayEquals(pallets, palletsMap.get(0));
    Assert.assertEquals(1, palletsMap.size());
  }

  /**
   * Test for popPallets method when it's successful.
   */
  @Test
  public void popPalletsSuccess() {
    String[][] pallets = factory.generatePallets(request);
    palletsMap.put(0, pallets);
    Assert.assertArrayEquals(pallets, manager.popPallets(0));
    Assert.assertEquals(0, palletsMap.size());
  }

  /**
   * Test for popPallets method when there are no pallets in storge.
   */
  @Test
  public void popPalletsFailEmpty() {
    Assert.assertNull(manager.popPallets(0));
  }

  /**
   * Test for popPallets method when the requested picking request id isn't
   * in the system.
   */
  @Test
  public void popPalletsFailMismatch() {
    String[][] pallets = factory.generatePallets(request);
    palletsMap.put(0, pallets);
    Assert.assertNull(manager.popPallets(3));
    Assert.assertArrayEquals(pallets, palletsMap.get(0));
    Assert.assertEquals(1, palletsMap.size());
  }
}