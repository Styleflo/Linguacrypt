package linguacrypt.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import linguacrypt.utils.CardType;

import java.io.Serializable;

public class Carte extends CarteBase {
    private final String word;

    public Carte(String word, CardType type) {
        super(type);
        this.word = word;
    }

    @JsonCreator
    public Carte(@JsonProperty("type") CardType type, @JsonProperty ("covered") boolean covered,
                 @JsonProperty ("word") String word) {
        super(type, covered);
        this.word = word;
    }

    @Override
    public String getWord() {
        return word;
    }

    @Override
    public String getUrl() {
        return "Attention la carte est une carte image, elle a un url de photo et non un mot";
    }

}
