package sample.controllers;

import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;
import javafx.stage.Stage;



public class TagTextsController {

    private static String currentPath;
    private static boolean iswritten;
    private static int num;
    private static HashMap<String, Color> tags;
    private static int idx;
    private static int lastIndex;
    private static String[] strings;
    private static String prevStr;

    public static HashMap<String, Color> getTags() {
        return tags;
    }

    @FXML
    private Tab tabEdit;

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
    void go(ActionEvent event) {
        if (check()) return;
        try {
            FileInputStream fi = new FileInputStream("D:\\PrJava\\NLPLab3\\src\\main\\resources\\assets\\texts.txt");
            Scanner scanner = new Scanner(fi);

            while (scanner.hasNextLine()){
                String line = scanner.nextLine();
                if (line.isEmpty())
                    continue;

                String[] parsed = line.split("\\| ");
                File file1 = new File(currentPath);
                String str1 = file1.getName().replace("_tag_right", "");
                File file2 = new File(parsed[0]);
                String str2 = file2.getName();
                if (!str1.equals(str2)) continue;
                if (num >= parsed.length) return;
                textArea.positionCaret(Integer.parseInt(parsed[num]));
                num++;
                return;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    void save(ActionEvent event) {
        if (check()) return;
        int d = textArea.getCaretPosition();
        try {
            FileInputStream fi = new FileInputStream("D:\\PrJava\\NLPLab3\\src\\main\\resources\\assets\\texts.txt");
            Scanner scanner = new Scanner(fi);
            StringBuilder sb = new StringBuilder();
            File currentFile = new File(currentPath);
            String str2 = currentFile.getName().replace("_tag_right", "");
            while (scanner.hasNextLine()){
                String line = scanner.nextLine();
                if (line.isEmpty())
                    continue;

                String[] parsed = line.split("\\| ");
                for (int i = 0; i < parsed.length - 1; i++) sb.append(parsed[i] + "| ");

                File file = new File(parsed[0]);
                String str3 = file.getName();
                if (str2.equals(str3)){
                    sb.append(parsed[parsed.length - 1] + "| ");
                    sb.append(d);
                }
                else sb.append(parsed[parsed.length - 1]);
                sb.append("\n");

            }
            FileWriter fileWriter = new FileWriter("D:\\PrJava\\NLPLab3\\src\\main\\resources\\assets\\texts.txt");
            fileWriter.write(sb.toString());
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    void start(ActionEvent event) {
        num = 1;
        lastIndex = 0;
        go(event);
    }


    @FXML
    void chooseText(ActionEvent event) {
        idx = 0;
        lastIndex = 0;
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File("D:\\PrJava\\NLPLab3\\src\\main\\resources\\assets\\textfiles\\tagRightTexts"));
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
            strings = content.split(" ");
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
        textArea.selectRange(lastIndex, lastIndex + str.length());
        lastIndex++;
    }


    @FXML
    void initialize() {
        currentPath = null;
        iswritten = false;
        prevStr = "";
        num = 1;
        idx = 0;
        lastIndex = 0;

        colors();
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

    public static HashMap<String, Color> colors(){
        tags = new HashMap<>();
        tags.put("CC", Color.ORANGE);
        tags.put("CD", Color.TURQUOISE);
        tags.put("DT", Color.OLIVE);
        tags.put("EX", Color.BEIGE);
        tags.put("FW", Color.BISQUE);
        tags.put("IN", Color.MAROON);
        tags.put("JJ", Color.BLUE);
        tags.put("JJR", Color.BLUEVIOLET);
        tags.put("JJS", Color.BROWN);
        tags.put("LS", Color.BURLYWOOD);
        tags.put("MD", Color.CADETBLUE);
        tags.put("NN", Color.CHARTREUSE);
        tags.put("NNS", Color.CHOCOLATE);
        tags.put("NNP", Color.CORAL);
        tags.put("NNPS", Color.CORNFLOWERBLUE);
        tags.put("PDT", Color.CORNSILK);
        tags.put("POS", Color.CRIMSON);
        tags.put("PRP", Color.CYAN);
        tags.put("PRP$", Color.DARKBLUE);
        tags.put("RB", Color.DARKCYAN);
        tags.put("RBR", Color.DARKGOLDENROD);
        tags.put("RBS", Color.DEEPPINK);
        tags.put("RP", Color.FIREBRICK);
        tags.put("SYM", Color.FORESTGREEN);
        tags.put("TO", Color.ORCHID);
        tags.put("UH", Color.GOLD);
        tags.put("VB", Color.GREEN);
        tags.put("VBD", Color.PERU);
        tags.put("VBG", Color.GRAY);
        tags.put("VBN", Color.NAVY);
        tags.put("VBP", Color.HOTPINK);
        tags.put("VBZ", Color.INDIANRED);
        tags.put("WDT", Color.INDIGO);
        tags.put("WP", Color.IVORY);
        tags.put("WP$", Color.KHAKI);
        tags.put("WRB", Color.PALEGREEN);
        return tags;
    }
}
