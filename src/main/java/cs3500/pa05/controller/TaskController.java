package cs3500.pa05.controller;

import cs3500.pa05.model.DayType;
import cs3500.pa05.model.Task;
import java.util.ArrayList;
import java.util.List;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

/**
 * Represents a controller for the window that creates a new task.
 */
public class TaskController implements ControllerInterface {
  private Task task;
  @FXML
  private TextField nameField;
  @FXML
  private TextField descripField;
  @FXML
  private ComboBox<DayType> dayField;
  @FXML
  private Button save;
  private List<Task> taskList;
  private Dialog<String> warning;
  private WeekController weekController;
  private Stage stage;
  private Scene weekScene;

  /**
   * Instantiates a TaskController by creating new buttons and text fields.
   *
   * @param w the scene to generate
   * @param s the stage to render the scene on
   * @param c the current week controller
   */
  public TaskController(Scene w, Stage s, WeekController c) {
    this.nameField = new TextField();
    this.descripField = new TextField();
    this.dayField = new ComboBox<DayType>();
    this.save = new Button("Save");
    this.taskList = new ArrayList<>();
    this.warning = new Dialog<>();
    this.weekController = c;
    this.stage = s;
    this.weekScene = w;
  }

  /**
   * Sets up warning dialog to display incorrect user input.
   */
  private void setWarning() {
    ButtonType ok = new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE);
    warning.setTitle("Warning");
    warning.getDialogPane().getButtonTypes().add(ok);
  }

  /**
   * Displays a warning popup with given message.
   *
   * @param message the warning to give to the user
   */
  private void displayWarning(String message) {
    warning.setContentText(message);
  }

  /**
   * Sets this task to the current user input.
   */
  private boolean setTask() {
    String name = nameField.getText();
    String description = descripField.getText();
    DayType day = dayField.getValue();
    // handles cases of incorrect user input
    if (name.isEmpty()) {
      displayWarning("Name cannot be empty");
      warning.showAndWait();
      return false;
    } else {
      // creates a new task when the input is valid
      Task newTask = new Task(name, description, day, false);
      this.task = newTask;
      return true;
    }
  }

  /**
   * Gets the current new task.
   *
   * @return the current task
   */
  public Task getTask() {
    return this.task;
  }

  /**
   * Sets the given task as complete.
   *
   * @param task the task to set complete
   */
  private void setComplete(Task task) {
    for (Task t : taskList) {
      if (t.equals(task)) {
        t.setIsComplete(true);
      }
    }
  }

  /**
   * Sets choices for day combo box.
   */
  private void addDayChoices() {
    dayField.getItems().add(DayType.MONDAY);
    dayField.getItems().add(DayType.TUESDAY);
    dayField.getItems().add(DayType.WEDNESDAY);
    dayField.getItems().add(DayType.THURSDAY);
    dayField.getItems().add(DayType.FRIDAY);
    dayField.getItems().add(DayType.SATURDAY);
    dayField.getItems().add(DayType.SUNDAY);
  }

  /**
   * Runs the controller.
   */
  @Override
  public void run() {
    addDayChoices();
    setWarning();
    save.setOnAction(e -> closeScene());
  }

  /**
   * Closes new task scene based on set task.
   */
  private void closeScene() {
    if (setTask()) {
      stage.setScene(weekScene);
      stage.show();
      weekController.saveNewTask();
    }
  }
}
