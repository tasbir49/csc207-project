package warehousefloor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.logging.Level;

import util.MasterSystem;

public class WarehouseFloor {

  private final int maxStock;
  private final int minStock;
  private HashMap<String, Integer> inventory = new HashMap<>();
  private ArrayList<Truck> trucks = new ArrayList<>();
  private LinkedList<String> toBeReplenished = new LinkedList<>();
  private String warehouseFile;
  private MasterSystem masterSystem;

  /**
   * Initialize a new warehousefloor.
   *
   * @param warehouseFile the file path to read inventory from.
   * @param max           the max stock level.
   */
  public WarehouseFloor(
      String warehouseFile,
      MasterSystem masterSystem,
      int max, int min
  ) {
    this.masterSystem = masterSystem;
    this.maxStock = max;
    this.minStock = min;
    this.warehouseFile = warehouseFile;
  }

  /**
   * Set the stock levels from the initial file.
   */
  public void setInventory() {
    //initial stock levels are at max unless specified otherwise from input file
    for (String sku : masterSystem.getSkuTranslator().getAllSku()) {
      inventory.put(sku, maxStock);
    }

    //updating stock levels from file
    if (!masterSystem.getFileSystem().getFileContent(warehouseFile).isEmpty()) {
      for (String s : masterSystem.getFileSystem()
          .getFileContent(warehouseFile)) {
        ArrayList<String> line = new ArrayList<>(Arrays.asList(s.split(",")));
        String[] location = line.subList(0, 4).toArray(new String[4]);
        String sku = masterSystem.getSkuTranslator()
            .getSkuFromLocation(location);
        int amount = Integer.valueOf(line.get(4));
        this.inventory.put(sku, amount);
        //Replenish request is always made when stock level is at the min amount or less.
        if (amount <= minStock) {
          toBeReplenished.add(sku);
        }
      }
    }
  }

  /**
   * Add SKUs to the inventory.
   *
   * @param sku    the sku of the facsia to be added
   * @param amount the amount to be added
   */
  public void addSku(String sku, int amount) {
    if (inventory.containsKey(sku)) {
      inventory.put(sku, Math.min(maxStock, inventory.get(sku) + amount));
    } else {
      masterSystem.getLogger().log(Level.WARNING, "SKU does not exist");
    }
  }

  /**
   * Removes one quantity of SKU from inventory if there are any. If the
   * quantity is <= minStock a replenish request is created for that SKU if there isn't
   * any.
   *
   * @param sku the sku being removed
   * @return true if the sku is taken.
   */
  public boolean removeSku(String sku) {
    if (!inventory.containsKey(sku)) {
      masterSystem.getLogger().log(Level.WARNING,"SKU " + sku + " does not exist");
      return false;
    }
    if (inventory.get(sku) < 1) {
      masterSystem.getLogger().log(Level.WARNING, "Empty Rack Removal Attempt");
      return false;
    } else {
      inventory.put(sku, inventory.get(sku) - 1);
      masterSystem.getLogger().log(Level.INFO,"One SKU " + String.valueOf(sku) + " taken.");
    }
    if (inventory.get(sku) <= minStock && !toBeReplenished.contains(sku)) {
      // put a replenish request in the system when the amount is <= 5
      toBeReplenished.add(sku);
      masterSystem.getLogger().log(Level.INFO, "Repleneish Request for SKU:" + sku);
    }
    return true;
  }

  /**
   * Add a truck to the warehousefloor.
   *
   * @param truck the truck to be added.
   */
  public void addTruck(Truck truck) {
    trucks.add(truck);
  }

  /**
   * Get the first truck that's not full.
   *
   * @return the not full truck
   */
  public Truck getFirstNonFullTruck() {
    for (Truck t : trucks) {
      if (!t.isFull()) {
        return t;
      }
    }
    return null;
  }

  /**
   *  Creates the output files. The output files are the loaded orders, and the updated inventory.
   */
  public void createOutputFiles() {
    ArrayList<String> finalCsv = new ArrayList<>();
    //Create arrayList of Sku quantities
    for (String sku : this.inventory.keySet()) {
      if (inventory.get(sku) < maxStock) {
        finalCsv.add(
            masterSystem.getSkuTranslator().getLocation(sku) + "," + inventory
                .get(sku));
      }
    }

    //Write the contents to file
    masterSystem.getFileSystem().setWritingFileContents(masterSystem.getOutPutPath() + "final"
        + ".csv", finalCsv);
    masterSystem.getFileSystem().writeAll();
  }

  /**
   * A method to write loaded orders to a file.
   *
   * @param order the order to be logged
   */
  public void writeLoadedOrders(String order) {
    masterSystem.getFileSystem().getWritingFileForEdit(masterSystem.getOutPutPath() + "orders"
        + ".csv").add(order);
  }

  /**
   * Gets the earliest replenish request made.
   *
   * @return The sku that must be replenished.
   */
  public String getFirstReplenishRequest() {
    return toBeReplenished.isEmpty() ? null : toBeReplenished.pop();
  }

  /**
   * Add a sku to the front of replenish requests to be made.
   *
   * @param toBeRemoved the sku to be added.
   */
  public void addReplenishRequestToFront(String toBeRemoved) {
    toBeReplenished.add(0, toBeRemoved);
  }
}
