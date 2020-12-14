package sample.controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import javax.swing.text.html.HTML;

public class AlertOnExist {

    private static int indexExistButton;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Label labelExist;

    @FXML
    private Button addQuantityButton;

    @FXML
    private Button cancelButton;

    @FXML
    void addQuantity(ActionEvent event) {
        indexExistButton = 1;
        labelExist.getScene().getWindow().hide();
    }

    @FXML
    void cancel(ActionEvent event) {
        indexExistButton = 0;
        labelExist.getScene().getWindow().hide();
    }

    @FXML
    void initialize() {
        indexExistButton = 0;
        String text = "The word \'";
        if (DictionaryController.getNewString() != null)
            text += DictionaryController.getNewString();
        else text += TagDictionaryController.getNewString();
        text += "\' already exists.\n";
        switch (DictionaryController.getI()){
            case 1:
                text += "Would you like to \nincrement it`s quantity?";
                break;
            case 3:
                text += "Would you like to \nsummary their quantities?";
                break;
            default: break;
        }
        labelExist.setText(text);
    }

    public static int getIndexExistButton() {
        return indexExistButton;
    }
}
