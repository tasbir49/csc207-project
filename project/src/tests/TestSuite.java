package tests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import util.MasterSystem;
import util.SkuTranslator;
import worker.Worker;


@RunWith(Suite.class)
@Suite.SuiteClasses({CsvReadWriteTest.class, FileSystemTest.class, LoaderTest.class,
    MainTest.class, MasterSystemFactoryTest.class, MasterSystemTest.class, OrderTest.class,
    PickerTest.class, PickingRequestTest.class, PickingRequestManagerTest.class,
    ReplenisherTest.class, SequencerTest.class, SimulatorTest.class, SkuTranslatorTest.class,
    TruckTest.class, WarehouseFloorTest.class, WarehousePickingTest.class, WorkerManagerTest.class,
    WorkerTest.class})
public class TestSuite {
  //nothing
}