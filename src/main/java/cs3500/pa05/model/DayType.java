package cs3500.pa05.model;

/**
 * Represents the possible days of the week.
 */
public enum DayType {
  MONDAY("Monday"),
  TUESDAY("Tuesday"),
  WEDNESDAY("Wednesday"),
  THURSDAY("Thursday"),
  FRIDAY("Friday"),
  SATURDAY("Saturday"),
  SUNDAY("Sunday");

  private String day;

  private DayType(String day) {
    this.day = day;
  }

  /**
   * Returns the string representation of the day.
   *
   * @return a string day of the week
   */
  public String getDay() {
    return day;
  }

}
