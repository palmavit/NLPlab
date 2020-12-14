package sample.controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class RegisterController {

    @FXML
    private AnchorPane parentContainerReg;

    @FXML
    private VBox vBoxReg;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private BorderPane welcomeRegisterPane;

    @FXML
    private Label welcomeRegisterLabel;

    @FXML
    private TextField usernameRegisterField;

    @FXML
    private TextField passwordRegisterField;

    @FXML
    private Button registerButton;

    @FXML
    private TextField nameRegisterField;

    @FXML
    private TextField lastnameRegisterField;

    @FXML
    void initialize() {
    }
}
