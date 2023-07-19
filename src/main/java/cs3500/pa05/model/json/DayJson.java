package cs3500.pa05.model.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import cs3500.pa05.model.DayType;
import java.util.List;

/**
 * Represents a Day in a week
 *
 * @param day weekday
 * @param events events on this day
 * @param tasks tasks on this day
 */
public record DayJson(
    @JsonProperty("day") DayType day,
    @JsonProperty("events") List<EventJson> events,
    @JsonProperty("tasks") List<TaskJson> tasks
) {

}
