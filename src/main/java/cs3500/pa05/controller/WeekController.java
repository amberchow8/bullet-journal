package cs3500.pa05.controller;

import cs3500.pa05.model.DayType;
import cs3500.pa05.model.Event;
import cs3500.pa05.model.FileWriter;
import cs3500.pa05.model.JsonUtils;
import cs3500.pa05.model.Task;
import cs3500.pa05.model.json.DayJson;
import cs3500.pa05.model.json.EventJson;
import cs3500.pa05.model.json.TaskJson;
import cs3500.pa05.model.json.WeekJson;
import cs3500.pa05.view.BujoView;

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
import javafx.scene.Node;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
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
    private GridPane gridBackground;
    @FXML
    private Label taskQueueName;
    @FXML
    private Button addQuote;
    @FXML
    private Label note;
    @FXML
    private HBox headerBox;
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
        headerBox = new HBox();
        daysGrid = new GridPane();
        gridBackground = new GridPane();
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
        System.out.println("Tasks size: " + tasks.size());
        note.setText(weekJson.note());
        displayEvents();
        displayTasks();
    }

    /**
     * Handles the theme by delegating to helper methods to change
     * font and colors.
     */
    private void handleTheme() {
        if (weekJson.chosenTheme().equals("FOREST")) {
            handleForestTheme();
        } else if (weekJson.chosenTheme().equals("OCEAN")) {
            handleOceanTheme();
        } else if (weekJson.chosenTheme().equals("NEON")) {
            handleNeonTheme();
        } else if (!weekJson.chosenTheme().equals("NONE")) {
            String color = weekJson.chosenTheme();
            headerBox.setBackground(new Background(
                    new BackgroundFill(Paint.valueOf(color), CornerRadii.EMPTY, Insets.EMPTY)));
            taskQueue.setBackground(new Background(
                    new BackgroundFill(Paint.valueOf(color), CornerRadii.EMPTY, Insets.EMPTY)));
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

//  /**
//   * Shows pop-up to delete an event
//   */
//  private void handleDeleteEvent() {
//    Dialog<ButtonType> dialog = new Dialog<>();
//    TableView<Object> tableView = new TableView<>();
//    TableColumn<Object, String> eventNameColumn = new TableColumn<>("Event Name");
//    eventNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
//    ObservableList<Object> allEvents = FXCollections.observableArrayList();
//    for (DayJson day : this.days) {
//      for (EventJson eventJson : day.events()) {
//        Event event = JsonUtils.convertEventJson(eventJson);
//        allEvents.add(event);
//      }
//    }
//    //tableView.getItems().addAll(allEvents);
//    tableView.setItems(allEvents);
//    tableView.getColumns().add(eventNameColumn);
//    ButtonType close = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
//    dialog.setTitle("Delete Event");
//    dialog.getDialogPane().setContent(tableView);
//    dialog.getDialogPane().getButtonTypes().add(close);
//    dialog.showAndWait();
//  }

    /**
     * Completes the given task
     */
    private void handleTaskComplete(Label newTaskLabel, Label taskQueueLabel, Task task) {
        TaskJson convertedTask = JsonUtils.convertTask(task);
        for (TaskJson taskJson : this.tasks) {
            if (convertedTask.equals(taskJson)) {
                int index = this.tasks.indexOf(taskJson);
                this.tasks.set(index, new TaskJson(taskJson.name(), taskJson.description(),
                        taskJson.day(), "true"));
            }
        }
        for (DayJson dayJson : this.days) {
            if (convertedTask.day().equals(dayJson.day().getDay())) {
                for (TaskJson taskJson : dayJson.tasks()) {
                    if (convertedTask.equals(taskJson)) {
                        int index = dayJson.tasks().indexOf(taskJson);
                        dayJson.tasks().set(index, new TaskJson(taskJson.name(), taskJson.description(),
                                taskJson.day(), "true"));
                    }
                }
            }
        }
        task.setIsComplete(true);
        newTaskLabel.setText(task.taskToString());
        taskQueueLabel.setText(task.nameProperty() + ": " + "complete");
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
        newEventLabel.setBackground(new Background(new BackgroundFill(Color.PINK,
                CornerRadii.EMPTY, Insets.EMPTY)));
        GridPane.setValignment(newEventLabel, VPos.TOP);
        newEventLabel.setPadding(new Insets(10, 10, 10, 10));
        newEventLabel.setMaxWidth(140);
        int index = findDayIndex(event.getDayOfWeek());
        int row = openEventTaskSpots.get(index);
        this.daysGrid.add(newEventLabel, index, row);
        openEventTaskSpots.set(index, row + 1);

        // add delete button to the label
        Button delete = new Button("X");
        delete.setStyle("-fx-font-size: 6pt;");
        newEventLabel.setGraphic(delete);
        newEventLabel.setContentDisplay(ContentDisplay.RIGHT);
        delete.setOnAction(e -> removeEvent(newEventLabel, event));
    }

    /**
     * Removes the given event from its DayJson events list
     *
     * @param eventLabel label
     * @param event      event
     */
    private void removeEvent(Label eventLabel, Event event) {
        EventJson convertedEvent = JsonUtils.convertEvent(event);
        this.daysGrid.getChildren().remove(eventLabel);

        for (DayJson dayJson : this.days) {
            dayJson.events().remove(convertedEvent);
        }
    }

    /**
     * Displays existing task data from saved file
     * to the week GUI and task queue
     */
    private void displayTasks() {
        for (DayJson d : days) {
            for (TaskJson t : d.tasks()) {
                Task task = JsonUtils.convertTaskJson(t);
                System.out.println(task.taskToString());
                displayTask(task);
            }
        }
    }

    /**
     * Displays given label with given task onto the task queue
     *
     * @param task task
     * @param taskQueueLabel label
     */
    private void displayTaskQueueLabel(Task task, Label taskQueueLabel) {
        taskQueueLabel.setWrapText(true);
        String status;
        if (task.completeProperty()) {
            status = "complete";
        } else {
            status = "incomplete";
        }
        taskQueueLabel.setText(task.nameProperty() + ": " + status);
        taskList.add(taskQueueLabel, openTaskQueueSpot, 0);
        openTaskQueueSpot++;
    }

    /**
     * Displays the given task to the week gui
     * and the task queue
     *
     * @param task task to display
     */
    private void displayTask(Task task) {
        VBox vbox = new VBox();
        Label newTaskLabel = new Label();
        newTaskLabel.setWrapText(true);
        newTaskLabel.setText(task.taskToString());
        vbox.setBackground(new Background(new BackgroundFill(Color.LIGHTYELLOW,
                CornerRadii.EMPTY, Insets.EMPTY)));
        GridPane.setValignment(vbox, VPos.TOP);
        vbox.setPadding(new Insets(10, 10, 10, 10));
        vbox.setMaxWidth(140);
        vbox.setPrefHeight(150);
        vbox.setMaxHeight(200);
        vbox.getChildren().add(newTaskLabel);
        int index = findDayIndex(task.dayOfWeekProperty());
        int row = openEventTaskSpots.get(index);
        this.daysGrid.add(vbox, index, row);
        openEventTaskSpots.set(index, row + 1);

        // add task queue label
        Label taskQueueLabel = new Label();
        displayTaskQueueLabel(task, taskQueueLabel);

        // add delete button to the label
        Button delete = new Button("X");
        delete.setStyle("-fx-font-size: 6pt;");
        newTaskLabel.setGraphic(delete);
        newTaskLabel.setContentDisplay(ContentDisplay.RIGHT);
        delete.setOnAction(e -> removeTask(vbox, taskQueueLabel, task));

        // add complete button to the label
        Button complete = new Button("Complete");
        complete.setStyle("-fx-font-size: 6pt;");
        vbox.getChildren().add(complete);
        complete.setOnAction(e -> handleTaskComplete(newTaskLabel, taskQueueLabel, task));
    }

    /**
     * Removes the given task from its DayJson tasks list
     * and the task queue list
     *
     * @param vbox label
     * @param task task
     */
    private void removeTask(VBox vbox, Label taskQueueLabel, Task task) {
        TaskJson convertedTask = JsonUtils.convertTask(task);
        this.daysGrid.getChildren().remove(vbox);
        this.taskList.getChildren().remove(taskQueueLabel);
        for (DayJson dayJson : this.days) {
            dayJson.tasks().remove(convertedTask);
        }
        openTaskQueueSpot--;
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
            if (event.getDayOfWeek().equals(d.day())) {
                d.events().add(eventJson);
                if (d.events().size() == weekJson.eventMax() + 1) {
                    warning.setContentText("Day has reached maximum event limit.");
                    d.events().remove(eventJson);
                    warning.showAndWait();
                } else {
                    displayEvent(event);
                    handleTheme();
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
                    handleTheme();
                }
            }
        }
    }

    /**
     * Handles saving to bujo file when save is clicked
     */
    private void handleSave() {
        int taskMax = weekJson.taskMax();
        int eventMax = weekJson.eventMax();
        String n = weekJson.name();
        List<String> themes = weekJson.allThemes();
        String currTheme = weekJson.chosenTheme();
        List<DayJson> days = weekJson.days();
        String note = this.note.getText();
        WeekJson updated = new WeekJson(n, taskMax, eventMax, themes,
                currTheme, days, this.tasks, note);
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
        gridBackground.setStyle("-fx-background-color: #03b0f1");
        taskQueue.setStyle("-fx-background-color: #ffcc7f;" + "-fx-border-color: #000000");
        newEvent.setFont(Font.font("Lucida Calligraphy", 12));
        newEvent.setStyle("-fx-background-color: #55A5FF");
        newEvent.setTextFill(Color.WHITE);
        newTask.setFont(Font.font("Lucida Calligraphy", 12));
        newTask.setStyle("-fx-background-color: #46ECB2");
        newTask.setTextFill(Color.WHITE);
        save.setFont(Font.font("Lucida Calligraphy", 12));
        save.setStyle("-fx-background-color: #68F3F3");
        save.setTextFill(Color.WHITE);
        weekOverview.setFont(Font.font("Lucida Calligraphy", 12));
        weekOverview.setStyle("-fx-background-color: #03b0f1");
        weekOverview.setTextFill(Color.WHITE);
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
