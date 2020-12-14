package sample.controllers;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import opennlp.tools.lemmatizer.DictionaryLemmatizer;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSSample;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.tokenize.WhitespaceTokenizer;
import sample.entities.DictionaryWriter;
import sample.entities.HelpWord;
import sample.entities.PairTag;
import sample.entities.Word;


class MyClass extends Task {

    private String filepath;

    public MyClass(String filepath){
        this.filepath = filepath;
    }

    @Override
    protected Object call() throws Exception {
        DeveloperController.spaceText1(filepath);
        this.updateProgress(1, 7);
        DeveloperController.tagText1(filepath);
        this.updateProgress(3, 7);
        DeveloperController.tagTextRight1(filepath);
        this.updateProgress(4, 7);
        DeveloperController.editTagStat(filepath);
        this.updateProgress(5, 7);
        DeveloperController.editBigrammStat(filepath);
        this.updateProgress(6, 7);
        DeveloperController.editPairStat(filepath);
        this.updateProgress(7, 7);
        return null;
    }
}


public class DeveloperController {

    public static HashMap<String, Word> dictionary;
    public static HashMap<String, Integer> tagstat;
    public static HashMap<PairTag, Integer> bigrammstat;
    public static HashMap<String, Integer> wordtagstat;

    private static HashMap<String, Integer> newTagstat;
    private static HashMap<PairTag, Integer> newBigrammstat;
    private static HashMap<String, Integer> newWordTagstat;
    private static HashMap<String, Word> newDict;


    private static HashMap<String, Integer> newLemmaDict;
    private static TreeSet<String> necessaryTags = new TreeSet<>();


    public static HashMap<String, Word> getNewDict() {
        return newDict;
    }

    public static HashMap<String, Integer> getNewTagstat() {
        return newTagstat;
    }

    public static HashMap<PairTag, Integer> getNewBigrammstat() {
        return newBigrammstat;
    }

    public static HashMap<String, Integer> getNewWordTagstat() {
        return newWordTagstat;
    }


    public static int i;
    private static ArrayList<String> texts;
    private static String newText;

    public static String getNewText() {
        return newText;
    }

    @FXML
    private Button viewTaggedTextButton;

    @FXML
    private Button addButton;

    @FXML
    private Button issButton;

    @FXML
    private Button viewTaggedVocabButton;

    @FXML
    private Button viewTagsButton;

    @FXML
    private Button vocabularyButton;

    @FXML
    private Button statButton;

    @FXML
    private Button viewTextsBtn;

    @FXML
    private ProgressBar pb;



    @FXML
    void initialize() {

        dictionary = new HashMap<>();
        tagstat = new HashMap<>();
        bigrammstat = new HashMap<>();
        wordtagstat = new HashMap<>();
        newLemmaDict = new HashMap<>();
        newText = null;
        necessaryTags.addAll(Arrays.asList("CD", "JJ", "JJR", "JJS", "NN", "NNS",
                "NNP", "NNPS", "RB", "RBR", "RBS", "VB", "VBD", "VBG", "VBN", "VBP", "VBZ"));
        pb.setVisible(false);
        i = 0;
        texts = new ArrayList<>();
        FileInputStream fi = null;
        try {
            fi = new FileInputStream("D:\\PrJava\\NLPLab3\\src\\main\\resources\\assets\\texts.txt");
            Scanner scanner = new Scanner(fi);
            while (scanner.hasNextLine()){
                String line = scanner.nextLine();
                if (line.equals(""))
                    continue;
                String file = line.substring(0, line.indexOf('|'));
                texts.add(file);
            }
            texts.remove("");

            fillDictionary();
            fillTagStat();
            fillBigrammStat();
            fillWordTagStat();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }




    @FXML
    void addText(ActionEvent event) throws IOException {

        File outFile = new File("D:\\PrJava\\NLPLab3\\src\\main\\resources\\assets\\texts.txt");
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File("D:\\PrJava\\NLPLab3\\src\\main\\resources\\assets\\textfiles"));
        File file = fileChooser.showOpenDialog((Stage)addButton.getScene().getWindow());
        if (file == null) return;
        pb.setVisible(true);

        newDict = new HashMap<>();
        newBigrammstat = new HashMap<>();
        newTagstat = new HashMap<>();
        newWordTagstat = new HashMap<>();
        newText = file.getPath();

        MyClass cl = new MyClass(file.getPath());
        Thread thread = new Thread(cl);
        pb.progressProperty().unbind();
        pb.progressProperty().bind(cl.progressProperty());


        cl.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED, new EventHandler() {
            @Override
            public void handle(Event event) {
                try {
                    ZeroController.jumpToWaitingStage("/stages/addTextNLPPage.fxml");

                } catch (IOException e) {
                    e.printStackTrace();
                }


                if (TextAddController.isSave()) {
                    FileWriter fileWriter = null;
                    try {
                        fileWriter = new FileWriter(outFile, true);
                        if (!texts.contains(file.getPath())) {
                            texts.add(file.getPath());
                            fileWriter.write("\n");
                            fileWriter.write(file.getPath() + "| 0");
                        }
                        fileWriter.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    join();
                    DictionaryWriter.write(dictionary);
                    writeTagStat();
                    writeBigrammStat();
                    writeWordTagStat();
                }
            }
        });

        thread.start();

    }




