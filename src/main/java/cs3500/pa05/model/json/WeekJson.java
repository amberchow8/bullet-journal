package cs3500.pa05.model.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * Represents a Week in JSON
 *
 * @param name name
 * @param days days of the week
 * @param taskMax the maximum number of tasks
 * @param eventMax the maximum number of events
 * @param allThemes all the stored themes the user can choose from
 * @param chosenTheme the theme currently active for this week
 * @param taskQueue list of tasks for the week
 * @param note note
 */
public record WeekJson(
    @JsonProperty("name") String name,
    @JsonProperty("task-max") int taskMax,
    @JsonProperty("event-max") int eventMax,
    @JsonProperty("theme-options") List<ThemeJson> allThemes,
    @JsonProperty("current-theme") ThemeJson chosenTheme,
    @JsonProperty("days") List<DayJson> days,
    @JsonProperty("task-queue") List<TaskJson> taskQueue,
    @JsonProperty("note") String note
) {

}
