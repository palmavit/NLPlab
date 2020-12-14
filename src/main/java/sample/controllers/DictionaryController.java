package sample.controllers;

import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Scanner;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import sample.entities.DictionaryWriter;
import sample.entities.Word;


import javax.swing.*;

import static sample.controllers.ZeroController.jumpToWaitingNoResizableStage;
import static sample.controllers.ZeroController.jumpToWaitingStage;


public class DictionaryController {

    private ObservableList<Word> wordList = FXCollections.observableArrayList();
    private static int wordsAmount;
    private static int i;
    private static String currentString;
    private static String newString;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Label uniqueWordsLabel;

    @FXML
    private Label wordsLabel;

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
    private TextField symbolField;


    @FXML
    private Button deleteButton;

    @FXML
    private TableView<Word> dictView;

    @FXML
    private TableColumn<Word, String> wordCol;

    @FXML
    private TableColumn<Word, Integer> quantityCol;



    @FXML
    void initialize() {

        i = 0;
        try {
            initWordList();
        } catch (IOException e) {
            e.printStackTrace();
        }

        wordCol.setCellValueFactory(new PropertyValueFactory<>("word"));
        quantityCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        dictView.setItems(wordList);
        dictView.setEditable(true);


        dictView.setRowFactory(event ->{
            TableRow<Word> row = new TableRow<>();
            row.setOnMouseClicked(action ->{
                if (action.getClickCount() == 2 && !row.isEmpty()){
                    currentString = row.getItem().getWord();
                    try {
                        jumpToWaitingNoResizableStage("/stages/canonInfo.fxml");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                if (action.isControlDown() && !row.isEmpty()){
                    changeWord();
                }
            });

            return row;
        });

        newString = null;
        uniqueWordsLabel.setText(String.valueOf(wordList.size()));
        wordsLabel.setText(String.valueOf(wordsAmount));
    }


    @FXML
    public void searchBySymbols(ActionEvent event) {
        String str;
        str = symbolField.getText();
        ObservableList<Word> list = FXCollections.observableArrayList();

        if(beginButton.isSelected()){
            for (int i = 0; i < wordList.size(); i++) {
                if ((wordList.get(i).getWord().startsWith(str))) {
                    list.add(wordList.get(i));
                }
            }
        }

        if(endButton.isSelected()) {
            for (int i = 0; i < wordList.size(); i++) {
                if ((wordList.get(i).getWord().endsWith(str))) {
                    list.add(wordList.get(i));
                }
            }
        }

        if(containButton.isSelected()) {
            for (int i = 0; i < wordList.size(); i++) {
                if ((wordList.get(i).getWord().contains(str))) {
                    list.add(wordList.get(i));
                }
            }
        }
        uniqueWordsLabel.setText(String.valueOf(list.size()));
        dictView.setItems(list);
    }


    @FXML
    public void deleteWord(){
        i = 2;
        Word wordSelected = dictView.getSelectionModel().getSelectedItem();
        currentString = wordSelected.getWord();
        try {
            jumpToWaitingStage("/stages/dialogOnChangeNLP.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
        int a = AlertOnChange.getIndexChangeButton();
        if (a == 0) return;

        wordList.remove(wordSelected);

        if (a == 2 || a == 3){
            DictionaryWriter.write(wordList);
        }

        if (a == 3){
            Charset charset = StandardCharsets.UTF_8;
            ArrayList<String> texts = DeveloperController.getTexts();

            for (String filepath: texts){
                Path path = Paths.get(filepath);
                String content = null;
                try {
                    content = new String(Files.readAllBytes(path), charset);
                    String patternString = "\\b"+wordSelected.getWord()+"\\b";
                    content = content.replaceAll(patternString, "");
                    Files.write(path, content.getBytes(charset));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        DeveloperController.dictionary.clear();
        for (Word w : wordList){
            DeveloperController.dictionary.put(w.getWord(), w);
        }
        dictView.setItems(wordList);
        uniqueWordsLabel.setText(String.valueOf(wordList.size()));
        System.out.println("Changes applied\n");
        newString = null;
    }



    public void changeWord(){
        i = 3;
        Word wordSelected = dictView.getSelectionModel().getSelectedItem();
        currentString = wordSelected.getWord();
        JTextField editWord = new JTextField(wordSelected.getWord());
        Object[] message = {
                "Word: ", editWord,
        };
        int result = JOptionPane.showConfirmDialog(null, message, "Redactor", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION && !editWord.getText().equals("")) {

            String str2 = editWord.getText();
            newString = str2;
            Word word = null;
            int c = 2;
            for (Word w : wordList){
                if (w.getWord().equals(str2)){
                    try {
                        jumpToWaitingStage("/stages/dialogExistNLP.fxml");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    c = AlertOnExist.getIndexExistButton();
                    word = w;
                    break;
                }
            }

            if (c == 0) return;

            try {
                jumpToWaitingStage("/stages/dialogOnChangeNLP.fxml");
            } catch (IOException e) {
                e.printStackTrace();
            }

            int a = AlertOnChange.getIndexChangeButton();
            if (a == 0) return;

            if (c == 1) word.setQuantity(word.getQuantity() + wordSelected.getQuantity());
            if (c == 2) {
                Word w = new Word(str2, wordSelected.getQuantity());
                w.setTags("?");
                wordList.add(w);
            }

            wordList.remove(wordSelected);

            if (a == 2 || a == 3){
                DictionaryWriter.write(wordList);
            }
            if (a == 3){
                Charset charset = StandardCharsets.UTF_8;
                ArrayList<String> texts = DeveloperController.getTexts();

                for (String filepath: texts){
                    Path path = Paths.get(filepath);
                    String content = null;
                    try {
                        content = new String(Files.readAllBytes(path), charset);
                        String patternString = "\\b" + currentString + "\\b";
                        content = content.replaceAll(patternString, str2);
                        Files.write(path, content.getBytes(charset));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }


            uniqueWordsLabel.setText(String.valueOf(wordList.size()));
            DeveloperController.dictionary.clear();
            for (Word w : wordList){
                DeveloperController.dictionary.put(w.getWord(), w);
            }
            dictView.setItems(wordList);

        }
        newString = null;
        System.out.println("Changes applied\n");
    }


    private void initWordList() throws IOException {

        File file = new File("D:\\PrJava\\NLPLab3\\DictionaryFull.txt");
        FileInputStream fi = new FileInputStream(file.getPath());
        Scanner scanner = new Scanner(fi);

        System.out.println("Please wait");
        int q = 0;
        wordsAmount = 0;
        while(scanner.hasNextLine()){
            String line = scanner.nextLine();

            String[] s = line.split("]");
            String[] ss = s[0].split(" \\[");
            String[] sss = ss[0].split(" ");
            Integer t = Integer.parseInt(sss[1]);
            Word w = new Word(sss[0], q = t);
            w.setTags(ss[1]);

            if (s.length < 2) continue;
            String[] s1 = s[1].split("\\);");
            for (String s11 : s1){
                String[] forms = s11.split(", ");
                w.addForm(forms[0].substring(2), forms[1]);
            }

            wordList.add(w);
            wordsAmount += q;

        }

        DeveloperController.dictionary.clear();
        for (Word w : wordList){
            DeveloperController.dictionary.put(w.getWord(), w);
        }
        newString = null;
        System.out.println("Process Finished\n");
    }



    public ObservableList<Word> getWordList(){
        return wordList;
    }

    public static int getI() {
        return i;
    }

    public static String getCurrentString() {
        return currentString;
    }

    public static String getNewString() {
        return newString;
    }
}
