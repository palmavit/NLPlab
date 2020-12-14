package sample.entities;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import sample.controllers.DeveloperController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Word {
    private final StringProperty word;
    private final IntegerProperty quantity;
    private ArrayList<String> texts;
    private ArrayList<String> tags;
    private ArrayList<Pair> forms;


    public Word(){
        this(null, null);
        texts = new ArrayList<>();
        tags = new ArrayList<>();
        forms = new ArrayList<>();
    }

    public Word(String word, int i) {
        this.word = new SimpleStringProperty(word);
        this.quantity = new SimpleIntegerProperty(i);
        texts = new ArrayList<>();
        tags = new ArrayList<>();
        forms = new ArrayList<>();
    }

    public Word(String word, int i, ArrayList<String> list) {
        this.word = new SimpleStringProperty(word);
        this.quantity = new SimpleIntegerProperty(i);
        texts = list;
        tags = new ArrayList<>();
        forms = new ArrayList<>();
    }

    public Word(String word, int i, ArrayList<String> list, ArrayList<String> tags) {
        this.word = new SimpleStringProperty(word);
        this.quantity = new SimpleIntegerProperty(i);
        this.texts = list;
        this.tags = tags;
    }

    public Word(String word, int i, ArrayList<String> list, ArrayList<String> tags, ArrayList<Pair> forms) {
        this.word = new SimpleStringProperty(word);
        this.quantity = new SimpleIntegerProperty(i);
        this.texts = list;
        this.tags = tags;
        this.forms = forms;
    }

    public Word(StringProperty word, IntegerProperty quantity) {
        this.word = word;
        this.quantity = quantity;
        this.texts = new ArrayList<>();
        this.tags = new ArrayList<>();
        this.forms = new ArrayList<>();
    }

    public Word(StringProperty word, IntegerProperty quantity, ArrayList<String> list) {
        this.word = word;
        this.quantity = quantity;
        this.texts = list;
        this.tags = new ArrayList<>();
        this.forms = new ArrayList<>();
    }

    public Word(StringProperty word, IntegerProperty quantity, ArrayList<String> list, ArrayList<String> tags) {
        this.word = word;
        this.quantity = quantity;
        this.texts = list;
        this.tags = tags;
        this.forms = new ArrayList<>();
    }

    public Word(HelpWord hw) {
        this(hw.getWord(), hw.getQ());
        this.forms = hw.getForms();
        this.texts = hw.getTexts();
        String str = hw.getTags();
        String[] tags = str.split(", ");
        for (String tag : tags){
            this.addTag(tag);
        }
    }

    public void join(Word w){
        if (!w.getWord().equals(word.get())) return;
        quantity.setValue(quantity.get() + w.getQuantity());
        for (String str : w.texts){
            if (!texts.contains(str)) texts.add(str);
        }
        for (String str : w.tags){
            if (!tags.contains(str)) tags.add(str);
        }
        for (Pair p : w.forms){
            boolean b = false;
            for (Pair p1 : forms){
                if (p1.equals(p)){
                    b = true;
                    break;
                }
            }
            if (!b) forms.add(p);
        }
    }


    public String getWord() {
        return word.get();
    }

    public StringProperty wordProperty() {
        return word;
    }

    public void setWord(String word) {
        this.word.set(word);
    }

    public int getQuantity() {
        return quantity.get();
    }

    public IntegerProperty quantityProperty() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity.set(quantity);
    }

    public ArrayList<String> getTexts(){
        return texts;
    }

    public ArrayList<String> getTags(){
        return tags;
    }

    public ArrayList<Pair> getForms() {
        return forms;
    }


    public void addText(String filepath){
        if (!texts.contains(filepath))
            texts.add(filepath);
    }

    public void addTag(String tag) {
        if (!tags.contains(tag))
            tags.add(tag);
    }

    public void addForm(String form, String tag){
        for (Pair p : forms){
            if (p.getWord().equals(form) && p.getTag().equals(tag)) return;
        }
        forms.add(new Pair(form, tag));
    }

    public void setTags(String str) {
        String[] strs = str.split(", ");
        tags = new ArrayList<>();
        Collections.addAll(tags, strs);
    }
}
