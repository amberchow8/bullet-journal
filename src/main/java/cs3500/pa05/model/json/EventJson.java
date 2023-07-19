package cs3500.pa05.model.json;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents one bujo event.
 *
 * @param name the name
 * @param description a description
 * @param day the day of week it is planned for
 * @param startTime the time it is planned for
 * @param duration how long it will take
 */
public record EventJson(
    @JsonProperty("name") String name,
    @JsonProperty("description") String description,
    @JsonProperty("day") String day,
    @JsonProperty("start-time") String startTime,
    @JsonProperty("duration") String duration
) {
}
