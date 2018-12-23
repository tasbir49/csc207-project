package tests;

import java.util.ArrayList;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import util.SkuTranslator;

/**
 * Unit test for SkuTranslator class.
 */
public class SkuTranslatorTest {

  private static SkuTranslator translator;

  /**
   * Setup a SkuTranslator instance for all the tests to use.
   */
  @BeforeClass
  public static void setUpBeforeClass() {
    translator = new TestFactory().getTestEnviroment().getSkuTranslator();
  }

  /**
   * Test for getSku when it's for the front sku.
   */
  @Test
  public void getSkuFront() {
    Assert.assertEquals("9", translator.getSku("Beige", "S", true));
  }

  /**
   * Test for getSku when it's for the back sku.
   */
  @Test
  public void getSkuBack() {
    Assert.assertEquals("24", translator.getSku("Red", "SEL", false));
  }

  /**
   * Test for getSku when the sku isn't in translation table.
   */
  @Test
  public void getSkuFail() {
    Assert.assertNull(translator.getSku("lol", "xd", true));
  }

  /**
   * Test for get location success.
   */
  @Test
  public void getLocationSuccess() {
    Assert.assertEquals("A,0,2,1", translator.getLocation("10"));
  }

  /**
   * Test for get location fail.
   */
  @Test
  public void getLocationFail() {
    Assert.assertNull(translator.getLocation("asdsdasad"));
  }

  /**
   * Test for getSkuFromLocation success.
   */
  @Test
  public void getSkuFromLocationSuccess() {
    Assert.assertEquals("38",
        translator.getSkuFromLocation(new String[]{"B", "1", "0", "1"}));
  }

  /**
   * Test for getSkuFromLocation fail.
   */
  @Test
  public void getSkuFromLocationFail() {
    Assert.assertNull(
        translator.getSkuFromLocation(new String[]{"asdasd", "NANI"}));
  }

  /**
   * Test for getAllSku method.
   */
  @Test
  public void getAllSku() {
    ArrayList<String> expected = new ArrayList<>();
    for (int i = 1; i < 49; i++) {
      expected.add(String.valueOf(i));
    }
    Assert.assertEquals(expected, translator.getAllSku());
  }
}