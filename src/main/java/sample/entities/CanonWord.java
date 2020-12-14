package sample.entities;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class CanonWord {
    private final StringProperty word;
    private final IntegerProperty quantity;
    private final StringProperty tag;

    public CanonWord(){
        word = null;
        tag = null;
        quantity = null;
    }

    public CanonWord(String word, String tag) {
        this.word = new SimpleStringProperty(word);
        this.tag = new SimpleStringProperty(tag);
        this.quantity = new SimpleIntegerProperty(0);
    }

    public CanonWord(String word, String tag, Integer quantity) {
        this.word = new SimpleStringProperty(word);
        this.tag = new SimpleStringProperty(tag);
        this.quantity = new SimpleIntegerProperty(quantity);
    }

    public CanonWord(StringProperty word, StringProperty tag) {
        this.word = word;
        this.tag = tag;
        this.quantity = new SimpleIntegerProperty(0);
    }

    public CanonWord(StringProperty word, StringProperty tag, IntegerProperty quantity) {
        this.word = word;
        this.tag = tag;
        this.quantity = quantity;
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

    public String getTag() {
        return tag.get();
    }

    public StringProperty tagProperty() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag.set(tag);
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
}
