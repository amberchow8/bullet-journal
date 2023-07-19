package cs3500.pa05.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests methods within the class Event.
 */
class EventTest {
  private Event testEvent;

  /**
   * Sets up fields for testing.
   */
  @BeforeEach
  public void setUp() {
    testEvent = new Event("tester", "a tester event", "11:00", "1.5", DayType.SUNDAY);
  }

  /**
   * Tests the getter.
   */
  @Test
  public void nameProperty() {
    assertEquals("tester", testEvent.nameProperty());
  }

  /**
   * Tests the getter.
   */
  @Test
  public void descriptionProperty() {
    assertEquals("a tester event", testEvent.descriptionProperty());
  }

  /**
   * Tests the getter.
   */
  @Test
  public void dayOfWeekProperty() {
    assertEquals(DayType.SUNDAY, testEvent.dayOfWeekProperty());
  }

  /**
   * Tests the getter.
   */
  @Test
  public void startTimeProperty() {
    assertEquals("11:00", testEvent.startTimeProperty());
  }

  /**
   * Tests the setter.
   */
  @Test
  public void setStartTime() {
    String output = testEvent.setStartTime("12:00");
    assertEquals("12:00", output);
  }

  /**
   * Tests the getter.
   */
  @Test
  public void durationProperty() {
    assertEquals("1.5", testEvent.durationProperty());
  }

  /**
   * Tests the method eventostring.
   */
  @Test
  public void eventToString() {
    Event event = new Event("n", "",
        "12:00", "5", DayType.TUESDAY);
    assertTrue(event.eventToString().contains("Event: n"));
    assertTrue(event.eventToString().contains("Time: 12:00"));
    assertTrue(event.eventToString().contains("Duration: 5"));
    assertTrue(testEvent.eventToString().contains("Event: tester"));
    assertTrue(testEvent.eventToString().contains("Description: a tester event"));
    assertTrue(testEvent.eventToString().contains("Time: 11:00"));
    assertTrue(testEvent.eventToString().contains("Duration: 1.5"));

    Event event2 = new Event("e", "https://example.com", "12:30", "2",DayType.FRIDAY);

    assertEquals("Event: e\n" +
        "Day: Friday\n" +
        "Time: 12:30\n" +
        "Duration: 2", event2.eventToString());
  }

  /**
   * Tests the method isUrl.
   */
  @Test
  public void testIsUrl() {
    Event event = new Event("n", "",
        "12:00", "5", DayType.TUESDAY);

    // a valid URL
    String validUrl = "https://example.com";
    assertTrue(event.isUrl(validUrl));

    // an invalid URL
    String invalidUrl = "example.com";
    assertFalse(event.isUrl(invalidUrl));

    // an empty string
    String emptyString = "";
    assertFalse(event.isUrl(emptyString));

    // an event with a url description
    Event eventTwo = new Event("n", "https://github.com/",
        "12:00", "5", DayType.TUESDAY);
    assertEquals(eventTwo.eventToString(), "Event: n\n"
        + "Day: Tuesday\nTime: 12:00\nDuration: 5");
  }
}
