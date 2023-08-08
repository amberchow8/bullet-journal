package cs3500.pa05.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests the methods in the task class.
 */
class TaskTest {
  private Task testTask;

  /**
   * Sets up fields for testing.
   */
  @BeforeEach
  public void setUp() {
    testTask = new Task("test", "a test task", DayType.SATURDAY, false);
  }

  /**
   * Tests the getter function.
   */
  @Test
  public void nameProperty() {
    assertEquals("test", testTask.nameProperty());
  }

  /**
   * Tests the getter function.
   */
  @Test
  public void descriptionProperty() {
    assertEquals("a test task", testTask.descriptionProperty());
  }

  /**
   * Tests the getter function.
   */
  @Test
  public void dayOfWeekProperty() {
    assertEquals(DayType.SATURDAY, testTask.dayOfWeekProperty());
  }

  /**
   * Tests the getter function.
   */
  @Test
  public void completeProperty() {
    assertFalse(testTask.completeProperty());
  }

  /**
   * Tests the setter function.
   */
  @Test
  public void setIsComplete() {
    testTask.setIsComplete(true);
    assertEquals(true, testTask.completeProperty());
  }

  /**
   * Tests the function that converts a task to a string.
   */
  @Test
  public void taskToString() {
    Task task = new Task("n", "", DayType.MONDAY, false);
    assertTrue(task.taskToString().contains("Task: n"));
    assertTrue(task.taskToString().contains("Day: Monday"));
    assertTrue(task.taskToString().contains("Status: incomplete"));
    assertTrue(testTask.taskToString().contains("Task: test"));
    assertTrue(testTask.taskToString().contains("Day: Saturday"));
    assertTrue(testTask.taskToString().contains("Description: a test task"));
    assertTrue(testTask.taskToString().contains("Status: incomplete"));
  }
}