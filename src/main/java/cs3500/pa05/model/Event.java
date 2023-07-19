package cs3500.pa05.model;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents an Event
 */
public class Event {
  private String name;
  private String description;
  private DayType dayOfWeek;
  private String startTime;
  private String duration;

  /**
   * Instantiates an Event
   *
   * @param name        name
   * @param description description
   * @param startTime   start time (HH:MM)
   * @param duration    duration (in hours)
   * @param dayOfWeek   day of week
   */
  public Event(String name, String description, String startTime, String duration,
               DayType dayOfWeek) {
    this.name = name;
    this.description = description;
    this.startTime = startTime;
    this.duration = duration;
    this.dayOfWeek = dayOfWeek;
  }

  /**
   * Gets name.
   *
   * @return name
   */
  public String nameProperty() {
    return name;
  }

  /**
   * Gets description.
   *
   * @return description
   */
  public String descriptionProperty() {
    return description;
  }

  /**
   * Gets day of week.
   *
   * @return day
   */
  public DayType dayOfWeekProperty() {
    return dayOfWeek;
  }

  /**
   * Gets start time.
   *
   * @return start time
   */
  public String startTimeProperty() {
    return startTime;
  }

  /**
   * Ensures that the given string is in format HH:MM.
   *
   * @param startTime string time
   * @return formatted string
   */
  public String setStartTime(String startTime) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
    LocalTime time = LocalTime.parse(startTime, formatter);
    return time.toString();
  }

  /**
   * Gets duration.
   *
   * @return duration
   */
  public String durationProperty() {
    return duration;
  }

  /**
   * Converts this event to string format.
   *
   * @return event string
   */
  public String eventToString() {
    if (description.isEmpty()) {
      return "Event: " + name + "\nDay: "
          + dayOfWeek.getDay() + "\nTime: " + setStartTime(startTime)
          + "\nDuration: " + duration;
    } else {
      if (isUrl(description)) {
        return "Event: " + name + "\nDay: " + dayOfWeek.getDay()
            + "\nTime: " + setStartTime(startTime)
            + "\nDuration: " + duration;
      } else {
        return "Event: " + name + "\nDay: " + dayOfWeek.getDay()
            + "\nDescription: " + description
            + "\nTime: " + setStartTime(startTime)
            + "\nDuration: " + duration;
      }
    }
  }

  /**
   * Determines if the string is a url.
   *
   * @param possiblyUrl the url to check
   * @return true if it is a url
   */
  public boolean isUrl(String possiblyUrl) {
    URL url;
    try {
      url = new URL(possiblyUrl);
      return true;
    } catch (MalformedURLException e) {
      return false;
    }
  }
}

