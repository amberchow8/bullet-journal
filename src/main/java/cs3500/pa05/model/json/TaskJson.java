package cs3500.pa05.model.json;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents the json form of a task.
 *
 * @param name task name
 * @param description task description
 * @param day the day the task should be completed
 * @param completed whether or not the task is completed
 */
public record TaskJson(
    @JsonProperty("name") String name,
    @JsonProperty("description") String description,
    @JsonProperty("day") String day,
    @JsonProperty("completed") String completed
) {
}
