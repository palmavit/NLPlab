package sample.controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class AlertOnChange {
    @FXML
    private ResourceBundle resources;
    private static int indexChangeButton;

    @FXML
    private URL location;

    @FXML
    public Label labelChange;

    @FXML
    private Button editViewButton;

    @FXML
    private Button editDictionaryButton;

    @FXML
    private Button cancelButton;

    @FXML
    private Button editTextButton;

    @FXML
    void initialize() {
        indexChangeButton = 0;
        String text = "";
        switch (DictionaryController.getI()){
            case 1:
                editTextButton.setDisable(true);
                text = "Would you like to add \nthe word \'" + DictionaryController.getCurrentString() + "\' only to the \ntable view " +
                        "or to the dictionary \nand to the text either?";
                break;
            case 2:
                text = "Would you like to delete \nthe word \'" + DictionaryController.getCurrentString() + "\' only from the \ntable view " +
                        "or from the dictionary \nand from the text either?";
                break;
            case 3:
                text = "Would you like to change \nthe word \'" + DictionaryController.getCurrentString() + "\' only in the \ntable view " +
                        "or in the dictionary \nand in the text either?";
                break;
            default: text = "What would you like to do?";
        }
        labelChange.setText(text);
    }

    @FXML
    public void editView(){
        indexChangeButton = 1;
        editViewButton.getScene().getWindow().hide();
    }

    @FXML
    public void editDictionary(){
        indexChangeButton = 2;
        editViewButton.getScene().getWindow().hide();
    }

    @FXML
    public void editText(){
        indexChangeButton = 3;
        editViewButton.getScene().getWindow().hide();
    }

    @FXML
    public void cancel(){
        indexChangeButton = 0;
        editViewButton.getScene().getWindow().hide();
    }

    public static int getIndexChangeButton() {
        return indexChangeButton;
    }
}
