package sample.entities;

import javafx.collections.ObservableList;
import sample.controllers.DeveloperController;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DictionaryWriter {

    private static HashMap<String, Word> dict = DeveloperController.getDictionary();

    public static void write(ObservableList<Word> words){
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter("DictionaryFull.txt");
            for (Word w : words){
                fileWriter.write(w.getWord() + " " + w.getQuantity() + " [");
                ArrayList<String> tags = w.getTags();
                for (int i = 0; i < tags.size(); i++){
                    fileWriter.write(tags.get(i));
                    if (i != tags.size() - 1) fileWriter.write(", ");
                    else fileWriter.write("] (");
                }

                ArrayList<Pair> pairs = w.getForms();
                for (int i = 0; i < pairs.size(); i++){
                    fileWriter.write(pairs.get(i).toString() + ", ");
                    Word nWord = dict.get(pairs.get(i).getWord());
                    if (nWord == null) fileWriter.write("0);");
                    else  {
                        int n = nWord.getQuantity();
                        fileWriter.write(Integer.toString(n));
                        fileWriter.write(");");
                    }
                    if (i != pairs.size() - 1) fileWriter.write(" (");
                }
                fileWriter.write("\n");

            }
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void write(HashMap<String, Word> words){
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter("DictionaryFull.txt");
            for (Map.Entry w : words.entrySet()){
                Word word = (Word)w.getValue();
                fileWriter.write(w.getKey() + " " + word.getQuantity() + " [");
                ArrayList<String> tags = word.getTags();
                for (int i = 0; i < tags.size(); i++){
                    fileWriter.write(tags.get(i));
                    if (i != tags.size() - 1) fileWriter.write(", ");
                    else fileWriter.write("] (");
                }

                ArrayList<Pair> pairs = word.getForms();
                for (int i = 0; i < pairs.size(); i++){
                    fileWriter.write(pairs.get(i).toString() + ", ");
                    Word nWord = dict.get(pairs.get(i).getWord());
                    if (nWord == null) fileWriter.write("0);");
                    else  {
                        int n = nWord.getQuantity();
                        fileWriter.write(Integer.toString(n));
                        fileWriter.write(");");
                    }
                    if (i != pairs.size() - 1) fileWriter.write(" (");
                }
                fileWriter.write("\n");

            }
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeHelp(ObservableList<HelpWord> words){
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter("DictionaryFull.txt");
            for (HelpWord w : words){
                fileWriter.write(w.getWord() + " " + w.getQ() + " [");
                String tags = w.getTags();
                fileWriter.write(tags);
                fileWriter.write("] (");

                ArrayList<Pair> pairs = w.getForms();
                for (int i = 0; i < pairs.size(); i++){
                    fileWriter.write(pairs.get(i).toString() + ", ");
                    Word nWord = dict.get(pairs.get(i).getWord());
                    if (nWord == null) fileWriter.write("0);");
                    else  {
                        int n = nWord.getQuantity();
                        fileWriter.write(Integer.toString(n));
                        fileWriter.write(");");
                    }
                    if (i != pairs.size() - 1) fileWriter.write(" (");
                }
                fileWriter.write("\n");

            }
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
