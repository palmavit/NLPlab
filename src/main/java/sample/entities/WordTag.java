package sample.entities;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class WordTag {
    private final StringProperty pair;
    private final IntegerProperty quantity;


    public WordTag(){
        this(null, null);
    }

    public WordTag(String pair, int i) {
        this.pair = new SimpleStringProperty(pair);
        this.quantity = new SimpleIntegerProperty(i);
    }

    public WordTag(StringProperty pair, IntegerProperty quantity) {
        this.pair = pair;
        this.quantity = quantity;
    }


    public String getPair() {
        return pair.get();
    }

    public StringProperty pairProperty() {
        return pair;
    }

    public void setPair(String pair) {
        this.pair.set(pair);
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
