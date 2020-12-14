package sample.controllers;


import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import sample.actors.Developer;
import sample.entities.*;

import javax.swing.*;

import static sample.controllers.ZeroController.jumpToWaitingStage;


public class TextAddController {

    private static boolean wasChanged;

    private static ObservableList<HelpWord> wordsTagged = FXCollections.observableArrayList();
    public static ObservableList<HelpWord> getWordsTagged() {
        return wordsTagged;
    }

    private static ObservableList<Word> wordList = FXCollections.observableArrayList();
    private static ObservableList<TagQuantity> tagList = FXCollections.observableArrayList();
    private static ObservableList<BigrammQuantity> bigrammList = FXCollections.observableArrayList();
    private static ObservableList<WordTag> wordTagList = FXCollections.observableArrayList();

    public static ObservableList<Word> getWordList() {
        return wordList;
    }

    public static ObservableList<TagQuantity> getTagList() {
        return tagList;
    }

    public static ObservableList<BigrammQuantity> getBigrammList() {
        return bigrammList;
    }

    public static ObservableList<WordTag> getWordTagList() {
        return wordTagList;
    }

    private static boolean save;

    public static boolean isSave() {
        return save;
    }

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button searchWordBtnQ;

    @FXML
    private ToggleGroup groupQ;

    @FXML
    private TextField wordFieldQ;

    @FXML
    private RadioButton beginQButton;

    @FXML
    private RadioButton endQButton;

    @FXML
    private RadioButton containQButton;




    @FXML
    private Button searchWordBtnT;

    @FXML
    private ToggleGroup groupT;

    @FXML
    private TextField wordFieldT;

    @FXML
    private RadioButton beginTButton;

    @FXML
    private RadioButton endTButton;

    @FXML
    private RadioButton containTButton;




    @FXML
    private Button searchBiButton;

    @FXML
    private TextField textBi;

    @FXML
    private RadioButton firstButton;

    @FXML
    private ToggleGroup groupFirstSecond;

    @FXML
    private RadioButton secondButton;

    @FXML
    private Button saveButton;



    @FXML
    private TableView<Word> tableQ;
    @FXML
    private TableColumn<Word, String> wordQ;
    @FXML
    private TableColumn<Word, Integer> quantityQ;


    @FXML
    private TableView<HelpWord> tableT;
    @FXML
    private TableColumn<HelpWord, String> wordT;
    @FXML
    private TableColumn<HelpWord, String> tagsT;


    @FXML
    private TableView<TagQuantity> tableTag;
    @FXML
    private TableColumn<TagQuantity, String> tagCol;
    @FXML
    private TableColumn<TagQuantity, Integer> quantTagCol;


    @FXML
    private TableView<BigrammQuantity> tableBigramm;
    @FXML
    private TableColumn<BigrammQuantity, String> bigrammCol;
    @FXML
    private TableColumn<BigrammQuantity, Integer> quantityBiCol;


    @FXML
    private TableView<WordTag> tablePair;
    @FXML
    private TableColumn<WordTag, String> pairCol;
    @FXML
    private TableColumn<WordTag, Integer> quantityPairCol;

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
    private RadioButton tagButton;

    @FXML
    private Button deleteButton;


    @FXML
    void searchBigramms(ActionEvent event) {
        String str;
        str = textBi.getText();
        ObservableList<BigrammQuantity> list = FXCollections.observableArrayList();

        if(firstButton.isSelected()){
            for (BigrammQuantity bigrammQuantity : bigrammList) {
                if ((bigrammQuantity.getBigramm().startsWith(str))) {
                    list.add(bigrammQuantity);
                }
            }
        }

        if(secondButton.isSelected()) {
            for (BigrammQuantity bigrammQuantity : bigrammList) {
                if ((bigrammQuantity.getBigramm().endsWith(str))) {
                    list.add(bigrammQuantity);
                }
            }
        }
        tableBigramm.setItems(list);
    }

