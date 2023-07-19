package cs3500.pa05.controller;

import cs3500.pa05.model.DayType;
import cs3500.pa05.model.Event;
import cs3500.pa05.model.FileWriter;
import cs3500.pa05.model.JsonUtils;
import cs3500.pa05.model.Task;
import cs3500.pa05.model.json.DayJson;
import cs3500.pa05.model.json.EventJson;
import cs3500.pa05.model.json.TaskJson;
import cs3500.pa05.model.json.ThemeJson;
import cs3500.pa05.model.json.WeekJson;
import cs3500.pa05.view.BujoView;
import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Side;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.stage.Stage;


/**
 * Represents the controller for the week GUI
 */
public class WeekController implements ControllerInterface {
  private WeekJson weekJson;
  private String name;
  private int intOne = 0;
  private int intTwo = 0;
  @FXML
  private MenuButton urlpicker;
  @FXML
  private Button delete;
  @FXML
  private Button delete1;
  @FXML
  private Label weekName;
  @FXML
  private Button newEvent;
  @FXML
  private Button newTask;
  @FXML
  private Button save;
  @FXML
  private HBox taskQueue;
  @FXML
  private GridPane daysGrid;
  @FXML
  private Label taskQueueName;
  @FXML
  private Button addQuote;
  @FXML
  private Label note;
  @FXML
  private GridPane taskList;
  private List<DayJson> days;
  private EventController eventController;
  private TaskController taskController;
  private Dialog<String> warning;
  private TextInputDialog notes;
  private List<TaskJson> tasks;
  private List<Integer> openEventTaskSpots;
  private int openTaskQueueSpot;
  private Stage stage;
  private String path;
  @FXML
  private ImageView icon;
  private PieChart stats;
  @FXML
  private Button weekOverview;


  /**
   * Instantiates a WeekController and displays data from
   * an existing .bujo file
   *
   * @param w the current weekJson
   * @param s the stage to render the scene on
   * @param p the path to the .bujo as a string
   */
  public WeekController(WeekJson w, Stage s, String p) {
    path = p;
    weekJson = w;
    stage = s;
    weekName = new Label();
    name = weekJson.name();
    newEvent = new Button();
    newTask = new Button();
    save = new Button();
    notes = new TextInputDialog();
    daysGrid = new GridPane();
    days = new ArrayList<>();
    taskQueue = new HBox();
    warning = new Dialog<>();
    taskQueueName = new Label("Tasks");
    taskList = new GridPane();
    tasks = new ArrayList<>();
    note = new Label();
    addQuote = new Button("Add quote");
    openEventTaskSpots = new ArrayList<>(Arrays.asList(0, 0, 0, 0, 0, 0, 0));
    openTaskQueueSpot = 0;
    stats = new PieChart();
    weekOverview = new Button();
    icon = new ImageView();
    delete = initButton();
    delete1 = initButton();
    urlpicker = new MenuButton();
  }

  /**
   * Runs this controller.
   */
  public void run() {
    handleTheme();
    setActions();
    setWarning();
    weekName.setText(name);
    days.addAll(weekJson.days());
    tasks.addAll(weekJson.taskQueue());
    note.setText(weekJson.note());
    displayEvents();
    displayTasks();

  }

  /**
   * Handles the theme by delegating to helper methods to change
   * font and colors.
   */
  private void handleTheme() {
    if (weekJson.chosenTheme().name().equals("forest")) {
      handleForestTheme();
    } else if (weekJson.chosenTheme().name().equals("ocean")) {
      handleOceanTheme();
    } else if (weekJson.chosenTheme().name().equals("neon")) {
      handleNeonTheme();
    } else {
      String color = weekJson.chosenTheme().name();
      daysGrid.setStyle("-fx-background-color: #" + color.substring(2));
      weekName.setTextFill(Paint.valueOf("-fx-background-color: #" + color.substring(2)));
    }
  }

  /**
   * Sets actions for buttons
   */
  private void setActions() {
    newEvent.setOnAction(e -> handleNewEvent());
    newTask.setOnAction(e -> handleNewTask());
    save.setOnAction(e -> handleSave());
    addQuote.setOnAction(e -> handleNoteQuote());
    weekOverview.setOnAction(e -> updateOverview());
  }

