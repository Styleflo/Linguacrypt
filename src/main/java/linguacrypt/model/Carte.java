package linguacrypt.model;

import linguacrypt.utils.CardType;

import java.io.Serializable;

public class Carte extends CarteBase {
    private final String word;

    public Carte(String word, CardType type) {
        super(type);
        this.word = word;
    }

    public String getWord() {
        return word;
    }
}
