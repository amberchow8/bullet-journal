package cs3500.pa05.controller;

import cs3500.pa05.model.DayType;
import cs3500.pa05.model.Event;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * Controller for New Event Scene.
 */
public class EventController implements ControllerInterface {
  private WeekController weekController;
  private Event event;
  private Stage stage;
  private Scene weekScene;
  @FXML
  private Button saveEvent;
  @FXML
  private TextField nameField;
  @FXML
  private TextField descripField;
  @FXML
  private ComboBox<DayType> dayField;
  @FXML
  private TextField startField;
  @FXML
  private TextField durationField;
  private Dialog<String> warning;

  /**
   * Instantiates a new EventController by creating new fxml components
   * to create a new event.
   *
   * @param w the scene
   * @param s the stage
   * @param c the controller for the week
   */
  public EventController(Scene w, Stage s, WeekController c) {
    weekController = c;
    stage = s;
    weekScene = w;
    saveEvent = new Button("Save");
    nameField = new TextField();
    descripField = new TextField();
    dayField = new ComboBox<DayType>();
    startField = new TextField();
    durationField = new TextField();
    warning = new Dialog<>();
  }

  /**
   * Sets up warning dialog to handle incorrect event settings.
   */
  private void setWarning() {
    ButtonType ok = new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE);
    warning.setTitle("Warning");
    warning.getDialogPane().getButtonTypes().add(ok);
  }

  /**
   * Displays a warning popup with given message.
   *
   * @param message warning message
   */
  private void displayWarning(String message) {
    warning.setContentText(message);
  }

  /**
   * Adds DayTypes to the day field options.
   */
  @FXML
  public void addDayChoices() {
    dayField.getItems().add(DayType.MONDAY);
    dayField.getItems().add(DayType.TUESDAY);
    dayField.getItems().add(DayType.WEDNESDAY);
    dayField.getItems().add(DayType.THURSDAY);
    dayField.getItems().add(DayType.FRIDAY);
    dayField.getItems().add(DayType.SATURDAY);
    dayField.getItems().add(DayType.SUNDAY);
  }

  /**
   * Gets the user event input.
   *
   * @return the Event created by the user
   */
  public Event getEvent() {
    return this.event;
  }

  /**
   * Sets this event to the user inputted fields.
   *
   * @return if the fields are valid
   */
  private boolean setEvent() {
    String name = nameField.getText();
    String description = descripField.getText();
    DayType day = dayField.getValue();
    String startTime = startField.getText();
    String duration = durationField.getText();
    // handles cases with incorrect user input by displaying a warning
    if (name.isEmpty()) {
      displayWarning("Name cannot be empty");
      warning.showAndWait();
      return false;
    } else if (startTime.isEmpty()) {
      displayWarning("Start time cannot be empty");
      warning.showAndWait();
      return false;
    } else if (duration.isEmpty()) {
      displayWarning("Duration cannot be empty");
      warning.showAndWait();
      return false;
    } else {
      // creates and sets the event when input is valid
      Event newEvent = new Event(name, description, startTime, duration, day);
      this.event = newEvent;
      return true;
    }
  }

  /**
   * Closes the scene and saves the event under the correct conditions.
   */
  private void closeScene() {
    if (setEvent()) {
      weekController.saveNewEvent();
      stage.setScene(weekScene);
    }
  }

  /**
   * Runs the controller.
   */
  @Override
  public void run() {
    addDayChoices();
    setWarning();
    saveEvent.setOnAction(e -> closeScene());
  }
}