  /**
   * Updates and shows the weekly overview
   */
  private void updateOverview() {
    int numEvents = 0;
    int numComplete = 0;
    int numIncomplete = 0;
    for (DayJson d : days) {
      numEvents += d.events().size();
      for (TaskJson t : d.tasks()) {
        if (t.completed().equalsIgnoreCase("false")) {
          numIncomplete++;
        } else {
          numComplete++;
        }
      }
    }

    ObservableList<PieChart.Data> statsData = FXCollections.observableArrayList(
        new PieChart.Data("Complete", numComplete),
        new PieChart.Data("Incomplete", numIncomplete));
    stats.setData(statsData);
    stats.setTitle("Completed Tasks");
    stats.setLegendVisible(true);
    stats.setLegendSide(Side.BOTTOM);
    stats.setLabelsVisible(false);
    Group group = new Group();
    group.getChildren().add(stats);
    Dialog<Group> overview = new Dialog<>();
    ButtonType ok = new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE);
    overview.setTitle("Week Overview");
    overview.setContentText("Total Events: " + numEvents + ",      Total Tasks: " + tasks.size());
    overview.getDialogPane().getButtonTypes().add(ok);
    overview.setGraphic(group);
    overview.showAndWait();
  }

  /**
   * Sets up warning dialog
   */
  private void setWarning() {
    ButtonType ok = new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE);
    warning.setTitle("Warning");
    warning.setContentText("");
    warning.getDialogPane().getButtonTypes().add(ok);
  }

  /**
   * Handles displaying note text input dialog
   */
  private void handleNoteQuote() {
    notes.setTitle("Quotes and Notes");
    notes.setHeaderText("Add a quote or note");
    notes.setContentText("Quote/note:");
    notes.showAndWait().ifPresent(result -> {
      note.setText(result);
    });

  }

  /**
   * Initializes a button.
   *
   * @return the button initialized
   */
  @FXML
  public Button initButton() {
    intOne++;
    intTwo++;
    Button button = new Button("del");
    button.setOnAction(event -> System.out.println("working!!"));
    button.setId(String.valueOf(intOne));
    button.toFront();
    button.arm();
    return button;
  }

  /**
   * Displays existing event data from previous saved file
   * to the week GUI
   */
  private void displayEvents() {
    for (DayJson d : days) {
      for (EventJson e : d.events()) {
        Event event = JsonUtils.convertEventJson(e);
        displayEvent(event);
      }
    }
  }

  /**
   * Displays the given event to the week gui
   *
   * @param event event
   */
  private void displayEvent(Event event) {
    Label newEventLabel = new Label();
    newEventLabel.setWrapText(true);
    newEventLabel.setText(event.eventToString());
    isHyperLink(event);
    GridPane.setValignment(newEventLabel, VPos.TOP);
    newEventLabel.setPadding(new Insets(10, 20, 10, 20));
    int index = findDayIndex(event.dayOfWeekProperty());
    int row = openEventTaskSpots.get(index);
    this.daysGrid.add(newEventLabel, index, row);
    openEventTaskSpots.set(index, row + 1);
    delete1.setText("delete: " + event.nameProperty() + newEventLabel.getId());
    delete.setOnAction(e -> {
      newEventLabel.setText("");
      daysGrid.getChildren().remove(newEventLabel);
      for (MenuItem item : urlpicker.getItems()) {
        if (item.getText().equals(event.nameProperty())) {
          urlpicker.getItems().remove(item);
        }
        for(DayJson d: days){
          for(EventJson eventt: d.events()){
            if(eventt.name().equals(event.nameProperty())){
              d.events().remove(eventt);
            }
          }
        }
      }
    });
  }

  public void handleRemove(){

  }

  /**
   * Displays existing task data from saved file
   * to the week GUI and task queue
   */
  private void displayTasks() {
    for (DayJson d : days) {
      for (TaskJson t : d.tasks()) {
        Task task = JsonUtils.convertTaskJson(t);
        displayTask(task);
      }
    }
  }

  /**
   * Displays the given task to the week gui
   * and the task queue
   *
   * @param task task to display
   */
  private void displayTask(Task task) {
    Label newTaskLabel = new Label();
    newTaskLabel.setWrapText(true);
    newTaskLabel.setText(task.taskToString());
    GridPane.setValignment(newTaskLabel, VPos.TOP);
    newTaskLabel.setPadding(new Insets(10, 20, 10, 20));
    int index = findDayIndex(task.dayOfWeekProperty());
    int row = openEventTaskSpots.get(index);
    this.daysGrid.add(newTaskLabel, index, row);
    openEventTaskSpots.set(index, row + 1);
    isHyperLinkTask(task);
    Label taskQueueLabel = new Label();
    taskQueueLabel.setWrapText(true);
    String status;
    if (task.completeProperty()) {
      status = "complete";
    } else {
      status = "incomplete";
    }
    taskQueueLabel.setText(task.nameProperty() + ": " + status);

    delete.setText("delete: " + task.nameProperty() + newTaskLabel.getId());

    taskList.add(taskQueueLabel, openTaskQueueSpot, 0);
    openTaskQueueSpot++;

    delete.setOnAction(e -> {
      newTaskLabel.setText("");
      daysGrid.getChildren().remove(newTaskLabel);
      for (MenuItem item : urlpicker.getItems()) {
        if (item.getText().equals(task.nameProperty())) {
          urlpicker.getItems().remove(item);
        }
        taskList.getChildren().remove(taskQueueLabel);
        taskQueueLabel.setText("");

        for(DayJson day: days){
          for(TaskJson taskk: day.tasks()){
            if(taskk.name().equals(task.nameProperty())){
              day.tasks().remove(taskk);
            }
          }
        }
      }});
  }

  /**
   * Sets the days list of this to the given list
   *
   * @param d the list of days
   */
  public void setDays(List<DayJson> d) {
    if (d.size() == 7) {
      this.days = d;
    } else {
      throw new IllegalArgumentException("There must be 7 days");
    }
  }

  /**
   * Handles creating a new event based on button click
   */
  private void handleNewEvent() {
    // switch scene to new event and delegate to EventController
    eventController = new EventController(stage.getScene(), stage, this);
    BujoView eventView = new BujoView(eventController, "eventpopup.fxml");
    try {
      stage.setScene(eventView.load());
      eventController.run();
      stage.show();
    } catch (IllegalStateException e) {
      System.err.println("Can't load new event");
    }
  }

  /**
   * Saves new event from event controller to the week GUI
   */
  public void saveNewEvent() {
    // get data of new event from event controller and add to week view
    Event event = eventController.getEvent();
    EventJson eventJson = JsonUtils.convertEvent(event);
    for (DayJson d : days) {
      if (event.dayOfWeekProperty().equals(d.day())) {
        d.events().add(eventJson);
        if (d.events().size() == weekJson.eventMax() + 1) {
          warning.setContentText("Day has reached maximum event limit.");
          d.events().remove(eventJson);
          warning.showAndWait();
        } else {
          displayEvent(event);
        }
      }
    }
  }

  /**
   * Finds the column index of the given day in the week
   *
   * @param day weekday
   * @return column index
   */
  private int findDayIndex(DayType day) {
    int index = 0;
    for (int i = 0; i < days.size(); i++) {
      if (days.get(i).day() == (day)) {
        index = i;
      }
    }
    return index;
  }

  /**
   * Handles creating a new task based on button click
   */
  private void handleNewTask() {
    // switch scene to new task and delegate to TaskController
    this.taskController = new TaskController(stage.getScene(), stage, this);
    BujoView taskView = new BujoView(taskController, "task.fxml");
    try {
      stage.setScene(taskView.load());
      this.taskController.run();
      stage.show();
    } catch (IllegalStateException e) {
      System.err.println("Can't load new task");
    }
  }

  /**
   * Saves new event from event controller to the week GUI
   */
  public void saveNewTask() {
    // get data of new event from event controller and add to week view
    Task task = taskController.getTask();
    TaskJson taskJson = JsonUtils.convertTask(task);
    tasks.add(taskJson);

    for (DayJson d : days) {
      if (task.dayOfWeekProperty().equals(d.day())) {
        d.tasks().add(taskJson);
        if (d.tasks().size() == weekJson.taskMax() + 1) {
          warning.setContentText("Day has reached maximum task limit.");
          tasks.remove(taskJson);
          d.tasks().remove(taskJson);
          warning.showAndWait();
        } else {
          displayTask(task);
        }
      }
    }
  }

  /**
   * Handles saving to bujo file when save is clicked
   */
  private void handleSave() {
    this.weekJson.taskQueue().addAll(this.tasks);
    int taskMax = weekJson.taskMax();
    int eventMax = weekJson.eventMax();
    String n = weekJson.name();
    List<ThemeJson> themes = weekJson.allThemes();
    ThemeJson currTheme = weekJson.chosenTheme();
    List<DayJson> days = weekJson.days();
    List<TaskJson> taskQ = weekJson.taskQueue();
    String note = this.note.getText();
    WeekJson updated = new WeekJson(n, taskMax, eventMax, themes,
        currTheme, days, taskQ, note);
    FileWriter fileWriter = new FileWriter(updated, this.path);
    fileWriter.writeBujo();
  }

  /**
   * A theme for the GUI
   */
  private void handleOceanTheme() {
    icon.setImage(new Image(
        "https://upload.wikimedia.org/wikipedia/commons/"
            + "thumb/7/7e/Emoji_u1f30a.svg/1024px-Emoji_u1f30a.svg.png"));
    icon.setPreserveRatio(true);
    stage.getScene().setFill(Color.LIGHTBLUE);
    Font font = Font.font("Lucida Calligraphy", 30);
    weekName.setFont(font);
    weekName.setTextFill(Color.WHITE);
    daysGrid.setStyle("-fx-background-color: #03b0f1");
    taskQueue.setStyle("-fx-background-color: #ffcc7f");
    newEvent.setFont(Font.font("Lucida Calligraphy", 12));
    newEvent.setStyle("-fx-background-color: #55A5FF");
    newEvent.setTextFill(Color.WHITE);
    newTask.setFont(Font.font("Lucida Calligraphy", 12));
    newTask.setStyle("-fx-background-color: #46ECB2");
    newTask.setTextFill(Color.WHITE);
    save.setFont(Font.font("Lucida Calligraphy", 12));
    save.setStyle("-fx-background-color: #68F3F3");
    save.setTextFill(Color.WHITE);
  }

  /**
   * A theme for the GUI
   */
  private void handleForestTheme() {
    icon.setImage(new Image("https://www.clipartmax.com/png/middle/118-1181813_"
        + "emoji-forest-forest-emoji.png"));
    icon.setPreserveRatio(true);
    stage.getScene().setFill(Color.FORESTGREEN);
    Font font = Font.font("Verdana", 30);
    weekName.setFont(font);
    weekName.setTextFill(Color.WHITE);
    daysGrid.setStyle("-fx-background-color: #ab6f29");
    taskQueue.setStyle("-fx-background-color: #DEC260");
    taskQueueName.setTextFill(Color.WHITE);
    newEvent.setFont(Font.font("Verdana", 12));
    newEvent.setStyle("-fx-background-color: #289550");
    newTask.setFont(Font.font("Verdana", 12));
    newTask.setStyle("-fx-background-color: #2FB852");
    save.setFont(Font.font("Verdana", 12));
    save.setStyle("-fx-background-color: #AFE852");
  }

  /**
   * Determines if the event contains a hyperlink.
   *
   * @param eventt the event to check
   */
  private void isHyperLink(Event eventt) {
    if (eventt.isUrl(eventt.descriptionProperty())) {
      MenuItem item = new MenuItem(eventt.nameProperty());
      item.setText(eventt.nameProperty());
      System.out.println(item.getText());
      item.setId(eventt.nameProperty() + intTwo);
      item.setOnAction(e -> {
        URI url;
        try {
          url = new URI(eventt.descriptionProperty());
          Desktop.getDesktop().browse(new java.net.URI(url.toString()));
        } catch (URISyntaxException | IOException ex) {
          throw new RuntimeException(ex);
        }
      });
      urlpicker.getItems().add(item);
    }
  }

  /**
   * Determines if the description is a link.
   *
   * @param taskk the task to look at
   */
  private void isHyperLinkTask(Task taskk) {
    if (taskk.isUrlTask(taskk.descriptionProperty())) {
      MenuItem item = new MenuItem(taskk.nameProperty());
      item.setText(taskk.nameProperty());
      System.out.println(item.getText());
      item.setId(taskk.nameProperty() + intOne);
      item.setOnAction(e -> {
        URI url;
        try {
          url = new URI(taskk.descriptionProperty());
          Desktop.getDesktop().browse(new java.net.URI(url.toString()));
        } catch (URISyntaxException | IOException ex) {
          throw new RuntimeException(ex);
        }
      });
      urlpicker.getItems().add(item);
    }
  }

  /**
   * A theme for the GUI
   */
  private void handleNeonTheme() {
    icon.setImage(new Image("https://cdn.shopify.com/s/files/1/1061/1924/"
        + "products/Smiling_Face_Emoji_large.png?v=1571606036"));
    icon.setPreserveRatio(true);
    stage.getScene().setFill(Color.HOTPINK);
    Font font = Font.font("impact", 30);
    weekName.setFont(font);
    daysGrid.setStyle("-fx-background-color: #f653ad");
    taskQueue.setStyle("-fx-background-color: #ae2ade");
    taskQueueName.setTextFill(Color.WHITE);
    newEvent.setFont(Font.font("impact", 12));
    newEvent.setStyle("-fx-background-color: #069dfa");
    newTask.setFont(Font.font("impact", 12));
    newTask.setStyle("-fx-background-color: #fd6705");
    save.setFont(Font.font("impact", 12));
    save.setStyle("-fx-background-color: #23f804");
  }
}
