package cs3500.pa05.controller;

import cs3500.pa05.model.DayType;
import cs3500.pa05.model.FileReader;
import cs3500.pa05.model.json.DayJson;
import cs3500.pa05.model.json.TaskJson;
import cs3500.pa05.model.json.ThemeJson;
import cs3500.pa05.model.json.WeekJson;
import cs3500.pa05.view.BujoView;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
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
      if (!validBujo()) {
        displayWarning("Must provide path to valid .bujo file, or "
            + "the path to create a new .bujo :(");
        warning.showAndWait();
      } else {
        SettingsController contr = new SettingsController(bujoWeek, stage, bujoPath);
        BujoView view = new BujoView(contr, "setup.fxml");
        stage.setScene(view.load());
        contr.run();
        stage.show();
      }
    } else {
      displayWarning("Must provide path to valid .bujo file, or "
          + "the path to create a new .bujo :(");
      warning.showAndWait();
    }

  }

  /**
   * If the user provides a valid path, store it as a field of this.
   *
   * @return true if the user provides a valid path
   */
  private boolean validPath() {
    Random rand = new Random();
    String inputPath = pathField.getText();
    File file = new File(inputPath);

    if (file.exists() && file.isFile() && inputPath.endsWith(".bujo")) {
      // if the user input a path to an existing .bujo, read it
      this.bujoPath = file.getAbsolutePath();
      this.newPath = false;
      return true;
    } else if (file.exists()) {
      // if the user input a directory, create a new .bujo
      this.newPath = true;
      String fileName = "week" + rand.nextInt(0, 52) + ".bujo";
      File newFile = new File(file, fileName);
      try {
        if (file.canWrite() && newFile.createNewFile()) {
          // create an empty .bujo
          this.bujoPath = newFile.getAbsolutePath();
          return true;
        } else {
          return false;
        }
      } catch (IOException e) {
        return false;
      }
    } else {
      // if the path isn't valid at all, return false
      return false;
    }
  }


  /**
   * Determine whether the path input from the user contains valid .bujo contents.
   *
   * @return true if the user file can be parsed to a valid weekJson
   */
  public boolean validBujo() {
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
      List<ThemeJson> allThemes = new ArrayList<>();
      ThemeJson neon = new ThemeJson("neon");
      allThemes.add(neon);
      allThemes.add(new ThemeJson("ocean"));
      allThemes.add(new ThemeJson("forest"));
      String note = "This is your first note :)";
      List<TaskJson> taskQueue = new ArrayList<>();
      this.bujoWeek = new WeekJson("New Week", 5, 5, allThemes,
          neon, days, taskQueue, note);
      System.out.println(bujoWeek);
      return true;
    } else {
      FileReader reader = new FileReader(Paths.get(bujoPath));
      try {
        this.bujoWeek = reader.readBujo();
        return true;
      } catch (Exception e) {
        return false;
      }
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
