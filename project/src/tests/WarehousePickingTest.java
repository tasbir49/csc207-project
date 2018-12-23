package tests;

import fascia.Order;
import java.util.ArrayList;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import util.SkuTranslator;
import worker.WarehousePicking;

/**
 * Unit test for WarehousePicking class.
 */
public class WarehousePickingTest {

  private static SkuTranslator translator;
  private static TestFactory factory;

  /**
   * Set up the variables once before all tests.
   */
  @BeforeClass
  public static void setUpBeforeClass() {
    factory = new TestFactory();
    translator = factory.getTestEnviroment().getSkuTranslator();
  }

  /**
   * Test for optimize method when it's successful.
   */
  @Test
  public void optimizeSuccess() {
    ArrayList<Order> orders = factory.randomOrders(4);
    ArrayList<String> skus = new ArrayList<>();
    for (Order order : orders) {
      skus.add(order.getSkus()[0]);
      skus.add(order.getSkus()[1]);
    }
    ArrayList<String> expected = new ArrayList<>();
    for (String sku : skus) {
      expected.add(translator.getLocation(sku));
    }
    Assert.assertEquals(expected, WarehousePicking.optimize(skus, translator));
  }

  /**
   * Test for optimize method when it fails.
   */
  @Test
  public void optimizeFail() {
    ArrayList<Order> orders = factory.randomOrders(4);
    ArrayList<String> skus = new ArrayList<>();
    for (Order order : orders) {
      skus.add(order.getSkus()[0]);
      skus.add(order.getSkus()[1]);
    }
    skus.set(3, "asdasd");
    Assert.assertNull(WarehousePicking.optimize(skus, translator));
  }
}