//package tests;
//
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertNull;
//
//import org.junit.Before;
//import org.junit.Test;
//import  util.SkuTranslator;
//
//
//
///**
// * Created by Tasbir on 2017-03-14.
// */
//public class SkuTranslatorTest {
//  @Before
//  public void setUp() throws Exception {
//    SkuTranslator.setLocations("tests/traversal_table.csv");
//    SkuTranslator.setProperties("tests/translation.csv");
//  }
//
//  @Test
//  public void getSku() throws Exception {
//    System.out.println(System.getProperty("user.dir"));
//    assertEquals(SkuTranslator.getSku("White","s",true),1);
//    assertEquals(SkuTranslator.getSku("White","s",false),2);
//    assertEquals(SkuTranslator.getSku("NOCOLOUR","ABCDEFG",true),-1);
//
//  }
//
//  @Test
//  public void getLocation() throws Exception {
//    assertEquals(SkuTranslator.getLocation(1),"A,0,0,0");
//    assertEquals(SkuTranslator.getLocation(48), "B,1,2,3");
//    assertEquals(SkuTranslator.getLocation(13),"A,1,0,0");
//    assertNull(SkuTranslator.getLocation(-1));
//
//  }
//
//  @Test
//  public void getSkuFromLocation() throws Exception {
//    assertEquals(SkuTranslator.getSkuFromLocation(new String[]{"A","0","0","1"}),2);
//    assertEquals(SkuTranslator.getSkuFromLocation(new String[]{"A","0","0","I am me"}),-1);
//
//  }
//
//  @Test
//  public void getAllSku() throws Exception {
//    assertEquals(SkuTranslator.getAllSku().size(),48);
//  }
//
//}