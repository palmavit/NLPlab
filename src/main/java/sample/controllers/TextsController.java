package sample.controllers;

//import java.awt.*;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import opennlp.tools.lemmatizer.DictionaryLemmatizer;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.tokenize.WhitespaceTokenizer;


class Par implements Comparable {
    private double d;
    private String str;

    public Par(double d, String str){
        this.d = d;
        this.str = str;
    }

    public double getD() {
        return d;
    }

    public void setD(double d) {
        this.d = d;
    }

    public String getStr() {
        return str;
    }

    public void setStr(String str) {
        this.str = str;
    }

    @Override
    public int compareTo(Object o) {
        if (this == o) return 0;
        if (o == null || getClass() != o.getClass()) return -1;
        Par par = (Par) o;
        if (this.d > par.d) return -1;
        else if (this.d < par.d) return 1;
        else return 0;
    }
}


class Pr implements Comparable{

    private int i1;
    private int i2;

    public Pr(int i1, int i2) {
        this.i1 = i1;
        this.i2 = i2;
    }

    public int getI1() {
        return i1;
    }

    public void setI1(int i1) {
        this.i1 = i1;
    }

    public int getI2() {
        return i2;
    }

    public void setI2(int i2) {
        this.i2 = i2;
    }

    @Override
    public int compareTo(Object o) {
        if (this == o) return 0;
        if (o == null || getClass() != o.getClass()) return -1;
        Pr pr = (Pr) o;
        if (this.i1 < pr.i1) return -1;
        else if (this.i1 > pr.i1) return 1;
        else return 0;
    }

}


public class TextsController {

    private static String currentPath;
    private static boolean iswritten;
    private static int lastIndex;
    private static String prevStr;



    @FXML
    private Tab tabEdit;

    @FXML
    private TextArea texts;

    @FXML
    private Button applyButton;

    @FXML
    private TextArea textArea;

    @FXML
    private Button chooseButton;

    @FXML
    private Button goButton;

    @FXML
    private Button saveButton;

    @FXML
    private Button startButton;

    @FXML
    private TextField symbolField;

    @FXML
    private Button searchButton;

    @FXML
    private Tab tabTag;

    @FXML
    private ImageView pict;

    @FXML
    private TextArea keywords;

    @FXML
    private TextArea fileName;

    @FXML
    private Button searchKeyButton;

    @FXML
    private Button openButton;

    @FXML
    private TextFlow tFlow;




