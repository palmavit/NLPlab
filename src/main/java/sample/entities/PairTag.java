package sample.entities;

import java.util.Objects;

public class PairTag {
    private String tag1;
    private String tag2;

    public PairTag(){
        tag1 = null;
        tag2 = null;
    }

    public PairTag(String tag1, String tag2) {
        this.tag1 = tag1;
        this.tag2 = tag2;
    }

    public String getTag1() {
        return tag1;
    }

    public void setTag1(String tag1) {
        this.tag1 = tag1;
    }

    public String getTag2() {
        return tag2;
    }

    public void setTag2(String tag2) {
        this.tag2 = tag2;
    }

    @Override
    public String toString() {
        return tag1 + ", " + tag2;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PairTag pairTag = (PairTag) o;
        return Objects.equals(tag1, pairTag.tag1) &&
                Objects.equals(tag2, pairTag.tag2);
    }

}
