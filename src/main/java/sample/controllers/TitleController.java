package sample.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class TitleController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;


    @FXML
    private VBox vBox;

    @FXML
    private BorderPane welcomePane;

    @FXML
    private Label welcomeLabel;

    @FXML
    private TextField titleUsernameField;

    @FXML
    private TextField titlePasswordField;

    @FXML
    private Button titleLogInButton;

    @FXML
    private Button titleRegisterButton;


    @FXML
    void initialize() {
    }


    @FXML
    void goToRegister(ActionEvent event) throws IOException {

        Stage stage = (Stage) (titleLogInButton.getScene().getWindow());
        ZeroController.jumpToStage(stage, "/stages/registerNLPPage.fxml");

    }


    @FXML
    void enter(ActionEvent event) throws IOException{

        Stage stage = (Stage) (titleLogInButton.getScene().getWindow());

        String name = titleUsernameField.getText();
        if(name.equals("a"))
            ZeroController.jumpToStage(stage, "/stages/adminNLPPage.fxml");
        else if (name.equals("d"))
            ZeroController.jumpToStage(stage, "/stages/developerNLPPage.fxml");
        else  ZeroController.jumpToStage(stage, "/stages/userNLPPage.fxml");

    }

}
