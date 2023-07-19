package cs3500.pa05.model;

import cs3500.pa05.model.json.WeekJson;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Reads a .bujo file and converts it into a WeekJson
 */
public class FileReader {
  private WeekJson weekJson;
  private Path path;

  /**
   * Instantiates a FileReader
   *
   * @param path path of .bujo file
   */
  public FileReader(Path path) {
    this.path = path;
  }

  /**
   * Reads the .bujo file and creates a WeekJson
   * containing the file's data.
   *
   * @return WeekJson with data for the week GUI
   */
  public WeekJson readBujo() {
    String jsonString = "";
    try {
      jsonString = new String(Files.readAllBytes(this.path));
    } catch (IOException e) {
      System.err.println("Path not found");
    }
    this.weekJson = JsonUtils.deserializeJsonString(jsonString);
    return this.weekJson;
  }
}
