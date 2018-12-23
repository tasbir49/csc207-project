//package tests;
//
//import static org.junit.Assert.assertEquals;
//
//import org.junit.After;
//import org.junit.Before;
//import org.junit.Test;
//import warehousefloor.Order;
//import util.SkuTranslator;
//
//
///**
// * The unit tests for the Order class.
// *
// * @author Chaitanya
// */
//public class OrderTests {
//
//  private Order order;
//
//  @Before
//  public void setUp() throws Exception {
//    SkuTranslator.setLocations("tests/translation.csv");
//    SkuTranslator.setProperties("tests/traversal_table.csv");
//    order = new Order("Order SomeColour SomeModel");
//  }
//
//  @After
//  public void tearDown() throws Exception {}
//
//  @Test
//  public void testGetSkuFront() {
//    assertEquals(-1, order.getSkus()[0]);
//  }
//
//  @Test
//  public void testGetSkuBack() {
//    assertEquals(-1, order.getSkus()[1]);
//  }
//
//  @Test
//  public void testToString() {
//    SkuTranslator.setLocations("tests/translation.csv");
//    SkuTranslator.setProperties("tests/traversal_table.csv");
//    assertEquals(order.toString(), "SomeModel, SomeColour");
//  }
//
//}