    private void writeTagStat() {
        try{
            FileWriter fileWriter = new FileWriter("tagStat.txt");
            for (Map.Entry e : tagstat.entrySet()){
                fileWriter.write(e.getKey().toString() + " " + e.getValue() + "\n");
            }
            fileWriter.close();
        } catch (IOException e){
            e.printStackTrace();
        }

    }

    private void writeBigrammStat() {
        try{
            FileWriter fileWriter = new FileWriter("bigrammStat.txt");
            for (Map.Entry e : bigrammstat.entrySet()){
                fileWriter.write(e.getKey().toString() + "--" + e.getValue() + "\n");
            }
            fileWriter.close();
        } catch (IOException e){
            e.printStackTrace();
        }

    }

    private void writeWordTagStat() {
        try{
            FileWriter fileWriter = new FileWriter("wordTagStat.txt");
            for (Map.Entry e : wordtagstat.entrySet()){
                fileWriter.write(e.getKey().toString() + " " + e.getValue() + "\n");
            }
            fileWriter.close();
        } catch (IOException e){
            e.printStackTrace();
        }

    }


    private void join() {

        ObservableList<HelpWord> words = TextAddController.getWordsTagged();
        for (HelpWord w : words){
            if (dictionary.get(w.getWord()) == null)
                dictionary.put(w.getWord(), new Word(w));
            else
                dictionary.get(w.getWord()).join(new Word(w));
        }


        for (Map.Entry e : newTagstat.entrySet()){
            if (tagstat.get(e.getKey()) == null)
                tagstat.put((String)e.getKey(), (Integer)e.getValue());
            else{
                tagstat.replace((String)e.getKey(), (Integer)e.getValue() + tagstat.get(e.getKey()));
            }
        }

        for (Map.Entry e : newWordTagstat.entrySet()){
            if (wordtagstat.get(e.getKey()) == null)
                wordtagstat.put((String)e.getKey(), (Integer)e.getValue());
            else{
                wordtagstat.replace((String)e.getKey(), (Integer)e.getValue() + wordtagstat.get(e.getKey()));
            }
        }

        for (Map.Entry e : newBigrammstat.entrySet()){
            if (bigrammstat.get(e.getKey()) == null)
                bigrammstat.put((PairTag)e.getKey(), (Integer)e.getValue());
            else{
                bigrammstat.replace((PairTag)e.getKey(), (Integer)e.getValue() + bigrammstat.get(e.getKey()));
            }
        }
    }


    private void fillDictionary() {

        try {
            FileInputStream fi = new FileInputStream("D:\\PrJava\\NLPLab3\\DictionaryFull.txt");
            Scanner scanner = new Scanner(fi);
            String line = null;

            while (scanner.hasNextLine()) {
                line = scanner.nextLine();
                String[] s1 = line.split("]");
                String[] s2 = s1[0].split(", ");
                String[] s3 = s2[0].split(" ");
                Word w = new Word(s3[0], Integer.parseInt(s3[1]));
                //w.setWord(s3[0]);
                //w.setQuantity(Integer.parseInt(s3[1]));
                if (s3.length < 3) continue;
                w.addTag(s3[2].substring(1));
                for (int i = 1; i < s2.length; i++){
                    w.addTag(s2[i]);
                }
                if (s1.length < 2) continue;
                String[] s4 = s1[1].split(";");
                for (String s: s4){
                    String[] ss5 = s.split(", ");
                    w.addForm(ss5[0].substring(2), ss5[1]);
                }
                dictionary.put(w.getWord(), w);
            }
        } catch (IOException e){
            e.printStackTrace();
        }

    }

