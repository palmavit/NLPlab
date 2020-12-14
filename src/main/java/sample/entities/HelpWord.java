package sample.entities;


import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.ArrayList;
import java.util.Collections;

public class HelpWord {
    private final StringProperty word;
    private final StringProperty tags;
    private ArrayList<String> texts;
    //private ArrayList<String> tagList;
    private ArrayList<Pair> forms;
    private int q;

    public HelpWord(){
        word = null;
        tags = null;
        q = 0;
        texts = new ArrayList<>();
        //tagList = new ArrayList<>();
        forms = new ArrayList<>();
    }

    public HelpWord(String word, String tags) {
        this.word = new SimpleStringProperty(word);
        this.tags = new SimpleStringProperty(tags);
        texts = new ArrayList<>();
        q = 0;
        //tagList = new ArrayList<>();
        forms = new ArrayList<>();
    }

    public int getQ() {
        return q;
    }

    public void setQ(int q) {
        this.q = q;
    }

    public HelpWord(StringProperty word, StringProperty tags) {
        this.word = word;
        this.tags = tags;
        q = 0;
        texts = new ArrayList<>();
        forms = new ArrayList<>();
    }

    public HelpWord(Word w){
        this(w.getWord(), null);
        StringBuilder str = new StringBuilder("");
        for (String s : w.getTags()){
            str.append(s).append(", ");
        }
        str = new StringBuilder(str.substring(0, str.length() - 2));
        this.q = w.getQuantity();
        this.setTags(str.toString());
        this.forms = w.getForms();
        this.texts = w.getTexts();
    }


    public void join(HelpWord w){
        if (!w.getWord().equals(word.get())) return;
        q += w.getQ();
        for (String str : w.texts){
            if (!texts.contains(str)) texts.add(str);
        }
        String [] strs = w.tags.get().split(", ");

        for (String tag : strs){
            if (!tags.get().contains(tag)) tags.setValue(tags.get() + ", " + tag);
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

    public String getTags() {
        return tags.get();
    }

    public StringProperty tagsProperty() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags.set(tags);
    }

    public ArrayList<String> getTexts() {
        return texts;
    }

    private void setTexts(ArrayList<String> texts) {
        this.texts = texts;
    }

    public ArrayList<Pair> getForms() {
        return forms;
    }

    private void setForms(ArrayList<Pair> forms) {
        this.forms = forms;
    }


    public void addText(String filepath){
        if (!texts.contains(filepath))
            texts.add(filepath);
    }

    public void addTag(String tag) {
        /*if (!tagList.contains(tag))
            tagList.add(tag);*/
        tags.set(tags.get() + ", " + tag);
    }

    public void addForm(String form, String tag){
        for (Pair p : forms){
            if (p.getWord().equals(form) && p.getTag().equals(tag)) return;
        }
        forms.add(new Pair(form, tag));
    }

    /*public void setTagList(String str) {
        String[] strs = str.split(", ");
        tagList = new ArrayList<>();
        Collections.addAll(tagList, strs);
    }*/

}
