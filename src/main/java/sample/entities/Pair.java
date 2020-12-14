package sample.entities;

import java.util.Objects;

public class Pair {
    private String word;
    private String tag;

    public Pair(String word, String tag) {
        this.word = word;
        this.tag = tag;
    }

    @Override
    public String toString() {
        return word + ", " + tag;
    }

    public String getWord() {
        return word;
    }

    private void setWord(String word) {
        this.word = word;
    }

    public String getTag() {
        return tag;
    }

    private void setTag(String tag) {
        this.tag = tag;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pair pair = (Pair) o;
        return word.equals(pair.word) && tag.equals(pair.tag);
    }

}
