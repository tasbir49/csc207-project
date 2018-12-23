package tests;

import fascia.Order;
import fascia.PickingRequest;
import fascia.PickingRequestManager;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import util.MasterSystem;
import warehousefloor.Truck;
import worker.Loader;
import worker.Picker;
import worker.Replenisher;
import worker.Sequencer;
import worker.Worker;
import worker.WorkerManager;

/**
 * Unit test for WorkerManager class.
 */
public class WorkerManagerTest {

  private static Field pickers;
  private static Field loaders;
  private static Field sequencers;
  private static Field replenishers;
  private static Field[] pickingRequestManagerFields;
  private static HashMap<String, Field> workerFields;
  private TestFactory factory;
  private MasterSystem masterSystem;
  private WorkerManager workerManager;
  private HashMap<String, Picker> pickersMap;
  private HashMap<String, Loader> loadersMap;
  private HashMap<String, Sequencer> sequencersMap;
  private HashMap<String, Replenisher> replenishersMap;
  private HashMap<String, Object> pickingRequestManagerVars;
  private Picker picker;
  private Sequencer sequencer;
  private Loader loader;
  private LinkedList<PickingRequest> outStandingPickingRequests;
  private LinkedList<PickingRequest> marshallingArea;
  private LinkedList<PickingRequest> loadingArea;
  private PickingRequest pickingRequest;

  /**
   * Setup the static fields once before all tests.
   */
  @BeforeClass
  public static void setUpBeforeClass() throws NoSuchFieldException {
    workerFields = new HashMap<>();
    pickers = WorkerManager.class.getDeclaredField("pickers");
    loaders = WorkerManager.class.getDeclaredField("loaders");
    sequencers = WorkerManager.class.getDeclaredField("sequencers");
    replenishers = WorkerManager.class.getDeclaredField("replenishers");
    pickingRequestManagerFields = PickingRequestManager.class
        .getDeclaredFields();
    for (Field field : pickingRequestManagerFields) {
      field.setAccessible(true);
    }
    for (Field field : Worker.class.getDeclaredFields()) {
      field.setAccessible(true);
      workerFields.put(field.getName(), field);
    }
    pickers.setAccessible(true);
    loaders.setAccessible(true);
    sequencers.setAccessible(true);
    replenishers.setAccessible(true);
    TestFactory.supressPrint();
  }

  /**
   * Set up the non-static variables for each test.
   */
  @Before
  public void setUp() throws IllegalAccessException {
    factory = new TestFactory();
    masterSystem = factory.getTestEnviroment();
    workerManager = masterSystem.getWorkerManager();
    pickersMap = (HashMap<String, Picker>) pickers.get(workerManager);
    loadersMap = (HashMap<String, Loader>) loaders.get(workerManager);
    sequencersMap = (HashMap<String, Sequencer>) sequencers.get(workerManager);
    replenishersMap = (HashMap<String, Replenisher>)
        replenishers.get(workerManager);
    pickingRequestManagerVars = new HashMap<>();
    for (Field field : pickingRequestManagerFields) {
      pickingRequestManagerVars.put(field.getName(), field.get(masterSystem
          .getPickingRequestManager()));
    }
    picker = new Picker("Baka", masterSystem);
    sequencer = new Sequencer("Aho", masterSystem);
    loader = new Loader("idk", masterSystem);
    outStandingPickingRequests = getRequestsList("outStandingPickingRequests");
    marshallingArea = getRequestsList("marshallingArea");
    loadingArea = getRequestsList("loadingArea");
    pickingRequest = factory
        .pickingRequest(0, masterSystem.getPickingRequestManager());
  }

  /**
   * Test for the update method when the Observable is a Picker and the
   * argument is true. And there are available picking requests in the system.
   */
  @Test
  public void updateReadyPickerNotNull()
      throws IllegalAccessException, NoSuchFieldException {
    addTenOrders();
    picker.tryReady();
    Assert.assertEquals(6, getOrdersFromPickManage().size());
    Assert.assertNotNull(currPicReq(picker));
    Assert.assertEquals(8, getPickerLocations(picker).size());
    readyNotNullHelper(picker, null);
  }

