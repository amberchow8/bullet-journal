package cs3500.pa05.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import cs3500.pa05.model.json.EventJson;
import cs3500.pa05.model.json.TaskJson;
import cs3500.pa05.model.json.WeekJson;

/**
 * Utils class for converting between JSON and records
 */
public class JsonUtils {
  /**
   * Converts a given record object to a JsonNode.
   *
   * @param record the record to convert
   * @return the JsonNode representation of the given record
   * @throws IllegalArgumentException if the record could not be converted correctly
   */
  public static JsonNode serializeRecord(Record record) throws IllegalArgumentException {
    try {
      ObjectMapper mapper = new ObjectMapper();
      return mapper.convertValue(record, JsonNode.class);
    } catch (IllegalArgumentException e) {
      throw new IllegalArgumentException("Given record cannot be serialized");
    }
  }

  /**
   * Deserializes the given Json string
   *
   * @param str Json string
   * @return WeekJson of the given data
   */
  public static WeekJson deserializeJsonString(String str) {
    try {
      ObjectMapper mapper = new ObjectMapper();
      JsonNode node = mapper.readValue(str, JsonNode.class);
      return mapper.convertValue(node, WeekJson.class);
    } catch (IllegalArgumentException | JsonProcessingException e) {
      throw new IllegalArgumentException("Given string cannot be deserialized");
    }
  }

  /**
   * Converts the given Event to an EventJson
   *
   * @param event event
   * @return event json
   */
  public static EventJson convertEvent(Event event) {
    EventJson eventJson = new EventJson(
        event.nameProperty(), event.descriptionProperty(),
        event.dayOfWeekProperty().getDay(), event.startTimeProperty(),
        event.durationProperty());
    return eventJson;
  }

  /**
   * Converts the given EventJson to an Event
   *
   * @param eventJson event json
   * @return event
   */
  public static Event convertEventJson(EventJson eventJson) {
    Event event = new Event(
        eventJson.name(), eventJson.description(),
        eventJson.startTime(),
        eventJson.duration(), DayType.valueOf(eventJson.day().toUpperCase()));
    System.out.println(event.eventToString());
    return event;
  }

  /**
   * Converts the given Task to an TaskJson
   *
   * @param task task
   * @return task json
   */
  public static TaskJson convertTask(Task task) {
    TaskJson taskJson = new TaskJson(
        task.nameProperty(), task.descriptionProperty(),
        task.dayOfWeekProperty().getDay(), Boolean.toString(task.completeProperty()));
    return taskJson;
  }

  /**
   * Converts the given TaskJson to a Task
   *
   * @param taskJson task json
   * @return task
   */
  public static Task convertTaskJson(TaskJson taskJson) {
    Task task = new Task(
        taskJson.name(), taskJson.description(),
        DayType.valueOf(taskJson.day().toUpperCase()),
        Boolean.parseBoolean(taskJson.completed()));
    return task;
  }
}
