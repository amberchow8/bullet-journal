package cs3500.pa05.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import cs3500.pa05.model.json.WeekJson;
import java.nio.file.Path;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests FileReader
 */
public class FileReaderTest {
  private FileReader fileReader;
  private FileReader badFileReader;

  /**
   * Sets up file reader
   */
  @BeforeEach
  public void setUp() {
    badFileReader = new FileReader(Path.of("src/resources/bad"));
    fileReader = new FileReader(Path.of("src/test/resources/weekTest.bujo"));
  }

  /**
   * Tests readBujo()
   */
  @Test
  public void testReadBujo() {
    assertThrows(IllegalArgumentException.class, () -> badFileReader.readBujo());
    WeekJson weekJson = fileReader.readBujo();
    assertEquals("Week Test", weekJson.name());
    assertEquals("task2", weekJson.days().get(0).tasks().get(0).name());
    assertEquals("Sunday", weekJson.days().get(0).tasks().get(0).day());
    assertEquals("false", weekJson.days().get(0).tasks().get(0).completed());
    assertEquals(7, weekJson.days().size());
    assertEquals(0, weekJson.days().get(0).events().size());
    assertEquals("12:00", weekJson.days().get(5).events().get(0).startTime());
  }
}
