package cs3500.pa05.model.json;

import static org.junit.jupiter.api.Assertions.assertEquals;

import cs3500.pa05.model.DayType;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests methods within a WeekJson.
 */
class WeekJsonTest {
  private List<DayJson> days;
  private List<TaskJson> tasks;
  private WeekJson week;

  /**
   * Sets up fields for testing.
   */
  @BeforeEach
  public void setUp() {
    List<EventJson> events = new ArrayList<>();
    EventJson eventJson1 = new EventJson("Meeting", "Team meeting",
        "Saturday", "10:00", "1 hour");
    EventJson eventJson2 = new EventJson("Meeting", "Team meeting",
        "Saturday", "10:00", "1 hour");
    EventJson eventJson3 = new EventJson("Presentation", "Project presentation",
        "Friday", "11:00", "2 hours");
    events.add(eventJson1);
    events.add(eventJson2);
    events.add(eventJson3);

    List<TaskJson> tasks2 = new ArrayList<>();
    TaskJson taskJson1 = new TaskJson("Meeting", "Team meeting",
        "Saturday", "true");
    TaskJson taskJson2 = new TaskJson("Meeting", "Team meeting",
        "Saturday", "true");
    TaskJson taskJson3 = new TaskJson("Presentation", "Project presentation",
        "Friday", "false");
    tasks2.add(taskJson1);
    tasks2.add(taskJson2);
    tasks2.add(taskJson3);
    DayJson day2 = new DayJson(DayType.FRIDAY, events, tasks2);
    tasks = day2.tasks();
    days = new ArrayList<>();
    days.add(day2);
    DayJson day1 = new DayJson(DayType.SATURDAY, events, tasks2);
    days.add(day1);
    week = new WeekJson("test", 5, 5, new ArrayList<>(),
        new ThemeJson("neon"), days, tasks, "testing");
  }

  /**
   * Tests the getter method.
   */
  @Test
  public void name() {
    assertEquals("test", week.name());
  }
}