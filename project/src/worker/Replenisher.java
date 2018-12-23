package worker;

import java.util.logging.Level;
import util.MasterSystem;


/**
 * This worker replenish the stock levels when asked to.
 *
 * @author Peijun
 */
public class Replenisher {

  private String name;
  private MasterSystem masterSystem;
  private String sku;
  private boolean needed;

  /**
   * Initialize a Replenisher object.
   *
   * @param name         the name of the Replenisher
   * @param masterSystem where the Replenisher works at
   */
  public Replenisher(String name, MasterSystem masterSystem) {
    this.name = name;
    this.masterSystem = masterSystem;
  }

  /**
   * A getter for its name.
   *
   * @return its name
   */
  public String getName() {
    return name;
  }

  /**
   * Replenish the warehousefloor when the stock level <= 5.
   *
   * @param sku the sku to be replenished
   */
  public void scan(String sku) {
    needed = this.sku != null && sku.equals(this.sku);
  }

  /**
   * Ready action for the replenisher.
   */
  public void ready() {
    sku = masterSystem.getWarehouseFloor().getFirstReplenishRequest();
  }

  /**
   * A method for when a replenisher is finished.
   */
  public void finish() {
    if (needed) {
      masterSystem.getLogger().log(Level.INFO,"SKU " + sku + " has been "
          + "replenished by " + name);
      masterSystem.getWarehouseFloor().addSku(sku, 25);
    } else {
      masterSystem.getLogger().log(Level.WARNING, "Unneeded replenish, nothing was added to "
          + "the inventory");
    }
    if (sku != null) {
      masterSystem.getWarehouseFloor().addReplenishRequestToFront(sku);
    }
    sku = null;
    needed = false;
  }

}
