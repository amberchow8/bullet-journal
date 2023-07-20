package cs3500.pa05.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.fasterxml.jackson.databind.node.ObjectNode;
import cs3500.pa05.model.json.DayJson;
import cs3500.pa05.model.json.EventJson;
import cs3500.pa05.model.json.TaskJson;
import cs3500.pa05.model.json.ThemeJson;
import cs3500.pa05.model.json.WeekJson;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

/**
 * Tests JsonUtils class
 */
public class JsonUtilsTest {
  /**
   * Tests serializeRecord()
   */
  @Test
  public void testSerializeRecord() {
    WeekJson weekJson = new WeekJson("hello", 5, 5, new ArrayList<>(),
        "NEON", new ArrayList<>(
            List.of(new DayJson(DayType.SUNDAY, new ArrayList<>(), new ArrayList<>()))),
        new ArrayList<>(
            List.of(new TaskJson(
                "name", "desc", "Monday", "true"))), "note");
    assertEquals(ObjectNode.class, JsonUtils.serializeRecord(weekJson).getClass());
  }

  /**
   * Tests deserializeJsonString()
   */
  @Test
  public void testDeserializeJsonString() {
    WeekJson weekJson = new WeekJson("Hello", 5, 5, new ArrayList<>(),
        "NEON", new ArrayList<>(
        List.of(new DayJson(DayType.SUNDAY, new ArrayList<>(), new ArrayList<>()))),
        new ArrayList<>(
            List.of(new TaskJson(
                "Task 1", "", "Monday", "true"))), "note");
    String str = "{\n" +
            "  \"name\" : \"Hello\",\n" +
            "  \"task-max\" : 5,\n" +
            "  \"event-max\" : 5,\n" +
            "  \"theme-options\" : [  ],\n" +
            "  \"current-theme\" : \"NEON\",\n" +
            "  \"days\" : [ {\n" +
            "    \"day\" : \"SUNDAY\",\n" +
            "    \"events\" : [ ],\n" +
            "    \"tasks\" : [ ]\n" +
            "  } ],\n" +
            "  \"task-queue\" : [ {\n" +
            "    \"name\" : \"Task 1\",\n" +
            "    \"description\" : \"\",\n" +
            "    \"day\" : \"Monday\",\n" +
            "    \"completed\" : \"true\"\n" +
            "  } ],\n" +
            "  \"note\" : \"note\"\n" +
            "}";
    assertEquals(weekJson, JsonUtils.deserializeJsonString(str));
  }

  /**
   * Test convertEvent()
   */
  @Test
  public void testConvertEvent() {
    Event event = new Event("n", "d",
        "11:00", "1", DayType.FRIDAY);
    EventJson eventJson = new EventJson("n", "d",
        "Friday", "11:00", "1");
    assertEquals(eventJson, JsonUtils.convertEvent(event));
  }

  /**
   * Test convertEventJson()
   */
  @Test
  public void testConvertEventJson() {
    Event event = new Event("n", "d",
        "11:00", "1", DayType.FRIDAY);
    EventJson eventJson = new EventJson("n", "d",
        "Friday", "11:00", "1");
    assertEquals(event.eventToString(),
        JsonUtils.convertEventJson(eventJson).eventToString());

  }

  /**
   * Test convertTask()
   */
  @Test
  public void testConvertTask() {
    Task task = new Task("n", "d",
        DayType.MONDAY, true);
    TaskJson taskJson = new TaskJson("n", "d",
        "Monday", "true");
    assertEquals(taskJson, JsonUtils.convertTask(task));
  }

  /**
   * Test convertTaskJson()
   */
  @Test
  public void testConvertTaskJson() {
    Task task = new Task("n", "d",
        DayType.MONDAY, true);
    TaskJson taskJson = new TaskJson("n", "d",
        "Monday", "true");
    assertEquals(task.taskToString(),
        JsonUtils.convertTaskJson(taskJson).taskToString());
  }
}
