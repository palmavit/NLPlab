package sample.controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Scanner;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import sample.entities.BigrammQuantity;
import sample.entities.TagQuantity;
import sample.entities.Word;
import sample.entities.WordTag;


public class Statistics {

    private ObservableList<TagQuantity> tagList = FXCollections.observableArrayList();
    private ObservableList<BigrammQuantity> bigrammList = FXCollections.observableArrayList();
    private ObservableList<WordTag> wordTagList = FXCollections.observableArrayList();

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;


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
    private TextField textBi;

    @FXML
    private RadioButton firstButton;

    @FXML
    private ToggleGroup groupFirstSecond;

    @FXML
    private RadioButton secondButton;


    @FXML
    void initialize() {

        try {
            initTagList();
            initBigrammList();
            initWordTagList();
        } catch (IOException e) {
            e.printStackTrace();
        }

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

    }


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


    private void initWordTagList() throws IOException{
        File file = new File("D:\\PrJava\\NLPLab3\\wordTagStat.txt");
        FileInputStream fi = new FileInputStream(file.getPath());
        Scanner scanner = new Scanner(fi);

        while(scanner.hasNextLine() && scanner.hasNext()){
            String str = scanner.nextLine();
            String[] strs = str.split(" ");
            if (strs.length < 2) continue;
            wordTagList.add(new WordTag(strs[0], Integer.parseInt(strs[1])));
        }
    }

    private void initBigrammList() throws IOException{
        File file = new File("D:\\PrJava\\NLPLab3\\bigrammStat.txt");
        FileInputStream fi = new FileInputStream(file.getPath());
        Scanner scanner = new Scanner(fi);

        while(scanner.hasNextLine() && scanner.hasNext()){
            String str = scanner.nextLine();
            String[] strs = str.split("--");
            if (strs.length < 2) continue;
            bigrammList.add(new BigrammQuantity(strs[0], Integer.parseInt(strs[1])));
        }
    }

    private void initTagList() throws IOException{
        File file = new File("D:\\PrJava\\NLPLab3\\tagStat.txt");
        FileInputStream fi = new FileInputStream(file.getPath());
        Scanner scanner = new Scanner(fi);

        while(scanner.hasNextLine() && scanner.hasNext()){
            tagList.add(new TagQuantity(scanner.next(), scanner.nextInt()));
        }
    }

}
