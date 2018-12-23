package tests;

import fascia.Order;
import fascia.PickingRequest;
import fascia.PickingRequestManager;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;
import util.MasterSystem;
import util.MasterSystemFactory;

/**
 * Generates PickingRequest for testing.
 */
class TestFactory {

  private String[] orderStrings;
  private MasterSystem testEnviroment;

  TestFactory() {
    orderStrings = new String[]{
        "Order White S",
        "Order White SE",
        "Order White SES",
        "Order White SEL",
        "Order Beige S",
        "Order Beige SE",
        "Order Beige SES",
        "Order Beige SEL",
        "Order Red S",
        "Order Red SE",
        "Order Red SES",
        "Order Red SEL",
        "Order Green S",
        "Order Green SE",
        "Order Green SES",
        "Order Green SEL",
        "Order Blue S",
        "Order Blue SE",
        "Order Blue SES",
        "Order Blue SEL",
        "Order Black S",
        "Order Black SE",
        "Order Black SES",
        "Order Black SEL"
    };
    testEnviroment = getTestEnviroment();
  }

  /**
   * Returns a new picking request object.
   *
   * @param id      the picking request id.
   * @param manager the PickingRequestManager
   * @return PickingRequest
   */
  PickingRequest pickingRequest(int id, PickingRequestManager
      manager) {

    return new PickingRequest(randomOrders(4), id, manager);
  }

  /**
   * Returns random orders.
   *
   * @param amount the amount of orders to return
   * @return ArrayList of random orders.
   */
  ArrayList<Order> randomOrders(int amount) {
    ArrayList<Order> result = new ArrayList<>();
    for (int i = 0; i < amount; i++) {
      int rand = new Random().nextInt(orderStrings.length);
      result.add(
          new Order(orderStrings[rand], testEnviroment.getSkuTranslator()));
    }
    return result;
  }

  /**
   * Generate front and back pallets from a given picking request.
   *
   * @param pickingRequest the picking request
   * @return front and back pallets
   */
  String[][] generatePallets(PickingRequest pickingRequest) {
    LinkedList<String> skus = pickingRequest.getProperSkus();
    String[] frontPallet = new String[4];
    String[] backPallet = new String[4];
    for (int i = 0; i < 4; i++) {
      frontPallet[i] = skus.get(i);
      backPallet[i] = skus.get(i + 4);
    }
    return new String[][]{frontPallet, backPallet};
  }

  /**
   * Returns a MasterSystem that's setup with all the proper subsystems.
   *
   * @return MasterSystem
   */
  MasterSystem getTestEnviroment() {
    String warehouseFile = "../initial.csv";
    String translationFile = "../translation.csv";
    String traversalFile = "../traversal_table.csv";
    String outFile = "tests/";
    return MasterSystemFactory.getMasterSystem(warehouseFile, translationFile,
        traversalFile, outFile, 30,5, false);
  }

  /**
   * Supress the print stream for testing.
   */
  static void supressPrint() {
    System.setOut(new PrintStream(new OutputStream() {
      @Override
      public void write(int num) throws IOException {
        //Do nothing
      }
    }));
  }
}
