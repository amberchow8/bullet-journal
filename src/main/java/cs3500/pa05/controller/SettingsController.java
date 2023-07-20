package cs3500.pa05.controller;

import cs3500.pa05.model.Theme;
import cs3500.pa05.model.json.ThemeJson;
import cs3500.pa05.model.json.WeekJson;
import cs3500.pa05.view.BujoView;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * Represents the controller for the game window that generates weekly settings.
 */
public class SettingsController implements ControllerInterface {
    private WeekJson bujoWeek;
    private Stage stage;
    @FXML
    private TextField nameField;
    @FXML
    private TextField eventMaxField;
    @FXML
    private TextField taskMaxField;
    @FXML
    private ComboBox<String> themeChoiceBox;
    @FXML
    private Button saveButton;
    private Dialog<String> warning;
    private Dialog<String> customTheme;
    private String selectedTheme;
    private String selectedName;
    private int selectedTaskMax;
    private int selectedEventMax;
    private String path;


    /**
     * Instantiates a controller to handle user settings.
     *
     * @param w the week the user is currently editing
     * @param s the stage to place the scene
     * @param p the string representation of the .bujo path
     */
    public SettingsController(WeekJson w, Stage s, String p) {
        this.path = p;
        this.bujoWeek = w;
        this.stage = s;
        this.nameField = new TextField();
        this.eventMaxField = new TextField();
        this.taskMaxField = new TextField();
        this.saveButton = new Button("Save Settings");
        this.warning = new Dialog<>();
        this.themeChoiceBox = new ComboBox<String>();
        this.customTheme = new Dialog<>();
    }

    /**
     * Sets up dialog to generate custom theme.
     */
    private void setCustomTheme() {
        ButtonType ok = new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE);
        customTheme.setTitle("Select Custom Theme");
        customTheme.getDialogPane().getButtonTypes().add(ok);

        ColorPicker colorPicker = new ColorPicker();
        colorPicker.setOnAction(event -> {
            Color selectedColor = colorPicker.getValue();
            selectedTheme = String.valueOf(selectedColor);
            bujoWeek.allThemes().add(selectedTheme);
        });
        customTheme.getDialogPane().setContent(colorPicker);
    }

    /**
     * Sets up warning dialog to handle incorrect paths.
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
     * Checks user input for week settings.
     *
     * @return whether the settings are valid
     */
    private boolean validSettings() {
        String name = nameField.getText();
        String eventMax = eventMaxField.getText();
        String taskMax = taskMaxField.getText();
        // handles cases with incorrect user input by displaying a warning
        if (name.isEmpty()) {
            selectedName = bujoWeek.name();
        } else {
            selectedName = name;
        }
        // handles event max by attempting to parse
        if (eventMax.isEmpty()) {
            selectedEventMax = bujoWeek.eventMax();
        } else {
            try {
                selectedEventMax = Integer.parseInt(eventMax);
            } catch (NumberFormatException e) {
                displayWarning("Event max must be an integer.");
                warning.showAndWait();
                return false;
            }
        }
        // handles task max by attempting to parse
        if (taskMax.isEmpty()) {
            selectedTaskMax = bujoWeek.taskMax();
        } else {
            try {
                selectedTaskMax = Integer.parseInt(taskMax);
            } catch (NumberFormatException e) {
                displayWarning("Task max must be an integer.");
                warning.showAndWait();
                return false;
            }
        }
        // handles the user creating a custom theme
        if (themeChoiceBox.getValue().equals("Custom theme")) {
            customTheme.setContentText("Choose a background color");
            customTheme.showAndWait();
        } else {
            selectedTheme = themeChoiceBox.getValue();
        }
        return true;
    }

    /**
     * Closes the scene by delegating to the WeekController to display the
     * next part of the program.
     */
    private void closeScene() {
        if (validSettings()) {
            WeekJson weekCopy = new WeekJson(selectedName, selectedTaskMax, selectedEventMax,
                    bujoWeek.allThemes(), selectedTheme, bujoWeek.days(), bujoWeek.taskQueue(),
                    bujoWeek.note());
            WeekController contr = new WeekController(weekCopy, stage, path);
            BujoView view = new BujoView(contr, "week.fxml");
            try {
                stage.setScene(view.load());
                contr.run();
                stage.show();
            } catch (IllegalStateException e) {
                System.err.println("Can't load week");
            }
        }
    }

    /**
     * Adds all theme choices to the comboBox.
     */
    private void addThemeChoices() {
        for (String theme : bujoWeek.allThemes()) {
            this.themeChoiceBox.getItems().add(theme);
        }
        this.themeChoiceBox.getItems().add("Custom theme");
        this.themeChoiceBox.setValue(bujoWeek.chosenTheme());
    }


    /**
     * Runs the controller.
     */
    @Override
    public void run() {
        addThemeChoices();
        setCustomTheme();
        setWarning();
        saveButton.setOnAction(event -> closeScene());
    }
}
