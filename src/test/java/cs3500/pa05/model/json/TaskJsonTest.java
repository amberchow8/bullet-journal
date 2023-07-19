package cs3500.pa05.model.json;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests methods for a TaskJson.
 */
class TaskJsonTest {

  private TaskJson taskJson;
  private String name;
  private String description;
  private String day;
  private String completed;

  /**
   * Setup fields for testing.
   */
  @BeforeEach
  public void setUp() {
    name = "Meeting";
    description = "Team meeting";
    day = "Friday";
    completed = "false";

    taskJson = new TaskJson(name, description, day, completed);
  }

  /**
   * Test getter method.
   */
  @Test
  public void name() {
    assertEquals("Meeting", taskJson.name());
  }

  /**
   * Test getter method.
   */
  @Test
  public void description() {
    assertEquals("Team meeting", taskJson.description());
  }

  /**
   * Test getter method.
   */
  @Test
  public void day() {
    assertEquals("Friday", taskJson.day());
  }

  /**
   * Test getter method.
   */
  @Test
  public void completed() {
    assertEquals("false", taskJson.completed());
  }

  /**
   * Test getter method.
   */
  @Test
  public void theSame() {
    TaskJson taskJson1 = new TaskJson("Meeting", "Team meeting", "Saturday", "true");
    TaskJson taskJson2 = new TaskJson("Meeting", "Team meeting", "Saturday", "true");
    TaskJson taskJson3 = new TaskJson("Presentation", "Project presentation", "Friday", "false");

    assertEquals(taskJson1, taskJson2);
    assertNotEquals(taskJson1, taskJson3);
  }
}