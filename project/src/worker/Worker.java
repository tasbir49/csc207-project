package worker;

import fascia.PickingRequest;
import java.util.LinkedList;
import java.util.Observable;
import java.util.logging.Level;
import util.MasterSystem;
import warehousefloor.Location;

/**
 * A generic worker class.
 *
 * @author Andrew
 */
public abstract class Worker extends Observable {

  private String name;
  private LinkedList<String> toBeScanned = new LinkedList<>();
  private PickingRequest currPickingReq;
  private int scanCount = 0;
  private MasterSystem masterSystem;

  /**
   * Initialize a new instance of Worker.
   *
   * @param name the name of the worker
   * @param masterSystem the MasterSystem the worker belongs to
   */
  Worker(String name, MasterSystem masterSystem) {
    this.name = name;
    this.masterSystem = masterSystem;
    addObserver(masterSystem.getWorkerManager());
  }

  /**
   * The action for a worker trying to ready i.e, the ready button is pressed.
   */
  public void tryReady() {
    setChanged();
    notifyObservers(true);
  }

  /**
   * What a worker does when it becomes ready.
   */
  abstract void readyAction();

  /**
   * The action for a worker being finished.
   */
  public void finish() {
    setChanged();
    notifyObservers(false);
  }

  /**
   * What a worker does when it finishes.
   */
  abstract void finishAction();

  /**
   * The getExpectedScan order that the worker should scan in. The picker class overwrites this
   * method.
   *
   * @return The getExpectedScan order that the worker should scan in.
   */
  LinkedList<String> getScanOrder() {
    return new LinkedList<>(currPickingReq.getProperSkus());
  }

  /**
   * Returns the expected sku (a.k.a the sku that is next to be scanned).
   *
   * @return the expected sku.
   */
  private String getExpectedScan() {
    return toBeScanned.pop();
  }

  /**
   * The result of a scan from a worker.
   *
   * @param sku the sku being scanned.
   * @param expected the getExpectedScan sku to match up with the sku being scanned.
   * @return true if the scan matches, else returns false.
   */
  boolean scanResult(String sku, String expected) {
    if (sku.equals(expected)) {
      masterSystem.getLogger().log(Level.INFO,
          getClass().getSimpleName() + " " + name + " SKU: " + sku + " correct scan");
      return true;
    } else {
      masterSystem.getLogger().log(Level.WARNING,
          getClass().getSimpleName() + " " + name + " SKU: " + sku + " incorrect scan");
      return false;
    }
  }

  /**
   * The action the worker takes when it scans.
   *
   * @param sku the sku being scanned.
   */
  public void scan(String sku) {
    masterSystem.getLogger().log(Level.INFO,
        getClass().getSimpleName() + " " + name + " scanned sku: " + sku);
    if (getCurrPickingReq() != null && scanResult(sku, getExpectedScan())) {
      addScanCount();
    } else if (getCurrPickingReq() == null) {
      masterSystem.getLogger().log(Level.WARNING,
          getClass().getSimpleName() + " " + getName() + " Unneeded Scan");
    }
  }

  /**
   * An event for rescan.
   */
  public void rescan() {
    masterSystem.getLogger().log(Level.INFO,
        getClass().getSimpleName() + " " + getName() + " " + "rescanned");
    resetScanCount();
    toBeScanned = getScanOrder();
  }

  /**
   * A getter for currPickingReq.
   *
   * @return the current Picking Request of this worker.
   */
  PickingRequest getCurrPickingReq() {
    return currPickingReq;
  }

  /**
   * A getter for toBeScanned.
   *
   * @return the sku's to be scanned.
   */
  LinkedList<String> getToBeScanned() {
    return toBeScanned;
  }

  /**
   * A getter for the name.
   *
   * @return name of the worker.
   */
  public String getName() {
    return name;
  }

  /**
   * Sets the current picking request for this worker.
   *
   * @param req the picking request the worker is handling.
   */
  void setCurrPickingReq(PickingRequest req) {
    currPickingReq = req;
  }

  /**
   * Add 1 to scan count.
   */
  void addScanCount() {
    scanCount++;
  }

  /**
   * Resets the scanCount to 0.
   */
  private void resetScanCount() {
    scanCount = 0;
  }

  /**
   * A helper method to try to make worker become ready.
   */
  void readyHelper() {
    String job = getClass().getSimpleName();
    String name = getName();
    if (getCurrPickingReq() == null) {
      masterSystem.getLogger().log(Level.WARNING, job + " " + name + ", No requests to process!");
    } else {
      masterSystem.getLogger().log(Level.WARNING, job + " " + name + " is ready.");
      readyAction();
      resetScanCount();
      toBeScanned = getScanOrder();
    }
  }

  /**
   * A helper method to try to make worker become finished.
   */
  void finishHelper() {
    String job = getClass().getSimpleName();
    String name = getName();
    if (getCurrPickingReq() == null) {
      masterSystem.getLogger().log(Level.WARNING,
          job + " " + name + " can't finish with no request!");
    } else if (getCurrPickingReq() != null && scanCount < getScanOrder().size()) {
      getCurrPickingReq().updateLocation(Location.pick);
      masterSystem.getLogger().log(Level.WARNING,
          job + name + " can't finish without scanning all correct SKUs!");
      masterSystem.getLogger().log(Level.WARNING,
          "Picking request " + getCurrPickingReq().getId() + " has been sent to be repicked.");
    } else {
      finishAction();
    }
    setCurrPickingReq(null);
    resetScanCount();
    getToBeScanned().clear();
  }
}
