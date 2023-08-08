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
    assertEquals("tester", testEvent.getName());
  }

  /**
   * Tests the getter.
   */
  @Test
  public void descriptionProperty() {
    assertEquals("a tester event", testEvent.getDescription());
  }

  /**
   * Tests the getter.
   */
  @Test
  public void dayOfWeekProperty() {
    assertEquals(DayType.SUNDAY, testEvent.getDayOfWeek());
  }

  /**
   * Tests the getter.
   */
  @Test
  public void startTimeProperty() {
    assertEquals("11:00", testEvent.getStartTime());
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
    assertEquals("1.5", testEvent.getDuration());
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
  }
}
