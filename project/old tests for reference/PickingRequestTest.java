//package tests;
//
//import static org.junit.Assert.assertEquals;
//import  static org.junit.Assert.assertSame;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.LinkedList;
//
//import org.junit.Before;
//import org.junit.Test;
//
//import warehousefloor.Order;
//import warehousefloor.PickingRequest;
//import util.SkuTranslator;
//
//
//
///**
// * Created by Tasbir on 2017-03-14.
// */
//public class PickingRequestTest {
//  private PickingRequest testPick;
//  private ArrayList<Order> orders;
//
//  /**
//   * Sets up the required variables for testing.
//   * @throws Exception if paths aren't valid.
//   */
//  @Before
//  public  void setUp() throws  Exception {
//    SkuTranslator.setLocations("tests/traversal_table.csv");
//    SkuTranslator.setProperties("tests/translation.csv");
//    orders = new ArrayList<Order>();
//    orders.add(new Order("Order White S"));
//    orders.add(new Order("Order White SE"));
//    orders.add(new Order("Order Beige S"));
//    orders.add(new Order("Order Blue SEL"));
//    testPick = new PickingRequest(orders, 0);
//  }
//
//  @Test
//  public void getId() throws Exception {
//    assertEquals(testPick.getId(),0);
//
//  }
//
//  @Test
//  public void getProperSkus() throws Exception {
//    LinkedList<Integer> properOrder = new LinkedList<>((Arrays.asList(1,3,9,39,2,4,10,40)));
//    assertEquals(testPick.getProperSkus(), properOrder);
//  }
//
//  @Test
//  public void getOrders() throws Exception {
//    assertSame(testPick.getOrders(), orders);
//
//  }
//
//  @Test
//  public void compareTo() throws Exception {
//    PickingRequest testPickCompare = new PickingRequest(orders, 1);
//    assertEquals(testPick.compareTo(testPickCompare),-1);
//    assertEquals(testPickCompare.compareTo(testPick),1);
//    assertEquals(testPick.compareTo(testPick),0);
//  }
//
//}