    @FXML
    public void searchBySymbols(ActionEvent event) {
        String str;
        str = symbolField.getText();
        ObservableList<WordTag> list = FXCollections.observableArrayList();

        if(beginButton.isSelected()){
            for (int i = 0; i < wordTagList.size(); i++) {
                if ((wordTagList.get(i).getPair().startsWith(str))) {
                    list.add(wordTagList.get(i));
                }
            }
        }

        if(endButton.isSelected()) {
            for (int i = 0; i < wordTagList.size(); i++) {
                String str1 = wordTagList.get(i).getPair();
                String s = str1.split("_")[0];
                if (s.endsWith(str)) {
                    list.add(wordTagList.get(i));
                }
            }
        }

        if(containButton.isSelected()) {
            for (int i = 0; i < wordTagList.size(); i++) {
                if ((wordTagList.get(i).getPair().contains(str))) {
                    list.add(wordTagList.get(i));
                }
            }
        }

        if(tagButton.isSelected()){
            for (int i = 0; i < wordTagList.size(); i++) {
                if ((wordTagList.get(i).getPair().endsWith(str))) {
                    list.add(wordTagList.get(i));
                }
            }
        }

        tablePair.setItems(list);
    }

    @FXML
    void searchWordQ(ActionEvent event) {
        String str = wordFieldQ.getText();
        ObservableList<Word> list = FXCollections.observableArrayList();

        if(beginQButton.isSelected()){
            for (int i = 0; i < wordList.size(); i++) {
                if ((wordList.get(i).getWord().startsWith(str))) {
                    list.add(wordList.get(i));
                }
            }
        }

        if(endQButton.isSelected()) {
            for (int i = 0; i < wordList.size(); i++) {
                if ((wordList.get(i).getWord().endsWith(str))) {
                    list.add(wordList.get(i));
                }
            }
        }

        if(containQButton.isSelected()) {
            for (int i = 0; i < wordList.size(); i++) {
                if ((wordList.get(i).getWord().contains(str))) {
                    list.add(wordList.get(i));
                }
            }
        }
        tableQ.setItems(list);
    }


    @FXML
    void searchWordT(ActionEvent event) {
        String str = wordFieldT.getText();
        ObservableList<HelpWord> list = FXCollections.observableArrayList();

        if(beginTButton.isSelected()){
            for (int i = 0; i < wordsTagged.size(); i++) {
                if ((wordsTagged.get(i).getWord().startsWith(str))) {
                    list.add(wordsTagged.get(i));
                }
            }
        }

        if(endTButton.isSelected()) {
            for (int i = 0; i < wordsTagged.size(); i++) {
                if ((wordsTagged.get(i).getWord().endsWith(str))) {
                    list.add(wordsTagged.get(i));
                }
            }
        }

        if(containTButton.isSelected()) {
            for (int i = 0; i < wordsTagged.size(); i++) {
                if ((wordsTagged.get(i).getWord().contains(str))) {
                    list.add(wordsTagged.get(i));
                }
            }
        }
        tableT.setItems(list);
    }


    @FXML
    void save(ActionEvent event) throws IOException {
        save = true;
        tagButton.getScene().getWindow().hide();
        //ZeroController.jumpToWaitingStage("/stages/developerNLPPage.fxml");
    }


    @FXML
    public void delete(){
        HelpWord wordSelected = tableT.getSelectionModel().getSelectedItem();
        wordsTagged.remove(wordSelected);
        for (Word w : wordList){
            if (w.getWord().equals(wordSelected.getWord())){
                wordList.remove(w);
                break;
            }
        }
    }



