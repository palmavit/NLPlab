package sample.entities;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class BigrammQuantity {
    private final StringProperty bigramm;
    private final IntegerProperty quantity;


    public BigrammQuantity(){
        this(null, null);
    }

    public BigrammQuantity(String tag, int i) {
        this.bigramm = new SimpleStringProperty(tag);
        this.quantity = new SimpleIntegerProperty(i);
    }

    public BigrammQuantity(StringProperty tag, IntegerProperty quantity) {
        this.bigramm = tag;
        this.quantity = quantity;
    }


    public String getBigramm() {
        return bigramm.get();
    }

    public StringProperty bigrammProperty() {
        return bigramm;
    }

    public void setBigramm(String bigramm) {
        this.bigramm.set(bigramm);
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
