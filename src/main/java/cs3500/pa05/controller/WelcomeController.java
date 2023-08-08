package cs3500.pa05.controller;

import cs3500.pa05.model.DayType;
import cs3500.pa05.model.FileReader;
import cs3500.pa05.model.Theme;
import cs3500.pa05.model.json.DayJson;
import cs3500.pa05.model.json.TaskJson;
import cs3500.pa05.model.json.WeekJson;
import cs3500.pa05.view.BujoView;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


/**
 * Represents the controller for the welcome scene.
 */
public class WelcomeController implements ControllerInterface {
    private Stage stage;
    @FXML
    private TextField pathField;
    @FXML
    private Button journalButton;
    private Dialog<String> warning;
    private String bujoPath;
    private WeekJson bujoWeek;
    private boolean newPath;

    /**
     * Instantiates a new WelcomeController by creating new fxml components
     * to open a new or existing .bujo.
     *
     * @param s the stage to render
     */
    public WelcomeController(Stage s) {
        stage = s;
        pathField = new TextField();
        journalButton = new Button("Journal!");
        warning = new Dialog<>();
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
     * Closes the scene and saves the .bujo file chosen by the user.
     */
    private void closeScene() {
        if (validPath()) {
            setBujoWeek();
            SettingsController contr = new SettingsController(bujoWeek, stage, bujoPath);
            BujoView view = new BujoView(contr, "setup.fxml");
            stage.setScene(view.load());
            contr.run();
            stage.show();
        } else {
            displayWarning("Please provide a path to valid .bujo file, or "
                    + "a path to create a new .bujo file");
            warning.showAndWait();
        }
    }

    /**
     * If the user provides a valid path that ends with .bujo,
     * store it as a field of this
     *
     * @return true if the user provides a valid path
     */
    private boolean validPath() {
        String inputPath = pathField.getText();
        File file = new File(inputPath);
        this.bujoPath = inputPath;

        if (file.exists() && file.isFile() && inputPath.endsWith(".bujo")) {
            // if the user inputs a path to an existing .bujo, read it
            this.newPath = false;
            return true;
        } else if (!file.exists() && inputPath.endsWith(".bujo")) {
            // if the user inputs a path to a non-existing .bujo, create a new file
            this.newPath = true;
            return true;
        } else {
            // if the path isn't valid, return false
            return false;
        }
    }

    /**
     * Sets this bujo week to prior contents or creates a new bujo week
     * if new path
     */
    public void setBujoWeek() {
        if (this.newPath) {
            // if it's a newly created .bujo, make a new weekJson
            List<DayJson> days = new ArrayList<>();
            days.add(new DayJson(DayType.SUNDAY, new ArrayList<>(), new ArrayList<>()));
            days.add(new DayJson(DayType.MONDAY, new ArrayList<>(), new ArrayList<>()));
            days.add(new DayJson(DayType.TUESDAY, new ArrayList<>(), new ArrayList<>()));
            days.add(new DayJson(DayType.WEDNESDAY, new ArrayList<>(), new ArrayList<>()));
            days.add(new DayJson(DayType.THURSDAY, new ArrayList<>(), new ArrayList<>()));
            days.add(new DayJson(DayType.FRIDAY, new ArrayList<>(), new ArrayList<>()));
            days.add(new DayJson(DayType.SATURDAY, new ArrayList<>(), new ArrayList<>()));
            List<String> allThemes = new ArrayList<>();
            for (Theme t : Theme.values()) {
                allThemes.add(t.toString());
            }
            String note = "This is your first note :)";
            List<TaskJson> taskQueue = new ArrayList<>();
            this.bujoWeek = new WeekJson("New Week", 5, 5, allThemes,
                    "NONE", days, taskQueue, note);
        } else {
            FileReader reader = new FileReader(Paths.get(bujoPath));
            this.bujoWeek = reader.readBujo();
        }
    }


    /**
     * Runs the controller
     */
    @Override
    public void run() {
        setWarning();
        journalButton.setOnAction(event -> closeScene());
    }
}
