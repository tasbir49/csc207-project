package worker;

import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;
import util.MasterSystem;
import warehousefloor.Location;

/**
 * A class to keep track of all workers.
 *
 * @author Peijun
 */
public class WorkerManager implements Observer {

  private HashMap<String, Picker> pickers = new HashMap<>();
  private HashMap<String, Loader> loaders = new HashMap<>();
  private HashMap<String, Sequencer> sequencers = new HashMap<>();
  private HashMap<String, Replenisher> replenishers = new HashMap<>();
  private MasterSystem masterSystem;

  public WorkerManager(MasterSystem masterSystem) {
    this.masterSystem = masterSystem;
  }

  /**
   * This method is called whenever the observed observableWorker is changed.
   * Handles what happens when the worker is ready for a new duty or they
   * finished their current.
   *
   * @param observableWorker the Worker object to be updated.
   * @param isReady          whether the worker is ready or not. If false, then
   *                         the worker finished.
   */
  @Override
  public void update(Observable observableWorker, Object isReady) {
    if (observableWorker == null
        || isReady == null
        || !(isReady instanceof Boolean
        && observableWorker instanceof Worker)) {
      throw new UnsupportedOperationException();
    } else {
      boolean readyBool = (boolean) isReady;
      Worker worker = (Worker) observableWorker;
      if (readyBool) {
        assignPickingRequest(worker);
        worker.readyHelper();
      } else {
        worker.finishHelper();
      }
    }
  }

  /**
   * Assign a picking request to worker based on its type.
   *
   * @param worker the worker to assign picking request to.
   */
  private void assignPickingRequest(Worker worker) {
    if (worker instanceof Picker) {
      worker.setCurrPickingReq(
          masterSystem.getPickingRequestManager().getUnpickedRequest());
    } else if (worker instanceof Sequencer) {
      worker.setCurrPickingReq(masterSystem
          .getPickingRequestManager().popRequest(Location.marshall));
    } else if (worker instanceof Loader) {
      worker.setCurrPickingReq(
          masterSystem.getPickingRequestManager().popRequest(Location.load));
    }
  }

  /**
   * Add a loader to the warehousefloor.
   *
   * @param loader the loader to be added
   */
  public void addLoader(Loader loader) {
    loaders.put(loader.getName(), loader);
  }

  /**
   * Add a sequencer to the warehousefloor.
   *
   * @param sequencer the sequencer to be added
   */
  public void addSequencer(Sequencer sequencer) {
    sequencers.put(sequencer.getName(), sequencer);
  }

  /**
   * Add a picker to the warehousefloor.
   *
   * @param picker the picker to be added
   */
  public void addPicker(Picker picker) {
    pickers.put(picker.getName(), picker);
  }

  /**
   * Add a replenisher to the warehousefloor.
   *
   * @param replenisher the replenisher to be added
   */
  public void addReplenisher(Replenisher replenisher) {
    replenishers.put(replenisher.getName(), replenisher);
  }

  /**
   * Get a loader by name.
   *
   * @param name the name
   * @return the loader with that name
   */
  public Loader getLoader(String name) {
    return loaders.get(name);
  }


  /**
   * Get a picker by name.
   *
   * @param name the name
   * @return the picker with that name
   */
  public Picker getPicker(String name) {
    return pickers.get(name);
  }

  /**
   * Get a sequencer by name.
   *
   * @param name the name
   * @return the sequencer with that name
   */
  public Sequencer getSequencer(String name) {
    return sequencers.get(name);
  }

  /**
   * Get a replenisher by name.
   *
   * @param name the name
   * @return the replenisher with that name
   */
  public Replenisher getReplenisher(String name) {
    return replenishers.get(name);
  }
}
