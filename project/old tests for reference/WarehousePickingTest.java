//package tests;
//
//import static org.junit.Assert.fail;
//
//import java.io.IOException;
//import java.util.ArrayList;
//
//import org.junit.After;
//import org.junit.Assert;
//import org.junit.Before;
//import org.junit.Test;
//
//import util.SkuTranslator;
//import warehousefloor.WarehousePicking;
//
//public class WarehousePickingTest {
//
//  ArrayList<Integer> skus;
//
//  /**
//   * Instantiates an sku ArrayList of Integers and sets up the SkuTranslator.
//   *
//   * @throws Exception File not found, probably
//   */
//  @Before
//  public void setUp() throws Exception {
//    SkuTranslator.setLocations("tests/traversal_table.csv");
//    SkuTranslator.setProperties("tests/translation.csv");
//
//    skus = new ArrayList<Integer>();
//    skus.add(1);
//    skus.add(2);
//    skus.add(3);
//    skus.add(4);
//  }
//
//  @After
//  public void tearDown() throws Exception {
//    skus = null;
//  }
//
//  @Test
//  public void testOptimize() {
//    ArrayList<String> expected = new ArrayList<>();
//    expected.add(SkuTranslator.getLocation(1));
//    expected.add(SkuTranslator.getLocation(2));
//    expected.add(SkuTranslator.getLocation(3));
//    expected.add(SkuTranslator.getLocation(4));
//
//    ArrayList<String> actual = WarehousePicking.optimize(skus);
//
//    Assert.assertEquals(expected, actual);
//  }
//
//}
