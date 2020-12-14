package sample.controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Pattern;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import sample.entities.DictionaryWriter;
import sample.entities.HelpWord;
import sample.entities.Pair;
import sample.entities.Word;


import javax.swing.*;

import static sample.controllers.ZeroController.jumpToWaitingStage;

public class TagDictionaryController {

    private ObservableList<HelpWord> wordsTagged = FXCollections.observableArrayList();

    public static String getNewString() {
        return newString;
    }

    private static String newString;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;


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
    private TableView<HelpWord> dictView;

    @FXML
    private TableColumn<HelpWord, String> wordCol;

    @FXML
    private TableColumn<HelpWord, String> tagCol;



    @FXML
    void initialize() {

        try {
            initWordList();
        } catch (IOException e) {
            e.printStackTrace();
        }

        newString = null;
        wordCol.setCellValueFactory(new PropertyValueFactory<>("word"));
        tagCol.setCellValueFactory(new PropertyValueFactory<>("tags"));

        dictView.setItems(wordsTagged);
        dictView.setEditable(true);
        wordCol.setCellFactory(TextFieldTableCell.forTableColumn());

        dictView.setRowFactory(event ->{
            TableRow<HelpWord> row = new TableRow<>();
            row.setOnMouseClicked(action ->{
                if (action.getClickCount() == 2 && !row.isEmpty()){
                    String word = row.getItem().getWord();
                    String tags = row.getItem().getTags();
                    JTextField editWord = new JTextField(word);
                    JTextField editTags = new JTextField(tags);
                    Object[] message = {
                            "Word: ", editWord,
                            "Tags: ", editTags
                    };
                    int result = JOptionPane.showConfirmDialog(null, message, "Redactor", JOptionPane.OK_CANCEL_OPTION);
                    if (result == JOptionPane.OK_OPTION && !editWord.getText().equals("") && !editTags.getText().equals("")){
                        for (HelpWord w: wordsTagged){
                            if (w.getWord().equals(editWord.getText())){
                                String content = null;
                                Charset charset = StandardCharsets.UTF_8;
                                Path path = Paths.get("D:\\PrJava\\NLPLab3\\DictionaryFull.txt");
                                try {
                                    String str3 = editTags.getText();
                                    w.setTags(str3);
                                    HashMap<String, Word> map = DeveloperController.getDictionary();
                                    Word curWord = map.get(w.getWord());
                                    curWord.setTags(str3);
                                    FileWriter fileWriter = new FileWriter("DictionaryFull.txt");
                                    for (Map.Entry e : map.entrySet()) {
                                        fileWriter.write(e.getKey() + " " + ((Word)e.getValue()).getQuantity() + " " + ((Word)e.getValue()).getTags().toString());
                                        ArrayList<Pair> pairs = ((Word)e.getValue()).getForms();
                                        for (Pair p : pairs){
                                            fileWriter.write(" (" + p.toString() + ", ");
                                            Word wd = map.get(p.getWord());
                                            if (wd != null) fileWriter.write(Integer.toString(wd.getQuantity()));
                                            else fileWriter.write("0");
                                            fileWriter.write(");");
                                        }
                                        fileWriter.write("\n");
                                    }
                                    fileWriter.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                w.setTags(editTags.getText());
                            }
                        }
                    }
                }
            });
            return row;
        });

    }


    @FXML

    public void searchBySymbols(ActionEvent event) {
        String str;
        str = symbolField.getText();
        ObservableList<HelpWord> list = FXCollections.observableArrayList();

        if(beginButton.isSelected()){
            for (int i = 0; i < wordsTagged.size(); i++) {
                if ((wordsTagged.get(i).getWord().startsWith(str))) {
                    list.add(wordsTagged.get(i));
                }
            }
        }

        if(endButton.isSelected()) {
            for (int i = 0; i < wordsTagged.size(); i++) {
                if ((wordsTagged.get(i).getWord().endsWith(str))) {
                    list.add(wordsTagged.get(i));
                }
            }
        }

        if(containButton.isSelected()) {
            for (int i = 0; i < wordsTagged.size(); i++) {
                if ((wordsTagged.get(i).getWord().contains(str))) {
                    list.add(wordsTagged.get(i));
                }
            }
        }
        dictView.setItems(list);
    }

    public void changeWord(TableColumn.CellEditEvent editedCell){
        HelpWord wordSelected = dictView.getSelectionModel().getSelectedItem();
        String currentString = wordSelected.getWord();
        String str2 = editedCell.getNewValue().toString();
        newString = str2;
        HelpWord word = null;
        int c = 2;
        for (HelpWord w : wordsTagged){
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


        wordsTagged.remove(wordSelected);

        if (a == 2 || a == 3){
            DictionaryWriter.writeHelp(wordsTagged);
        }
        if (a == 3){
            Charset charset = StandardCharsets.UTF_8;
            ArrayList<String> texts = DeveloperController.getTexts();

            for (String filepath: texts){
                Path path = Paths.get(filepath);
                String content = null;
                try {
                    content = new String(Files.readAllBytes(path), charset);
                    String patternString = currentString;
                    content = content.replaceAll(patternString, str2);
                    Files.write(path, content.getBytes(charset));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        DeveloperController.dictionary.clear();
        for (HelpWord hw : wordsTagged){
            DeveloperController.dictionary.put(hw.getWord(), new Word(hw));
        }

        dictView.setItems(wordsTagged);
        newString = null;
        System.out.println("Changes applied\n");
    }


    private void initWordList() throws IOException {

        File file = new File("D:\\PrJava\\NLPLab3\\DictionaryFull.txt");
        FileInputStream fi = new FileInputStream(file.getPath());
        Scanner scanner = new Scanner(fi);

        while(scanner.hasNextLine()) {
            String line = scanner.nextLine();

            String[] s = line.split("]");
            String[] ss = s[0].split(" \\[");
            String[] sss = ss[0].split(" ");
            Integer t = Integer.parseInt(sss[1]);
            HelpWord w = new HelpWord(sss[0], ss[1]);
            w.setQ(t);

            if (s.length < 2) continue;
            String[] s1 = s[1].split("\\);");
            for (String s11 : s1) {
                String[] forms = s11.split(", ");
                w.addForm(forms[0].substring(2), forms[1]);
            }
            wordsTagged.add(w);
        }
    }


    public ObservableList<HelpWord> getWordsTagged(){
        return wordsTagged;
    }


}