    private void fillTagStat(){
        try {
            FileInputStream fi = new FileInputStream("D:\\PrJava\\NLPLab3\\tagStat.txt");
            Scanner scanner = new Scanner(fi);
            String line = null;

            while (scanner.hasNextLine()) {
                line = scanner.nextLine();
                if (line.equals("")) break;
                String[] stat = line.split(" ");
                tagstat.put(stat[0], Integer.parseInt(stat[1]));
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    private void fillBigrammStat(){
        try {
            FileInputStream fi = new FileInputStream("D:\\PrJava\\NLPLab3\\bigrammStat.txt");
            Scanner scanner = new Scanner(fi);
            String line = null;

            while (scanner.hasNextLine()) {
                line = scanner.nextLine();
                if (line.equals("")) break;
                String[] stat = line.split("--");
                PairTag pr= new PairTag(stat[0].split(", ")[0], stat[0].split(", ")[1]);
                bigrammstat.put(pr, Integer.parseInt(stat[1]));
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    private void fillWordTagStat(){
        try {
            FileInputStream fi = new FileInputStream("D:\\PrJava\\NLPLab3\\wordTagStat.txt");
            Scanner scanner = new Scanner(fi);
            String line = null;

            while (scanner.hasNextLine()) {
                line = scanner.nextLine();
                if (line.equals("")) break;
                String[] stat = line.split(" ");
                wordtagstat.put(stat[0], Integer.parseInt(stat[1]));
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }


    static void tagTextRight1(String filepath) {
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


        spaceTextRight1(filepath);
        File file = new File(filepath);
        String str1 = file.getPath();
        String str2 = file.getName();
        String new_file = str1.substring(0, str1.length() - str2.length());
        String spaceFilePath = new_file + "spaceRightTexts\\" + str2.substring(0, str2.length() - 4) + "_space_right.txt";
        new_file += "tagRightTexts\\" + str2.substring(0, str2.length() - 4) + "_tag_right.txt";

        try {
            FileInputStream fi = new FileInputStream(spaceFilePath);
            Scanner scanner = new Scanner(fi);
            FileWriter fileWriter = new FileWriter(new_file);
            String line = null;

            while(scanner.hasNextLine()) {
                line = scanner.nextLine();
                String[] tokens = simpleTokenizer.tokenize(line);
                String[] tags = tagger.tag(tokens);
                POSSample sample = new POSSample(tokens, tags);
                fileWriter.write(sample.toString() + "\n");
            }
            fileWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void spaceText1(String filepath) {
        String patternString = "[a-zA-Z]+(-[a-zA-Z]+)?";
        Pattern pattern = Pattern.compile(patternString);
        Matcher matcher;
        File file = new File(filepath);
        String str1 = file.getPath();
        String str2 = file.getName();
        String new_file = str1.substring(0, str1.length() - str2.length());
        new_file += "spaceTexts\\" + str2.substring(0, str2.length() - 4) + "_space.txt";


        try {
            FileWriter fileWriter = new FileWriter(new_file);
            FileInputStream fi = new FileInputStream(str1);
            Scanner scanner = new Scanner(fi);

            while (scanner.hasNextLine()){
                int i = 0;
                String line = scanner.nextLine();
                matcher = pattern.matcher(line);

                while(matcher.find()){
                    String s = matcher.group(0);
                    fileWriter.write(s + " ");
                }

                fileWriter.write( "\n");
            }
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    static void spaceTextRight1(String filepath) {

        String patternString = "[a-zA-Z]+(-[a-zA-Z]+)?";
        Pattern pattern = Pattern.compile(patternString);
        Matcher matcher;

        File file = new File(filepath);
        String str1 = file.getPath();
        String str2 = file.getName();
        String new_file = str1.substring(0, str1.length() - str2.length());
        new_file += "spaceRightTexts\\" + str2.substring(0, str2.length() - 4) + "_space_right.txt";

        try {
            FileWriter fileWriter = new FileWriter(new_file);
            FileInputStream fi = new FileInputStream(str1);
            Scanner scanner = new Scanner(fi);

            while (scanner.hasNextLine()){
                String line = scanner.nextLine();
                matcher = pattern.matcher(line);

                int i = 0;
                while(matcher.find()){
                    String s = matcher.group(0);
                    int start = matcher.start(0) + i;

                    if (start > 0){
                        if (line.charAt(start - 1) != ' '){
                            line = line.substring(0, start) + ' ' + s + line.substring(start + s.length());
                            i ++;
                            start++;
                        }
                    }
                    if (start + s.length() >= line.length()) break;
                    if (line.charAt(start + s.length()) != ' '){
                        line = line.substring(0, start) + s + ' ' + line.substring(start + s.length());
                        i ++;
                    }

                }
                fileWriter.write( line + "\n");
            }
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    static void tagText1(String filepath){
        InputStream inputStream = null;
        POSTaggerME tagger = null;
        WhitespaceTokenizer simpleTokenizer = WhitespaceTokenizer.INSTANCE;
        DictionaryLemmatizer lemmatizer = null;
        newLemmaDict.clear();

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

        File file = new File(filepath);
        String str1 = file.getPath();
        String str2 = file.getName();
        String new_file1 = str1.substring(0, str1.length() - str2.length());
        String spaceFilePath = new_file1 + "spaceTexts\\" + str2.substring(0, str2.length() - 4) + "_space.txt";
        String new_file = new_file1 + "tagTexts\\" + str2.substring(0, str2.length() - 4) + "_tag.txt";
        int n = 1;

        String content = null;
        try {
            FileInputStream fi = new FileInputStream(spaceFilePath);
            Scanner scanner = new Scanner(fi);
            FileWriter fileWriter = new FileWriter(new_file);

            while(scanner.hasNextLine()) {
                content = scanner.nextLine();
                String[] tokens = simpleTokenizer.tokenize(content);
                String[] tags = tagger.tag(tokens);
                String[] lemmas = lemmatizer.lemmatize(tokens, tags);

                POSSample sample = new POSSample(tokens, tags);

                for (int i = 0; i < tags.length; i++){
                    Word w = newDict.get(tokens[i]);
                    if (w != null)
                    {
                        w.setQuantity(w.getQuantity() + 1);
                        if (!w.getTags().contains(tags[i])
                                && Character.isLetter(tags[i].charAt(0))){
                            w.addTag(tags[i]);
                            String[] strs = {lemmas[i]};
                            String[] strtag = tagger.tag(strs);
                            if (!lemmas[i].equals("O")){
                                if (tags[i].contains(strtag[0])) w.addForm(lemmas[i], strtag[0]);
                                else w.addForm(lemmas[i], tags[i]);
                            }
                            else w.addForm(tokens[i], tags[i]);
                        }
                    }

                    else{
                        w = new Word(tokens[i], 1);
                        w.addText(filepath);
                        w.addTag(tags[i]);
                        if (!lemmas[i].equals("O")) w.addForm(lemmas[i], tags[i]);
                        else w.addForm(tokens[i], tags[i]);
                        newDict.put(tokens[i], w);
                    }

                    if (!lemmas[i].equals("O")){
                        if (necessaryTags.contains(tags[i])){

                            Integer l = newLemmaDict.get(lemmas[i]);

                            if (l == null){
                                newLemmaDict.put(lemmas[i], 1);
                            }
                            else{
                                newLemmaDict.replace(lemmas[i], l + 1);
                                n = Math.max(n, l + 1);
                            }
                        }
                    }
                }
                fileWriter.append(sample.toString() + "\n");
            }
            fileWriter.close();

            String clearText = new_file1 + "clearedTexts\\" + str2.substring(0, str2.length() - 4) + "_clear.txt";
            fileWriter = new FileWriter(clearText);
            for (Map.Entry e : newLemmaDict.entrySet()){
                fileWriter.write(e.getKey() + " ");
                int t = ((Integer)e.getValue());
                fileWriter.write(t + " ");
                double d = (double)t / n;
                fileWriter.write(d + "\n");
            }
            fileWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    static void editTagStat(String filepath) {
        HashMap<String, Color> colorTags = TagTextsController.colors();
        WhitespaceTokenizer simpleTokenizer = WhitespaceTokenizer.INSTANCE;
        newTagstat.clear();
        File file = new File(filepath);
        String str1 = file.getPath();
        String str2 = file.getName();
        String new_file = str1.substring(0, str1.length() - str2.length());
        new_file += "tagTexts\\" + str2.substring(0, str2.length() - 4) + "_tag.txt";

        Path path = Paths.get(new_file);
        Charset charset = StandardCharsets.UTF_8;
        String content = null;
        try {
            content = new String(Files.readAllBytes(path), charset);
            String[] tokens = simpleTokenizer.tokenize(content);

            for (String cur_str: tokens){
                String[] word_tag = cur_str.split("_");
                if (word_tag.length < 2) continue;
                String tag = word_tag[1];
                if (colorTags.get(tag) == null) continue;
                Integer l = newTagstat.get(tag);
                if (l == null){
                    newTagstat.put(tag, 1);
                }
                else {
                    newTagstat.replace(tag, l + 1);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void editBigrammStat(String filepath) {
        HashMap<String, Color> colorTags = TagTextsController.colors();
        WhitespaceTokenizer simpleTokenizer = WhitespaceTokenizer.INSTANCE;
        newBigrammstat.clear();
        File file = new File(filepath);
        String str1 = file.getPath();
        String str2 = file.getName();
        String new_file = str1.substring(0, str1.length() - str2.length());
        new_file += "tagTexts\\" + str2.substring(0, str2.length() - 4) + "_tag.txt";

        Path path = Paths.get(new_file);
        Charset charset = StandardCharsets.UTF_8;
        String content = null;
        try {
            content = new String(Files.readAllBytes(path), charset);
            String[] tokens = simpleTokenizer.tokenize(content);

            for (int i = 0; i < tokens.length - 1; i++){
                String[] word_tag1 = tokens[i].split("_");
                String[] word_tag2 = tokens[i + 1].split("_");
                if (word_tag1.length < 2 || word_tag2.length < 2) continue;
                String tag1 = word_tag1[1];
                String tag2 = word_tag2[1];
                if (colorTags.get(tag1) == null || colorTags.get(tag2) == null) continue;
                PairTag pr = new PairTag(tag1, tag2);
                Integer l = null;
                for (Map.Entry e: newBigrammstat.entrySet()){
                    if (e.getKey().equals(pr)) {
                        l = (Integer)e.getValue();
                        pr = (PairTag)e.getKey();
                        break;
                    }
                }
                if (l == null){
                    newBigrammstat.put(pr, 1);
                }
                else {
                    newBigrammstat.replace(pr, l + 1);
                }
            }

           /* FileWriter fileWriter = new FileWriter("bigrammStat.txt");
            for (Map.Entry e : bigrammstat.entrySet()){
                fileWriter.write(e.getKey().toString() + "--" + e.getValue() + "\n");
            }
            fileWriter.close();*/


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void editPairStat(String filepath) {
        HashMap<String, Color> colorTags = TagTextsController.colors();
        WhitespaceTokenizer simpleTokenizer = WhitespaceTokenizer.INSTANCE;
        newWordTagstat.clear();

        File file = new File(filepath);
        String str1 = file.getPath();
        String str2 = file.getName();
        String new_file = str1.substring(0, str1.length() - str2.length());
        new_file += "tagTexts\\" + str2.substring(0, str2.length() - 4) + "_tag.txt";

        Path path = Paths.get(new_file);
        Charset charset = StandardCharsets.UTF_8;
        String content = null;
        try {
            content = new String(Files.readAllBytes(path), charset);
            String[] tokens = simpleTokenizer.tokenize(content);

            for (String cur_str: tokens){
                String[] word_tag = cur_str.split("_");
                if (word_tag.length < 2) continue;
                String tag = word_tag[1];
                if (colorTags.get(tag) == null) continue;
                Integer l = newWordTagstat.get(cur_str);
                if (l == null){
                    newWordTagstat.put(cur_str, 1);
                }
                else {
                    newWordTagstat.replace(cur_str, l + 1);
                }
            }

            /*FileWriter fileWriter = new FileWriter("wordTagStat.txt");
            for (Map.Entry e : wordtagstat.entrySet()){
                fileWriter.write(e.getKey().toString() + " " + e.getValue() + "\n");
            }
            fileWriter.close();*/

        } catch (IOException e) {
            e.printStackTrace();
        }
    }




    public static ArrayList<String> getTexts(){
        return texts;
    }

    public static HashMap<String, Word> getDictionary() {
        return dictionary;
    }

    public static HashMap<String, Integer> getTagstat() {
        return tagstat;
    }

    public static HashMap<PairTag, Integer> getBigrammstat() {
        return bigrammstat;
    }

    public static HashMap<String, Integer> getWordtagstat() {
        return wordtagstat;
    }



    @FXML
    void showVocabulary(ActionEvent event) throws IOException{
        ZeroController.jumpToWaitingStage("/stages/dictionaryNLPPage.fxml");
    }

    @FXML
    void showStatistics(ActionEvent event) throws IOException {
        ZeroController.jumpToWaitingStage("/stages/statsNLPPage.fxml");
    }

    @FXML
    void showTaggedVocabulary(ActionEvent event) throws IOException {
        ZeroController.jumpToWaitingStage("/stages/tagDictionaryNLPPage.fxml");
    }

    @FXML
    void showTags(ActionEvent event) throws IOException {
        ZeroController.jumpToWaitingNoResizableStage("/stages/tagPictureNLP.fxml");
    }

    @FXML
    void showTaggedText(ActionEvent event) throws IOException {
        ZeroController.jumpToWaitingStage("/stages/tagTextsNLPPage.fxml");
    }

    @FXML
    void showTexts(ActionEvent event) throws IOException{
        ZeroController.jumpToWaitingStage("/stages/textsNLPPage.fxml");
    }

}
