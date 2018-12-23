package fascia;

import util.SkuTranslator;

/**
 * A class to represent Orders.
 *
 * @author Chaitanya
 */

public class Order {

  private String[] skus = new String[2];
  private String colour;
  private String model;

  /**
   * Initializes a new order based on input to the system.
   *
   * @param orderAsString The Order in the format "Order Colour Model"
   * @param skuTranslator the skuTranslator
   * @throws UnsupportedOperationException when the skuTranslator returned null
   *                                       for one of the getSku calls.
   */
  public Order(String orderAsString, SkuTranslator skuTranslator)
      throws UnsupportedOperationException {
    String[] orderSplit = orderAsString.split("\\s");
    colour = orderSplit[1];
    model = orderSplit[2];
    skus[0] = skuTranslator.getSku(colour, model, true);
    skus[1] = skuTranslator.getSku(colour, model, false);
    if (skus[0] == null | skus[1] == null) {
      throw new UnsupportedOperationException();
    }
  }


  /**
   * Returns the SKU numbers of the order.
   *
   * @return Array of 2 ints, the SKU number of the front object, then the back.
   */
  public String[] getSkus() {
    return skus;
  }

  /**
   * Returns the Order as a String.
   *
   * @return String of the Order in the form "model, colour".
   */
  public String toString() {
    return model + ", " + colour;
  }

}
