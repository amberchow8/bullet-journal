package cs3500.pa05.model;

import cs3500.pa05.model.json.WeekJson;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Writes the current data inputted into the week GUI to a .bujo file
 */
public class FileWriter {
  private WeekJson weekJson;
  private Path path;
  private BufferedWriter writer;

  /**
   * Instantiates a FileWriter.
   *
   * @param week the week to write
   * @param path the path to write to
   */
  public FileWriter(WeekJson week, String path) {
    this.weekJson = week;
    this.path = Path.of(path);
  }

  /**
   * Writes this WeekJson to the file path.
   */
  public void writeBujo() {
    try {
      writer = Files.newBufferedWriter(this.path);
      writer.write(JsonUtils.serializeRecord(this.weekJson).toPrettyString());
      writer.flush();
    } catch (IOException e) {
      System.err.println("Path not found");
    }
  }

}
