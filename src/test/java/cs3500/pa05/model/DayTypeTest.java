package cs3500.pa05.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * Tests DayType
 */
class DayTypeTest {

  /**
   * Tests getDay()
   */
  @Test
  public void getDay() {
    assertEquals("Monday", DayType.MONDAY.getDay());
    assertEquals("Tuesday", DayType.TUESDAY.getDay());
    assertEquals("Wednesday", DayType.WEDNESDAY.getDay());
    assertEquals("Thursday", DayType.THURSDAY.getDay());
    assertEquals("Friday", DayType.FRIDAY.getDay());
    assertEquals("Saturday", DayType.SATURDAY.getDay());
    assertEquals("Sunday", DayType.SUNDAY.getDay());
  }
}