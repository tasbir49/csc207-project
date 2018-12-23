package util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This class is responsible for translating SKUs and returning information
 * for a particular set of that data, or about a SKU.
 *
 * @author Tasbir
 */

public class SkuTranslator {

  private ArrayList<String> locations;
  private ArrayList<String> properties;

  public SkuTranslator(ArrayList<String> locations,
      ArrayList<String> properties) {
    this.locations = locations;
    this.properties = properties;
  }

  /**
   * Using the given translation table, returns the front or back
   * SKU given the colour, model, and whether it is a front or back bumper.
   *
   * @param colour  the colour of this sku
   * @param model   the model of this sku
   * @param isFront true if the sku is a front fascia, false if its a rear fascia
   * @return : the sku that matches the colour, model, and orientation
   */
  public String getSku(String colour, String model, boolean isFront) {
    for (String s : properties) {
      String[] line = s.split(",");
      if (colour.equals(line[0]) && model.equals(line[1])) {
        return isFront ? line[2] : line[3];
      }
    }
    return null;
  }

  /**
   * Using the given traversal table, returns the location of <sku></sku> in
   * warehousefloor.
   *
   * @param sku the sku that is being looked up for its location
   * @return : the <sku></sku> with the given location as a String
   */
  public String getLocation(String sku) {
    for (String s : locations) {
      String[] line = s.split(",");
      if (sku.equals(line[4])) {
        List<String> result = Arrays.asList(line);
        return String.join(",", result.subList(0, 4));
      }
    }
    return null;
  }

  /**
   * Using the given traversal table, fetches the sku stored at a location.
   *
   * @param location is in String array format such as {Zone, Aisle, Rack, Rack Level}
   * @return sku the sku stored in given area according to the translation table
   */
  public String getSkuFromLocation(String[] location) {
    for (String s : locations) {
      List<String> line = Arrays.asList(s.split(","));
      String[] locationArray = line.subList(0, 4).toArray(new String[4]);
      if (Arrays.equals(location, locationArray)) {
        return line.get(4);
      }
    }
    return null;
  }

  /**
   * Get a list of all skus from the translation table.
   *
   * @return a list of all skus from the translation table.
   */
  public ArrayList<String> getAllSku() {
    ArrayList<String> result = new ArrayList<>();
    for (String s : locations) {
      List<String> line = Arrays.asList(s.split(","));
      result.add(line.get(4));
    }
    return result;
  }
}
