package tests;

import fascia.Order;
import fascia.PickingRequest;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedList;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import util.MasterSystem;
import warehousefloor.Location;

/**
 * Unit tests for PickingRequest class.
 */
public class PickingRequestTest {

  private static TestFactory testFactory;
  private static MasterSystem masterSystem;
  private static Field orders;
  private PickingRequest pickingRequest;
  private ArrayList<Order> ordersList;

  /**
   * Set up some static variables for all the tests.
   */
  @BeforeClass
  public static void setUpBeforeClass() throws NoSuchFieldException {
    testFactory = new TestFactory();
    masterSystem = testFactory.getTestEnviroment();
    orders = PickingRequest.class.getDeclaredField("orders");
    orders.setAccessible(true);
  }

  /**
   * Get a new picking request object for each test.
   */
  @Before
  public void setUp() throws IllegalAccessException {
    pickingRequest = testFactory.pickingRequest(69, masterSystem
        .getPickingRequestManager());
    ordersList = (ArrayList<Order>) orders.get(pickingRequest);
  }

  /**
   * Test for getId method.
   */
  @Test
  public void getId() {
    Assert.assertEquals(69, pickingRequest.getId());
  }

  /**
   * Test for the getProperSkus method.
   */
  @Test
  public void getProperSkus() {
    LinkedList<String> expected = new LinkedList<>();
    for (Order order : ordersList) {
      expected.add(order.getSkus()[0]);
    }
    for (Order order : ordersList) {
      expected.add(order.getSkus()[1]);
    }
    Assert.assertEquals(expected, pickingRequest.getProperSkus());
  }

  /**
   * Test for the getOrders method.
   */
  @Test
  public void getOrders() {
    Assert.assertEquals(ordersList, pickingRequest.getOrders());
  }

  /**
   * Test for when two picking request are equal.
   */
  @Test
  public void compareToEqual() {
    PickingRequest target = testFactory
        .pickingRequest(69, masterSystem.getPickingRequestManager());
    Assert.assertEquals(0, pickingRequest.compareTo(target));
  }

  /**
   * Test for when the pickingRequest variable is less than a new
   * PickingRequest object.
   */
  @Test
  public void compareToLess() {
    PickingRequest target = testFactory
        .pickingRequest(420, masterSystem.getPickingRequestManager());
    Assert.assertTrue(pickingRequest.compareTo(target) < 0);
  }

  /**
   * Test for when the pickingRequest variable is more than a new
   * PickingRequest object.
   */
  @Test
  public void compareToMore() {
    PickingRequest target = testFactory
        .pickingRequest(1, masterSystem.getPickingRequestManager());
    Assert.assertTrue(pickingRequest.compareTo(target) > 0);
  }

  /**
   * Test for updateLocation when the argument is Location.pick
   */
  @Test
  public void updateLocationPick() {
    pickingRequest.updateLocation(Location.pick);
    Assert.assertEquals(masterSystem.getPickingRequestManager()
        .popRequest(Location.pick), pickingRequest);
  }

  /**
   * Test for updateLocation when the argument is Location.marshall
   */
  @Test
  public void updateLocationMarshall() {
    pickingRequest.updateLocation(Location.marshall);
    Assert.assertEquals(masterSystem.getPickingRequestManager()
        .popRequest(Location.marshall), pickingRequest);
  }

  /**
   * Test for updateLocation when the argument is Location.load
   */
  @Test
  public void updateLocationLoad() {
    pickingRequest.updateLocation(Location.load);
    Assert.assertEquals(masterSystem.getPickingRequestManager()
        .popRequest(Location.load), pickingRequest);
  }
}