  /**
   * Test for the update method when the Observable is a Picker and the
   * argument is true. And there are no  available picking requests in the
   * system.
   */
  @Test
  public void updateReadyPickerNull()
      throws IllegalAccessException, NoSuchFieldException {
    picker.tryReady();
    workerDefaultState(picker);
    Assert.assertNull(getPickerLocations(picker));
  }

  /**
   * Test for the update method when the Observable is a Sequencer and the
   * argument is true. And there are available picking requests in the system.
   */
  @Test
  public void updateReadySequencerNotNull() throws IllegalAccessException {
    marshallingArea.add(pickingRequest);
    sequencer.tryReady();
    readyNotNullHelper(sequencer, pickingRequest);
    Assert.assertEquals(0, marshallingArea.size());
  }

  /**
   * Test for the update method when the Observable is a Sequencer and the
   * argument is true. And there are no  available picking requests in the
   * system.
   */
  @Test
  public void updateReadySequencerNull() throws IllegalAccessException {
    sequencer.tryReady();
    workerDefaultState(sequencer);
  }

  /**
   * Test for the update method when the Observable is a Loader and the
   * argument is true. And there are available picking requests in the system.
   */
  @Test
  public void updateReadyLoaderNotNull()
      throws IllegalAccessException, NoSuchFieldException {
    String[][] pallets = factory.generatePallets(pickingRequest);
    getManagerPallets().put(pickingRequest.getId(), pallets);
    loadingArea.add(pickingRequest);
    loader.tryReady();
    readyNotNullHelper(loader, pickingRequest);
    Assert.assertEquals(0, loadingArea.size());
    Assert.assertArrayEquals(pallets, getLoaderPallets(loader));
  }

  /**
   * Test for the update method when the Observable is a Loader and the
   * argument is true. And there are no  available picking requests in the
   * system.
   */
  @Test
  public void updateReadyLoaderNull()
      throws IllegalAccessException, NoSuchFieldException {
    loader.tryReady();
    workerDefaultState(loader);
    String[][] expected = new String[][]{new String[4], new String[4]};
    Assert.assertArrayEquals(expected, getLoaderPallets(loader));
  }

  /**
   * Test for the update method when the Observable is a Picker and the
   * argument is false. And the Picker has null pickingRequest.
   */
  @Test
  public void updateFinishPickerNull() throws IllegalAccessException {
    finishNullHelper(picker);
  }

  /**
   * Test for the update method when the Observable is a Picker and the
   * argument is false. And the Picker has scanCount != 8.
   */
  @Test
  public void updateFinishPickerFail() throws IllegalAccessException {
    addTenOrders();
    picker.tryReady();
    picker.finish();
    Assert.assertEquals(1, outStandingPickingRequests.size());
    workerDefaultState(picker);
  }

  /**
   * Test for the update method when the Observable is a Picker and the
   * argument is false. And the Picker has scanCount == 8.
   */
  @Test
  public void updateFinishPickerSuccess() throws IllegalAccessException {
    outStandingPickingRequests.add(pickingRequest);
    picker.tryReady();
    workerFields.get("scanCount").set(picker, 8);
    picker.finish();
    Assert.assertTrue(outStandingPickingRequests.size() == 0
        && marshallingArea.size() == 1);
    Assert.assertEquals(pickingRequest, marshallingArea.get(0));
    workerDefaultState(picker);
  }

  /**
   * Test for the update method when the Observable is a Sequencer and the
   * argument is false. And the Sequencer has null pickingRequest.
   */
  @Test
  public void updateFinishSequencerNull() throws IllegalAccessException {
    finishNullHelper(sequencer);
  }

  /**
   * Test for the update method when the Observable is a Sequencer and the
   * argument is false. And the Sequencer has scanCount != 8.
   */
  @Test
  public void updateFinishSequencerFail() throws IllegalAccessException {
    marshallingArea.add(pickingRequest);
    sequencer.tryReady();
    sequencer.finish();
    Assert.assertEquals(1, outStandingPickingRequests.size());
    Assert.assertEquals(outStandingPickingRequests.get(0), pickingRequest);
    Assert.assertEquals(0, marshallingArea.size());
    workerDefaultState(sequencer);
  }