    @FXML
    void open(ActionEvent event) {
        String str = fileName.getText();
        String name = "D:\\PrJava\\NLPLab3\\src\\main\\resources\\assets\\textfiles\\" + str;
        File file = new File(name);
        if (!file.exists()) {
            textArea.setText("Choose the file");
            tFlow.getChildren().add(new Text("Choose th file"));
            return;
        }
        currentPath = file.getPath();
        iswritten = true;

        Path path = Paths.get(name);
        Charset charset = StandardCharsets.UTF_8;
        String content = null;
        try {
            content = new String(Files.readAllBytes(path), charset);
            textArea.setText(content);

            ArrayList<Pr> prl = new ArrayList<>();
            String keystr = keywords.getText();
            String[] keys = keystr.split("\n");
            for (String key : keys){
                int lastidx = 0;
                while (lastidx != -1){
                    lastidx = content.indexOf(" " + key + " ", lastidx + 1);
                    prl.add(new Pr(lastidx, lastidx + key.length()));
                }
                Collections.sort(prl);
                prl.remove(0);
            }

            int num = 0;
            for (Pr pr: prl){
                if (num > content.length() - 2) return;
                String s = content.substring(num, pr.getI1());
                Text txt1 = new Text(s);
                Text txt2 = new Text(content.substring(pr.getI1(), pr.getI2() + 1));
                txt2.setFill(Color.BLUE);
                tFlow.getChildren().addAll(txt1, txt2);
                num = pr.getI2() + 1;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    void searchByKeywords(ActionEvent event) {

        InputStream inputStream = null;
        POSTaggerME tagger = null;
        WhitespaceTokenizer simpleTokenizer = WhitespaceTokenizer.INSTANCE;
        DictionaryLemmatizer lemmatizer = null;

        try {
            inputStream = new
                    FileInputStream("D:\\PrJava\\NLPLab3\\src\\main\\resources\\models\\en-pos-maxent.bin");
            POSModel model = new POSModel(inputStream);
            tagger = new POSTaggerME(model);
            simpleTokenizer = WhitespaceTokenizer.INSTANCE;
            InputStream dictLemmatizer = new FileInputStream("D:\\PrJava\\NLPLab3\\src\\main\\resources\\models\\en-lemmatizer.dict");
            lemmatizer = new DictionaryLemmatizer(
                    dictLemmatizer);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String wordList = keywords.getText();
        String[] tokens = simpleTokenizer.tokenize(wordList);
        String[] tags = tagger.tag(tokens);
        String[] lemmas = lemmatizer.lemmatize(tokens, tags);

        List<Par> list = new ArrayList<>();
        FileInputStream fi = null;
        try {
            fi = new FileInputStream("D:\\PrJava\\NLPLab3\\src\\main\\resources\\assets\\texts.txt");
            Scanner scanner = new Scanner(fi);
            while (scanner.hasNextLine()){
                String line = scanner.nextLine();
                if (line.equals(""))
                    continue;
                String file = line.substring(0, line.indexOf('|'));
                list.add(new Par(0.0, file));
            }
            list.remove("");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        for (int j = 0; j < list.size(); j++){
            File file = new File(list.get(j).getStr());
            String str1 = file.getPath();
            String str2 = file.getName();
            String new_file = str1.substring(0, str1.length() - str2.length());
            new_file += "clearedTexts\\" + str2.substring(0, str2.length() - 4) + "_clear.txt";


            try{
                fi = new FileInputStream(new_file);
                Scanner sc= new Scanner(fi);
                while (sc.hasNextLine()){
                    String str = sc.nextLine();
                    String[] strs = str.split(" ");

                    for (int i = 0; i < tokens.length; i++){
                        if (strs[0].equals(tokens[i]) || strs[0].equals(lemmas[i])){
                            Par pr = list.get(j);
                            pr.setD(pr.getD() + Double.parseDouble(strs[2]));
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Collections.sort(list);
        StringBuilder sb = new StringBuilder("");
        for (Par p : list){
            if (p.getD() == 0.0) break;
            File file = new File(p.getStr());
            String str1 = file.getName();
            sb.append(str1);
            sb.append("     --     ");
            sb.append(p.getD());
            sb.append("\n");
        }
        if (sb.toString().equals("")) texts.setText("No matches");
        else texts.setText(sb.toString());
    }



    @FXML
    void applyChanges(ActionEvent event) {
        if (check()) return;
        Charset charset = StandardCharsets.UTF_8;
        String content = textArea.getText();
        Path path = Paths.get(currentPath);
        try {
            Files.write(path, content.getBytes(charset));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    @FXML
    void chooseText(ActionEvent event) {
        lastIndex = 0;
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File("D:\\PrJava\\NLPLab3\\src\\main\\resources\\assets\\textfiles"));
        File file = fileChooser.showOpenDialog((Stage)applyButton.getScene().getWindow());
        if (file == null) {
            if (iswritten) return;
            iswritten = true;
            textArea.setText("Choose file");
            return;
        }
        currentPath = file.getPath();


        try {
            Charset charset = StandardCharsets.UTF_8;
            Path path = Paths.get(currentPath);
            String content = new String(Files.readAllBytes(path), charset);
            textArea.setText(content);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    @FXML
    public void searchBySymbols(ActionEvent event) {
        if (check()) return;
        String str;
        str = symbolField.getText();
        if (!prevStr.equals(str)){
            lastIndex = 0;
            prevStr = str;
        }
        if (lastIndex == -1) return;
        String content = textArea.getText();
        lastIndex = content.indexOf(str, lastIndex);
        if (lastIndex == -1) return;
        //textArea.positionCaret(lastIndex);
        textArea.selectRange(lastIndex, lastIndex + str.length());
        lastIndex++;
    }


    @FXML
    void initialize() {
        currentPath = null;
        iswritten = false;
        prevStr = "";
        lastIndex = 0;
    }


    private boolean check(){
        if (currentPath == null) {
            if (iswritten) return true;
            Text noFile = new Text("Choose file");
            textArea.setText(noFile.getText());
            iswritten = true;
            return true;
        }
        return false;
    }

}
