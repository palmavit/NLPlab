package sample.entities;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class TagQuantity {
    private final StringProperty tag;
    private final IntegerProperty quantity;


    public TagQuantity(){
        this(null, null);
    }

    public TagQuantity(String tag, int i) {
        this.tag = new SimpleStringProperty(tag);
        this.quantity = new SimpleIntegerProperty(i);
    }

    public TagQuantity(StringProperty tag, IntegerProperty quantity) {
        this.tag = tag;
        this.quantity = quantity;
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
