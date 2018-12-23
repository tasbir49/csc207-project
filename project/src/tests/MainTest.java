package tests;

import org.junit.Assert;
import org.junit.Test;
import util.Main;

/**
 * Unit test for Main class.
 */
public class MainTest {

  /**
   * Test for when the main method is successful.
   */
  @Test
  public void mainSuccess() {
    TestFactory.supressPrint();
    try {
      String[] args = new String[]{"../events.txt"};
      Main.main(args);
    } catch (UnsupportedOperationException ex) {
      Assert.fail();
    }
  }

  /**
   * Test for when the main method fails.
   */
  @Test(expected = UnsupportedOperationException.class)
  public void mainFail() {
    String[] args = new String[]{"sadasdsa"};
    Main.main(args);
  }
}