  /**
   * Test for the update method when the Observable is a Sequencer and the
   * argument is false. And the Sequencer has scanCount == 8.
   */
  @Test
  public void updateFinishSequencerSuccess() throws IllegalAccessException {
    marshallingArea.add(pickingRequest);
    sequencer.tryReady();
    workerFields.get("scanCount").set(sequencer, 8);
    sequencer.finish();
    String[][] pallets = factory.generatePallets(pickingRequest);
    Assert.assertArrayEquals(pallets, getManagerPallets().get(0));
    Assert.assertEquals(1, loadingArea.size());
    Assert.assertEquals(0, marshallingArea.size());
    workerDefaultState(sequencer);
  }

  /**
   * Test for the update method when the Observable is a Loader and the
   * argument is false. And the Loader has null pickingRequest.
   */
  @Test
  public void updateFinishLoaderNull() throws IllegalAccessException {
    finishNullHelper(loader);
  }

  /**
   * Test for the update method when the Observable is a Loader and the
   * argument is false. And the Loader has scanCount != 8.
   */
  @Test
  public void updateFinishLoaderFail()
      throws IllegalAccessException, NoSuchFieldException {
    String[][] pallets = factory.generatePallets(pickingRequest);
    loadingArea.add(pickingRequest);
    getManagerPallets().put(0, pallets);
    loader.tryReady();
    loader.finish();
    Assert.assertEquals(0, getManagerPallets().size());
    Assert.assertEquals(0, loadingArea.size());
    Assert.assertEquals(outStandingPickingRequests.get(0), pickingRequest);
    Assert.assertEquals(1, outStandingPickingRequests.size());
    String[][] expected = new String[][]{new String[4], new String[4]};
    Assert.assertArrayEquals(expected, getLoaderPallets(loader));
    workerDefaultState(loader);
  }

  /**
   * Test for the update method when the Observable is a Loader and the
   * argument is false. And the Loader has scanCount == 8, but the loader
   * can't load the pallets onto a truck.
   */
  @Test
  public void updateFinishLoaderTruckFail()
      throws IllegalAccessException, NoSuchFieldException {
    Truck truck = new Truck(1111);
    masterSystem.getWarehouseFloor().addTruck(truck);
    String[][] pallets = factory.generatePallets(pickingRequest);
    loadingArea.add(pickingRequest);
    getManagerPallets().put(0, pallets);
    loader.tryReady();
    workerFields.get("scanCount").set(loader, 8);
    loader.finish();
    Assert.assertEquals(1, getManagerPallets().size());
    Assert.assertEquals(1, loadingArea.size());
    Assert.assertEquals(0, outStandingPickingRequests.size());
    String[][] expected = new String[][]{new String[4], new String[4]};
    Assert.assertArrayEquals(expected, getLoaderPallets(loader));
    workerDefaultState(loader);
  }


  /**
   * Test for the update method when the Observable is a Loader and the
   * argument is false. And the Loader has scanCount == 8, and the loader
   * loaded the pallets onto a truck.
   */
  @Test
  public void updateFinishLoaderSuccess()
      throws IllegalAccessException, NoSuchFieldException {
    Truck truck = new Truck(0);
    masterSystem.getWarehouseFloor().addTruck(truck);
    String[][] pallets = factory.generatePallets(pickingRequest);
    loadingArea.add(pickingRequest);
    getManagerPallets().put(0, pallets);
    loader.tryReady();
    workerFields.get("scanCount").set(loader, 8);
    loader.finish();
    Field cargo = Truck.class.getDeclaredField("cargo");
    cargo.setAccessible(true);
    final ArrayList<ArrayList<String[]>> cargoList = (ArrayList<ArrayList<String[]>>)
        cargo.get(truck);
    Assert.assertEquals(0, getManagerPallets().size());
    Assert.assertEquals(0, loadingArea.size());
    Assert.assertEquals(0, outStandingPickingRequests.size());
    String[][] expected = new String[][]{new String[4], new String[4]};
    Assert.assertArrayEquals(expected, getLoaderPallets(loader));
    workerDefaultState(loader);
    Assert.assertArrayEquals(cargoList.get(0).get(0), pallets[0]);
    Assert.assertArrayEquals(cargoList.get(0).get(1), pallets[1]);
  }

