//package tests;
//
//import org.junit.After;
//import org.junit.Assert;
//import org.junit.Before;
//import org.junit.Test;
//import warehousefloor.Truck;
//
///**
// * The unit tests for the Truck class.
// *
// * @author Chaitanya
// */
//public class TruckTest {
//
//  private Truck truck;
//
//  @Before
//  public void setUp() {
//    truck = new Truck(3);
//  }
//
//  @After
//  public void tearDown() {
//
//  }
//
//  @Test
//  public void testUnSuccessfulAdd() {
//    int[] cargo = {2, 3, 2, 3};
//    Assert.assertEquals(false, truck.addCargo(cargo, cargo, 23));
//  }
//
//  @Test
//  public void testSuccessfulAdd() {
//    int[] cargo = {2, 3, 2, 3};
//    Assert.assertEquals(true, truck.addCargo(cargo, cargo, 3));
//    Assert.assertEquals(true, truck.addCargo(cargo, cargo, 4));
//  }
//
//  @Test
//  public void testNotIsFull() {
//    Assert.assertEquals(false, truck.isFull());
//  }
//
//  @Test
//  public void testNotIsFull1() {
//    int[] cargo = {2, 3, 2, 3};
//    for (int i = 0; i < 19; i++) {
//      truck.addCargo(cargo, cargo, i + 3);
//    }
//    Assert.assertEquals(false, truck.isFull());
//  }
//
//  @Test
//  public void testIsFull() {
//    int[] cargo = {2, 3, 2, 3};
//    for (int i = 0; i < 20; i++) {
//      truck.addCargo(cargo, cargo, i + 3);
//    }
//    Assert.assertEquals(true, truck.isFull());
//  }
//
//  @Test
//  public void testFullAdd() {
//    int[] cargo = {2, 3, 2, 3};
//    for (int i = 0; i < 20; i++) {
//      truck.addCargo(cargo, cargo, i + 3);
//    }
//    Assert.assertEquals(false, truck.addCargo(cargo, cargo, 19 + 4));
//  }
//
//}
