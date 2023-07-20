package cs3500.pa05.view;

import cs3500.pa05.controller.ControllerInterface;
import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

/**
 * Represents a view in the .bujo program.
 */
public class BujoView {
  private FXMLLoader loader;

  /**
   * Loads new event scene
   *
   * @param evc event controller
   * @param fxml the scene to load
   */
  public BujoView(ControllerInterface evc, String fxml) {
    this.loader = new FXMLLoader();
    this.loader.setLocation(getClass().getClassLoader().getResource(fxml));
    this.loader.setController(evc);
  }

  /**
   * Loads given .fxml scene
   *
   * @param fxml the scene to load
   */
  public BujoView(String fxml) {
    this.loader = new FXMLLoader();
    this.loader.setLocation(getClass().getClassLoader().getResource(fxml));
  }

  /**
   * Loads a scene from a new event GUI layout
   *
   * @return the layout
   */
  public Scene load() {
    try {
      return this.loader.load();
    } catch (IOException e) {
      throw new IllegalStateException("Can't load the scene.");
    }
  }

}