  /**
   * Test for update method when it fails.
   */
  @Test(expected = UnsupportedOperationException.class)
  public void updateFail() {
    workerManager.update(null, "");
  }

  /*
  YAY TESTING BOILERPLATE THANKS JAVA
   */

  /**
   * Test for the addLoader method.
   */
  @Test
  public void addLoader() {
    workerManager.addLoader(loader);
    Assert.assertEquals(loader, loadersMap.get(loader.getName()));
  }

  /**
   * Test for the addSequencer method.
   */
  @Test
  public void addSequencer() {
    workerManager.addSequencer(sequencer);
    Assert.assertEquals(sequencer, sequencersMap.get(sequencer.getName()));
  }

  /**
   * Test for the addPicker method.
   */
  @Test
  public void addPicker() {
    workerManager.addPicker(picker);
    Assert.assertEquals(picker, pickersMap.get(picker.getName()));
  }

  /**
   * Test for the addReplenisher method.
   */
  @Test
  public void addReplenisher() {
    Replenisher replenisher = new Replenisher("", masterSystem);
    workerManager.addReplenisher(replenisher);
    Assert
        .assertEquals(replenisher, replenishersMap.get(replenisher.getName()));
  }

  /**
   * Test for the getLoader method.
   */
  @Test
  public void getLoader() {
    loadersMap.put(loader.getName(), loader);
    Assert.assertEquals(loader, workerManager.getLoader(loader.getName()));
    Assert.assertNull(workerManager.getLoader("asdasdsadasdasd"));
  }

  /**
   * Test for the getPicker method.
   */
  @Test
  public void getPicker() {
    pickersMap.put(picker.getName(), picker);
    Assert.assertEquals(picker, workerManager.getPicker(picker.getName()));
    Assert.assertNull(workerManager.getPicker("asdas"));
  }

  /**
   * Test for the getSequencer method.
   */
  @Test
  public void getSequencer() {
    sequencersMap.put(sequencer.getName(), sequencer);
    Assert.assertEquals(sequencer,
        workerManager.getSequencer(sequencer.getName()));
    Assert.assertNull(workerManager.getSequencer("aaaa"));
  }

  /**
   * Test for the getReplenisher method.
   */
  @Test
  public void getReplenisher() {
    Replenisher replenisher = new Replenisher("sssssasd", masterSystem);
    replenishersMap.put(replenisher.getName(), replenisher);
    Assert.assertEquals(replenisher, workerManager.getReplenisher(replenisher
        .getName()));
    Assert.assertNull(workerManager.getReplenisher("miku"));
  }

  /**
   * A helper method to get all of Worker class's instance variables.
   *
   * @param worker    the worker object
   * @param fieldName the name of the field for the variable
   */
  private Object getWorkerVar(Worker worker, String fieldName)
      throws IllegalAccessException {
    return workerFields.get(fieldName).get(worker);
  }

  /**
   * A helper method for getting Picker's location variable.
   *
   * @param picker the picker object to get the var from
   */
  private ArrayList<String> getPickerLocations(Picker picker)
      throws NoSuchFieldException, IllegalAccessException {
    Field field = Picker.class.getDeclaredField("locations");
    field.setAccessible(true);
    return (ArrayList<String>) field.get(picker);
  }

  /**
   * A helper method to get the orders var from PickingRequest manager class.
   */
  private LinkedList<Order> getOrdersFromPickManage() {
    return (LinkedList<Order>) pickingRequestManagerVars.get("orders");
  }

  /**
   * A helper method to get toBeScanned var from a Worker instance.
   *
   * @param worker the Worker instance
   * @return the toBeScanned linked list
   */
  private LinkedList<String> getToBeScanned(Worker worker)
      throws IllegalAccessException {
    return (LinkedList<String>) getWorkerVar(worker, "toBeScanned");
  }

