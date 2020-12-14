package sample.controllers;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ResourceBundle;
import java.util.Scanner;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import sample.entities.CanonWord;
import sample.entities.HelpWord;

import javax.swing.*;

import static sample.controllers.DictionaryController.getCurrentString;

public class CanonController {

    public ObservableList<CanonWord> getWordsForms() {
        return wordsForms;
    }

    private ObservableList<CanonWord> wordsForms = FXCollections.observableArrayList();


    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField symbolField;

    @FXML
    private Button searchButton;

    @FXML
    private RadioButton beginButton;

    @FXML
    private ToggleGroup buttonGroup;

    @FXML
    private RadioButton endButton;

    @FXML
    private RadioButton containButton;

    @FXML
    private TableView<CanonWord> dictView;

    @FXML
    private TableColumn<CanonWord, String> wordCol;

    @FXML
    private TableColumn<CanonWord, String> tagCol;

    @FXML
    private TableColumn<CanonWord, Integer> quantityCol;


    @FXML
    public void searchBySymbols(ActionEvent event) {
        String str;
        str = symbolField.getText();
        ObservableList<CanonWord> list = FXCollections.observableArrayList();

        if(beginButton.isSelected()){
            for (int i = 0; i < wordsForms.size(); i++) {
                if ((wordsForms.get(i).getWord().startsWith(str))) {
                    list.add(wordsForms.get(i));
                }
            }
        }

        if(endButton.isSelected()) {
            for (int i = 0; i < wordsForms.size(); i++) {
                if ((wordsForms.get(i).getWord().endsWith(str))) {
                    list.add(wordsForms.get(i));
                }
            }
        }

        if(containButton.isSelected()) {
            for (int i = 0; i < wordsForms.size(); i++) {
                if ((wordsForms.get(i).getWord().contains(str))) {
                    list.add(wordsForms.get(i));
                }
            }
        }
        dictView.setItems(list);
    }


    @FXML
    void initialize() {

        dictView.setItems(wordsForms);
        dictView.setEditable(true);

        String str = getCurrentString();
        FileInputStream fi = null;
        try {
            fi = new FileInputStream("DictionaryFull.txt");
            Scanner scanner = new Scanner(fi);
            while(scanner.hasNextLine()){
                String content = scanner.nextLine();
                String[] info = content.split("] ", 2);
                String[] infoParts = info[0].split(" ");


                if (infoParts[0].equals(str)){
                    if (info.length < 2) break;
                    String[] forms = info[1].split(" ");
                    for (int i = 0; i < forms.length - 2; i+=3){
                        CanonWord word = new CanonWord(forms[i].substring(1, forms[i].length() - 1),
                                forms[i+1].substring(0, forms[i+1].length() - 1),
                                Integer.parseInt(forms[i+2].substring(0, forms[i+2].length() - 2)));
                        wordsForms.add(word);
                    }
                    break;
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        wordCol.setCellValueFactory(new PropertyValueFactory<>("word"));
        tagCol.setCellValueFactory(new PropertyValueFactory<>("tag"));
        quantityCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));


        dictView.setItems(wordsForms);

        dictView.setRowFactory(event ->{
            TableRow<CanonWord> row = new TableRow<>();
            row.setOnMouseClicked(action ->{
                if (action.getClickCount() == 2 && !row.isEmpty()){
                    String word = row.getItem().getWord();
                    String tags = row.getItem().getTag();
                    Integer quantity = row.getItem().getQuantity();
                    JTextField editWord = new JTextField(word);
                    JTextField editTags = new JTextField(tags);
                    Object[] message = {
                            "Word: ", editWord,
                            "Tag: ", editTags
                    };
                    int result = JOptionPane.showConfirmDialog(null, message, "Redactor", JOptionPane.OK_CANCEL_OPTION);
                    if (result == JOptionPane.OK_OPTION && !editWord.getText().equals("") && !editTags.getText().equals("")){
                        for (CanonWord w: wordsForms){
                            if (w.getWord().equals(editWord.getText())){
                                String content = null;
                                Charset charset = StandardCharsets.UTF_8;
                                Path path = Paths.get("D:\\PrJava\\NLPLab3\\DictionaryFull.txt");
                                try {
                                    content = new String(Files.readAllBytes(path), charset);
                                    String str1 = "(" + w.getWord() + ", " + w.getTag();
                                    String str2 = "(" + w.getWord() + ", " + editTags.getText();
                                    content = content.replace(str1, str2);
                                    Files.write(path, content.getBytes(charset));
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                w.setTag(editTags.getText());
                            }
                        }
                    }
                }
            });
            return row;
        });

    }

}
