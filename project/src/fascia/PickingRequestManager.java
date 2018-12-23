package fascia;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;
import warehousefloor.Location;

/**
 * A class to keep track of all PickingRequests.
 *
 * @author Peijun
 */
public class PickingRequestManager implements Observer {

  private LinkedList<PickingRequest> outStandingPickingRequests = new
      LinkedList<>();
  private LinkedList<PickingRequest> marshallingArea = new LinkedList<>();
  private LinkedList<PickingRequest> loadingArea = new LinkedList<>();
  private HashMap<Integer, String[][]> pallets = new HashMap<>();
  private LinkedList<Order> orders = new LinkedList<>();
  private int pickingReqId = 0;
  private Logger masterLogger;

  public void setMasterLogger(Logger logger) {
    masterLogger = logger;
  }

  /**
   * This method is called whenever any PickingRequest is changed. Such as when
   * it is ready for loading and sequencing.
   *
   * @param request the observable object.
   * @param arg     an argument passed to the notifyObservers
   */
  @Override
  public void update(Observable request, Object arg) {
    if (!(arg instanceof Location) | !(request instanceof PickingRequest)) {
      throw new UnsupportedOperationException();
    }
    PickingRequest castedRequest = (PickingRequest) request;
    if (arg == Location.pick) {
      outStandingPickingRequests.add(castedRequest);
    } else if (arg == Location.marshall) {
      marshallingArea.add(castedRequest);
    } else {
      loadingArea.add(castedRequest);
      loadingArea.sort(PickingRequest::compareTo);
    }
  }

  /**
   * This method generate a new picking request.
   * This method should never be called if there's less than 4 orders in
   * outstanding Orders.
   *
   * @return A picking request from 4 orders.
   */
  private PickingRequest generatePickingReq() {
    ArrayList<Order> toBeSent = new ArrayList<>();
    for (int i = 0; i < 4; i++) {
      toBeSent.add(orders.pop());
    }
    return new PickingRequest(toBeSent, pickingReqId++, this);
  }

  /**
   * Pop off a picking request from one of the areas based on the requested
   * area.
   *
   * @param location the requested location.
   */
  public PickingRequest popRequest(Location location) {
    if (location == Location.pick && !outStandingPickingRequests
        .isEmpty()) {
      return outStandingPickingRequests.pop();
    } else if (location == Location.marshall && !marshallingArea.isEmpty()) {
      return marshallingArea.pop();
    } else if (location == Location.load && !loadingArea.isEmpty()) {
      return loadingArea.pop();
    }
    return null;
  }

  /**
   * Get a picking request for the picker. Requests are sent in the order they
   * are made.
   *
   * @return a picking request for the picker.
   */
  public PickingRequest getUnpickedRequest() {
    if (!outStandingPickingRequests.isEmpty()) {
      return popRequest(Location.pick);
    } else if (orders.size()
        >= 4) { //Create one from earliest four orders if there aren't any.
      return generatePickingReq();
    } else {
      return null;
    }
  }

  /**
   * Method for adding an order to the system.
   *
   * @param order the order to be added
   */
  public void addOrder(Order order) {
    orders.add(order);
    if (masterLogger != null) {
      masterLogger.log(Level.INFO, "New Order:" + order);
    }
  }

  /**
   * Add front and back pallets based on their picking request id.
   *
   * @param pallet the front and back pallets.
   * @param id     the picking request id.
   */
  public void addPallets(String[][] pallet, int id) {
    this.pallets.put(id, pallet);
  }

  /**
   * Remove and return pallets by given picking request id.
   *
   * @param id the picking request id.
   */
  public String[][] popPallets(int id) {
    if (pallets.containsKey(id)) {
      String[][] result = pallets.get(id);
      pallets.remove(id);
      return result;
    } else {
      return null;
    }
  }
}
