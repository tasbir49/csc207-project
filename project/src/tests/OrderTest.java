package tests;

import fascia.Order;
import java.util.regex.Pattern;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import util.SkuTranslator;

/**
 * Unit test or Order class.
 */
public class OrderTest {

  private static SkuTranslator skuTranslator;
  private static TestFactory testFactory;

  /**
   * Init the testFactory and skuTranslator once before the tests.
   */
  @BeforeClass
  public static void setUp() {
    testFactory = new TestFactory();
    skuTranslator = testFactory.getTestEnviroment().getSkuTranslator();
  }

  /**
   * Test for getSkus.
   */
  @Test
  public void getSkus() {
    Order order = testFactory.randomOrders(1).get(0);
    Assert.assertTrue(order.getSkus()[0] != null
        && order.getSkus()[1] != null);
  }

  /**
   * Test for bad initialize attempt.
   */
  @Test(expected = UnsupportedOperationException.class)
  public void initFail() {
    new Order("kas das d", skuTranslator);
  }

  /**
   * Test for toString.
   */
  @Test
  public void toStringTest() {
    Order order = testFactory.randomOrders(1).get(0);
    String res = order.toString();
    Assert.assertTrue(Pattern.matches("[A-Z]+, [A-Z][a-z]+", res));
  }
}