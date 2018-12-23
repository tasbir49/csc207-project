package tests;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import util.CsvReadWrite;
import util.FileSystem;

/**
 * Tests for the FileSystem class.
 *
 * @author Peijun
 */
public class FileSystemTest {

  private FileSystem fileSystem;
  private String[] readPaths = new String[]{"tests/file1.txt",
      "tests/file2.csv", "lol"};
  private String[] writePaths = new String[]{"tests/fileout.csv"};
  private ArrayList<String> content = new ArrayList<>(Arrays.asList(new
      String[]{"line1", "line2", "line3"}));

  /**
   * Make a new instance of FileSystem before each test case.
   */
  @Before
  public void setUp() {
    for (String s : readPaths) {
      if (!s.equals("lol")) {
        CsvReadWrite.overWrite(content, s);
      }
    }
    fileSystem = new FileSystem(readPaths, writePaths);
  }

  /**
   * Delete the test files made by the tests.
   */
  @After
  public void tearDown() {
    for (String s : readPaths) {
      if (!s.equals("lol")) {
        File file = new File(s);
        file.delete();
      }
    }
  }

  /**
   * Test get file content.
   */
  @Test
  public void getFileContent() {
    ArrayList<String> actual0 = fileSystem.getFileContent(readPaths[0]);
    ArrayList<String> actual1 = fileSystem.getFileContent(readPaths[1]);
    Assert.assertTrue(actual0.equals(content) && actual1.equals(content));
  }

  /**
   * Test get file content when the path isnt valid.
   */
  @Test
  public void getFileContentFail() {
    Assert.assertNull(fileSystem.getFileContent("lol"));
  }

  /**
   * Test getWritingFileForEdit.
   */
  @Test
  public void getWritingFileForEdit() {
    fileSystem.setWritingFileContents(writePaths[0], content);
    ArrayList<String> actual = fileSystem.getWritingFileForEdit(writePaths[0]);
    Assert.assertEquals(content, actual);
  }

  /**
   * Test setWritingFileContents.
   */
  @Test
  public void setWritingFile() {
    fileSystem.setWritingFileContents("DansGame", new ArrayList<>(
        Arrays.asList(new String[]{"lololol", "lasdlasld", "asdsad"})));
    fileSystem.setWritingFileContents("DansGame", content);
    ArrayList<String> actual = fileSystem.getWritingFileForEdit("DansGame");
    Assert.assertEquals(content, actual);
  }

  /**
   * Test for writeAll.
   */
  @Test
  public void writeAll() {
    fileSystem.setWritingFileContents(writePaths[0], content);
    fileSystem.writeAll();
    ArrayList<String> actual = CsvReadWrite.readCsv(writePaths[0]);
    File file = new File(writePaths[0]);
    file.delete();
    Assert.assertEquals(content, actual);
  }

  /**
   * Test editing a writing file.
   */
  @Test
  public void testEdit() {
    fileSystem.getWritingFileForEdit(writePaths[0]).add("bleh");
    ArrayList<String> expected = new ArrayList<>(Arrays.asList(new
        String[]{"bleh"}));
    Assert.assertEquals(expected,
        fileSystem.getWritingFileForEdit(writePaths[0]));
  }
}