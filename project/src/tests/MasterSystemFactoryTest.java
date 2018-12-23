package tests;

import java.lang.reflect.Field;
import org.junit.Assert;
import org.junit.Test;
import util.MasterSystem;
import util.MasterSystemFactory;

/**
 * Unit test fot MasterSystemFactory class.
 */
public class MasterSystemFactoryTest {

  /**
   * Test for when getMasterSystem returns a proper MasterSystem.
   */
  @Test
  public void getMasterSystemSuccess() throws IllegalAccessException {
    String warehouseFile = "../initial.csv";
    String translationFile = "../translation.csv";
    String traversalFile = "../traversal_table.csv";
    String outFile = "../";
    MasterSystem masterSystem = MasterSystemFactory
        .getMasterSystem(warehouseFile, translationFile, traversalFile, outFile,
            30, 5, false);
    Field[] fields = MasterSystem.class.getDeclaredFields();
    for (Field field : fields) {
      field.setAccessible(true);
      Assert.assertNotNull(field.get(masterSystem));
    }
  }

  /**
   * Test for when getMasterSystem returns a bad MasterSystem.
   */
  @Test(expected = NullPointerException.class)
  public void getMasterSystemFail() throws IllegalAccessException {
    String warehouseFile = "../sv";
    String translationFile = "../sv";
    String traversalFile = "../tr.csv";
    String outFile = "../";
    MasterSystemFactory
        .getMasterSystem(warehouseFile, translationFile, traversalFile, outFile,
            30, 5, false);
  }

}