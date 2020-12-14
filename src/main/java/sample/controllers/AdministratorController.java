package sample.controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;

public class AdministratorController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ScrollPane scrollViewers;

    @FXML
    private Button createViewerButton;

    @FXML
    private Button deleteViewerButton;

    @FXML
    private ScrollPane scrollDevelopers;

    @FXML
    private Button createDeveloperButton;

    @FXML
    private Button deleteDeveloperButton;

    @FXML
    void initialize() {

    }
}