    @FXML
    void initialize() {
        tagList.clear();
        bigrammList.clear();
        wordTagList.clear();
        wordList.clear();
        wordsTagged.clear();
        initTagList();
        initBigrammList();
        initWordTagList();
        initWordListQ();
        initWordsTaggedT();

        save = false;
        wasChanged = false;

        wordQ.setCellValueFactory(new PropertyValueFactory<>("word"));
        quantityQ.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        tableQ.setEditable(true);
        tableQ.setItems(wordList);

        wordT.setCellValueFactory(new PropertyValueFactory<>("word"));
        tagsT.setCellValueFactory(new PropertyValueFactory<>("tags"));
        tableT.setEditable(true);
        tableT.setItems(wordsTagged);

        tagCol.setCellValueFactory(new PropertyValueFactory<TagQuantity, String>("tag"));
        quantTagCol.setCellValueFactory(new PropertyValueFactory<TagQuantity, Integer>("quantity"));
        tableTag.setEditable(true);
        tableTag.setItems(tagList);

        bigrammCol.setCellValueFactory(new PropertyValueFactory<BigrammQuantity, String>("bigramm"));
        quantityBiCol.setCellValueFactory(new PropertyValueFactory<BigrammQuantity, Integer>("quantity"));
        tableBigramm.setEditable(true);
        tableBigramm.setItems(bigrammList);

        pairCol.setCellValueFactory(new PropertyValueFactory<WordTag, String>("pair"));
        quantityPairCol.setCellValueFactory(new PropertyValueFactory<WordTag, Integer>("quantity"));
        tablePair.setEditable(true);
        tablePair.setItems(wordTagList);




        tableT.setRowFactory(event ->{
            TableRow<HelpWord> row = new TableRow<>();
            row.setOnMouseClicked(action ->{

                if (action.getClickCount() == 2 && !row.isEmpty()){
                    HelpWord wordSelected = tableT.getSelectionModel().getSelectedItem();
                    String oldval = wordSelected.getWord();
                    JTextField editWord = new JTextField(wordSelected.getWord());
                    JTextField editTags = new JTextField(wordSelected.getTags());
                    Object[] message = {
                            "Word: ", editWord,
                            "Tags: ", editTags
                    };
                    int result = JOptionPane.showConfirmDialog(null, message, "Redactor", JOptionPane.OK_CANCEL_OPTION);
                    if (result == JOptionPane.OK_OPTION && !editWord.getText().equals("") && !editTags.getText().equals("")) {

                        wordList.removeIf(word -> word.getWord().equals(wordSelected.getWord()));
                        wordsTagged.remove(wordSelected);
                        wordSelected.setWord(editWord.getText());
                        wordSelected.setTags(editTags.getText());

                        boolean b1 = true;
                        for (HelpWord hw : wordsTagged){
                            if (hw.getWord().equals(editWord.getText())){
                                hw.join(wordSelected);
                                b1 = false;
                                break;
                            }
                        }
                        if (b1) {
                            wordsTagged.add(wordSelected);
                            wasChanged = true;
                        }



                        boolean b2 = true;
                        for (Word w : wordList){
                            if (w.getWord().equals(editWord.getText())){
                                w.join(new Word(wordSelected));
                                b2 = false;
                                break;
                            }
                        }
                        if (b2) wordList.add(new Word(wordSelected));

                    }

                    Object[] message1 = {
                            "Would you like to add changes\n" +
                                    "to the current text file?"
                    };
                    int result1 = JOptionPane.showConfirmDialog(null, message1, "Redactor", JOptionPane.OK_CANCEL_OPTION);
                    if (result1 == JOptionPane.OK_OPTION ) {
                        String str = DeveloperController.getNewText();
                        Charset charset = StandardCharsets.UTF_8;
                        ArrayList<String> texts = DeveloperController.getTexts();

                        Path path = Paths.get(str);
                        String content = null;
                        try {
                            content = new String(Files.readAllBytes(path), charset);
                            String patternString = "\\b" + oldval + "\\b";
                            content = content.replaceAll(patternString, wordSelected.getWord());
                            Files.write(path, content.getBytes(charset));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
            ((Stage)searchWordBtnT.getScene().getWindow()).show();
            return row;
        });


    }



    private void initWordListQ() {
        HashMap<String, Word> map = DeveloperController.getNewDict();
        for (Map.Entry e : map.entrySet()){
            wordList.add((Word)e.getValue());
        }
    }

    private void initWordsTaggedT() {
        HashMap<String, Word> map = DeveloperController.getNewDict();
        for (Map.Entry e : map.entrySet()){
            HelpWord helpWord = new HelpWord((Word)e.getValue());
            wordsTagged.add(helpWord);
        }
    }

    private void initWordTagList() {
        HashMap<String, Integer> map = DeveloperController.getNewWordTagstat();
        for (Map.Entry e : map.entrySet()){
            wordTagList.add(new WordTag((String)e.getKey(), (Integer)e.getValue()));
        }
    }

    private void initBigrammList() {
        HashMap<PairTag, Integer> map = DeveloperController.getNewBigrammstat();
        for (Map.Entry e : map.entrySet()){
            bigrammList.add(new BigrammQuantity(e.getKey().toString(), (Integer)e.getValue()));
        }
    }

    private void initTagList() {
        HashMap<String, Integer> map = DeveloperController.getNewTagstat();
        for (Map.Entry e : map.entrySet()){
            tagList.add(new TagQuantity((String)e.getKey(), (Integer)e.getValue()));
        }
    }

}
