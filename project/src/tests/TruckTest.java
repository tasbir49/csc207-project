package tests;

import java.lang.reflect.Field;
import java.util.ArrayList;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import util.MasterSystem;
import warehousefloor.Truck;

/**
 * Unit test for Truck class.
 */
public class TruckTest {

  private static TestFactory factory;
  private static Truck truck;
  private static MasterSystem masterSystem;
  private static ArrayList<ArrayList<String[]>> cargoList;

  /**
   * We will use the same truck, masterSystem, and factory for all tests.
   * Also setup the cargo field.
   */
  @BeforeClass
  public static void setUpBeforeClass()
      throws NoSuchFieldException, IllegalAccessException {
    factory = new TestFactory();
    truck = new Truck(0);
    masterSystem = factory.getTestEnviroment();
    Field cargo = Truck.class.getDeclaredField("cargo");
    cargo.setAccessible(true);
    cargoList = (ArrayList<ArrayList<String[]>>) cargo.get(truck);
  }

  /**
   * Test for addCargo to the left.
   */
  private void addCargoLeft() {
    String[][] pallets = factory.generatePallets(
        factory.pickingRequest(0, masterSystem.getPickingRequestManager()));
    String[] frontPallet = pallets[0];
    String[] backPallet = pallets[1];
    Assert.assertTrue(truck.addCargo(frontPallet, backPallet, 0));
    Assert.assertEquals(1, cargoList.size());
    Assert.assertArrayEquals(frontPallet, cargoList.get(0).get(0));
    Assert.assertArrayEquals(backPallet, cargoList.get(0).get(1));
  }

  /**
   * Test for addCargo to the right.
   */
  private void addCargoRight() {
    String[][] pallets = factory.generatePallets(
        factory.pickingRequest(1, masterSystem.getPickingRequestManager()));
    String[] frontPallet = pallets[0];
    String[] backPallet = pallets[1];
    Assert.assertTrue(truck.addCargo(frontPallet, backPallet, 1));
    Assert.assertEquals(1, cargoList.size());
    Assert.assertArrayEquals(frontPallet, cargoList.get(0).get(2));
    Assert.assertArrayEquals(backPallet, cargoList.get(0).get(3));
  }

  /**
   * Test for addCargo fail.
   */
  private void addCargoFail() {
    String[][] pallets = factory.generatePallets(
        factory.pickingRequest(3, masterSystem.getPickingRequestManager()));
    String[] frontPallet = pallets[0];
    String[] backPallet = pallets[1];
    Assert.assertFalse(truck.addCargo(frontPallet, backPallet, 3));
    Assert.assertEquals(1, cargoList.size());
  }


  /**
   * Test for wheh addCargo is called when the cargo list is full.
   */
  private void addCargoFull() throws IllegalAccessException {
    for (int i = 0; i < 13; i++) {
      cargoList.add(new ArrayList<>());
    }
    String[][] pallets = factory.generatePallets(
        factory.pickingRequest(2, masterSystem.getPickingRequestManager()));
    String[] frontPallet = pallets[0];
    String[] backPallet = pallets[1];
    Assert.assertFalse(truck.addCargo(frontPallet, backPallet, 2));
  }

  /**
   * We want to test in this specific order.
   */
  @Test
  public void testAll() throws IllegalAccessException {
    addCargoLeft();
    addCargoRight();
    addCargoFail();
    addCargoFull();
  }
}