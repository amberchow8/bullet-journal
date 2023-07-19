package cs3500.pa05.model.json;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import cs3500.pa05.model.DayType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests methods in the EventJson class.
 */
class EventJsonTest {

  private EventJson eventJson;
  private String name;
  private String description;
  private String day;
  private String startTime;
  private String duration;

  /**
   * Setup fields for testing.
   */
  @BeforeEach
  public void setUp() {
    name = "Meeting";
    description = "Team meeting";
    day = "Friday";
    startTime = "10:00";
    duration = "1 hour";

    eventJson = new EventJson(name, description, day, startTime, duration);
  }

  /**
   * Tests the getter method.
   */
  @Test
  public void name() {
    assertEquals("Meeting", eventJson.name());

  }

  /**
   * Tests the getter method.
   */
  @Test
  public void description() {
    assertEquals("Team meeting", eventJson.description());
  }

  /**
   * Tests the getter method.
   */
  @Test
  public void day() {
    assertEquals(DayType.FRIDAY.getDay(), eventJson.day());
  }

  /**
   * Tests the getter method.
   */
  @Test
  public void startTime() {
    assertEquals("10:00", eventJson.startTime());
  }

  /**
   * Tests the getter method.
   */
  @Test
  public void duration() {
    assertEquals("1 hour", eventJson.duration());
  }

  /**
   * Tests whether two events are equal or not.
   */
  @Test
  public void eventEquals() {
    EventJson eventJson1 = new EventJson("Meeting", "Team meeting",
        "Saturday", "10:00", "1 hour");
    EventJson eventJson2 = new EventJson("Meeting", "Team meeting",
        "Saturday", "10:00", "1 hour");
    EventJson eventJson3 = new EventJson("Presentation", "Project presentation",
        "Friday", "11:00", "2 hours");
    assertEquals(eventJson1, eventJson2);
    assertNotEquals(eventJson1, eventJson3);
  }
}