  /**
   * Get the currPickingReq var from an instance of Worker.
   *
   * @param worker the worker instance
   * @return the currPickingRequest
   */
  private PickingRequest currPicReq(Worker worker)
      throws IllegalAccessException {
    return (PickingRequest) workerFields.get("currPickingReq").get(worker);
  }

  /**
   * Return one of the 3 LinkedList PickingRequest from the
   * PickingRequestManager class.
   *
   * @param name the name of the field
   */
  private LinkedList<PickingRequest> getRequestsList(String name) {
    return (LinkedList<PickingRequest>) pickingRequestManagerVars.get(name);
  }

  /**
   * A helper method to check for a worker where it is at the it's default
   * state.
   *
   * @param worker the worker instance
   */
  private void workerDefaultState(Worker worker) throws IllegalAccessException {
    Assert.assertNull(currPicReq(worker));
    Assert.assertEquals(0, getWorkerVar(worker, "scanCount"));
    Assert.assertEquals(0, getToBeScanned(worker).size());
  }

  /**
   * A helper to test when a worker readies with availble picking requests.
   *
   * @param worker         the worker instance
   * @param pickingRequest the pickingRequest instance
   */
  private void readyNotNullHelper(Worker worker, PickingRequest pickingRequest)
      throws IllegalAccessException {
    if (pickingRequest != null) {
      Assert.assertEquals(pickingRequest, currPicReq(worker));
    }
    Assert.assertEquals(0, getWorkerVar(worker, "scanCount"));
    Assert.assertEquals(8, getToBeScanned(worker).size());
  }

  /**
   * A helper method to get access to the pallets hashmap in
   * PickingRequestManager.
   *
   * @return pallets hashmap
   */
  private HashMap<Integer, String[][]> getManagerPallets() {
    return (HashMap<Integer, String[][]>) pickingRequestManagerVars
        .get("pallets");
  }

  /**
   * A helper method for getting Loader's front and back pallets.
   *
   * @param loader the loader
   * @return front and back pallets
   */
  private String[][] getLoaderPallets(Loader loader)
      throws NoSuchFieldException, IllegalAccessException {
    Field frontPalletField = Loader.class.getDeclaredField("frontPallet");
    Field backPalletField = Loader.class.getDeclaredField("backPallet");
    frontPalletField.setAccessible(true);
    backPalletField.setAccessible(true);
    String[] frontPallet = (String[]) frontPalletField.get(loader);
    String[] backPallet = (String[]) backPalletField.get(loader);
    return new String[][]{frontPallet, backPallet};
  }

  /**
   * Returns an array of 3 deep copies of the PickingRequestManager
   * PickingRequest LinkedLists.
   */
  private LinkedList[] deepCopyLists() {
    LinkedList<PickingRequest> outStandingPickingRequests = new LinkedList<>();
    LinkedList<PickingRequest> marshallingArea = new LinkedList<>();
    LinkedList<PickingRequest> loadingArea = new LinkedList<>();
    outStandingPickingRequests
        .addAll(getRequestsList("outStandingPickingRequests"));
    marshallingArea.addAll(getRequestsList("marshallingArea"));
    loadingArea.addAll(getRequestsList("loadingArea"));
    return new LinkedList[]{outStandingPickingRequests, marshallingArea,
        loadingArea};
  }

  /**
   * A helper method to add 10 random orders to PickingRequestManager.
   */
  private void addTenOrders() {
    for (Order order : factory.randomOrders(10)) {
      masterSystem.getPickingRequestManager().addOrder(order);
    }
  }

  /**
   * A helper method to check for Worker doing finish with null
   * currPickingRequest.
   *
   * @param worker the worker to check for
   */
  private void finishNullHelper(Worker worker) throws IllegalAccessException {
    LinkedList[] init = deepCopyLists();
    worker.finish();
    LinkedList[] fin = deepCopyLists();
    workerDefaultState(worker);
    Assert.assertArrayEquals(init, fin);
  }
}