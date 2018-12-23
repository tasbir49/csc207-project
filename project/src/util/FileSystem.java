package util;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A class that keeps hold of all data that needs to be read and written.
 *
 * @author Peijun
 */
public class FileSystem {

  //Maps path to content. Each element in content is a line.
  private HashMap<String, ArrayList<String>> readingFiles = new HashMap<>();
  private HashMap<String, ArrayList<String>> writingFiles = new HashMap<>();

  /**
   * Initialize a FileSystem object with some file paths.
   *
   * @param readPaths  All the file this class needs to read from.
   * @param writePaths All the files this class needs to write to.
   */
  public FileSystem(String[] readPaths, String[] writePaths) {
    if (readPaths != null) {
      for (String s : readPaths) {
        try {
          readingFiles.put(s, CsvReadWrite.readCsv(s));
        } catch (NullPointerException npe) {
          System.out.println("File path " + s + " is not found, skipping.");
        }
      }
    }
    if (writePaths != null) {
      for (String s : writePaths) {
        writingFiles.put(s, new ArrayList<>());
      }
    }
  }

  /**
   * Writes all the contents in writingFiles onto the file system.
   */
  public void writeAll() {
    for (String s : writingFiles.keySet()) {
      CsvReadWrite.overWrite(writingFiles.get(s), s);
    }
  }

  /**
   * Get the file content by path from readingFiles.
   *
   * @param path the path for the file.
   * @return the content of the file.
   */
  public ArrayList<String> getFileContent(String path) {
    return readingFiles.get(path);
  }

  /**
   * Get a file in the file system for edit.
   *
   * @param path the path to the file.
   * @return the content of that file.
   */
  public ArrayList<String> getWritingFileForEdit(String path) {
    return writingFiles.get(path);
  }

  /**
   * Set the content of a file to be written with path path.
   *
   * @param path    the path of the file.
   * @param content the content to set it to.
   */
  public void setWritingFileContents(String path, ArrayList<String> content) {
    writingFiles.put(path, content);
  }
}
