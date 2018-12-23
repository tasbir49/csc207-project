package tests;

import java.lang.reflect.Field;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import util.MasterSystem;
import worker.Loader;

/**
 * Unit test for Loader class.
 */
public class LoaderTest {

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
    masterSystem = Loader.class.getDeclaredField("masterSystem");
    masterSystem.setAccessible(true);
  }

  /**
   * Test the Loader constructor.
   *
   * @throws IllegalAccessException when access is denied to a field
   */
  @Test
  public void loaderInit() throws IllegalAccessException {
    String name = "testSubject";
    MasterSystem system = testFactory.getTestEnviroment();
    Loader dude = new Loader(name, system);
    Assert.assertTrue(
        name.equals(dude.getName()) && system.equals(masterSystem.get(dude)));
  }
}