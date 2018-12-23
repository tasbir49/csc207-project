package tests;

import java.lang.reflect.Field;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import util.MasterSystem;
import worker.Sequencer;

/**
 * Unit test for Sequencer class.
 */
public class SequencerTest {

  private TestFactory testFactory;
  private Field masterSystem;

  /**
   * Sets up the test enviroment before each test.
   *
   * @throws NoSuchFieldException when a field is not found.
   */
  @Before
  public void setUp() throws NoSuchFieldException {
    testFactory = new TestFactory();
    masterSystem = Sequencer.class.getDeclaredField("masterSystem");
    masterSystem.setAccessible(true);
  }

  /**
   * Test the Sequencer constructor.
   *
   * @throws IllegalAccessException when access is denied to a field
   */
  @Test
  public void sequencerInit() throws IllegalAccessException {
    String name = "testSubject";
    MasterSystem system = testFactory.getTestEnviroment();
    Sequencer dude = new Sequencer(name, system);
    Assert.assertTrue(
        name.equals(dude.getName()) && system.equals(masterSystem.get(dude)));
  }
}