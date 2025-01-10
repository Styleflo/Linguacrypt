package linguacrypt.model;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import linguacrypt.utils.CardType;
import linguacrypt.utils.CarteDeserializer;


@JsonDeserialize(using = CarteDeserializer.class)
public abstract class CarteBase {
    protected CardType type;  // 0: bleu ; 1: rouge ; 2: noir ; 3: blanc
    protected boolean covered;

    public CarteBase(CardType type) {
        this.type = type;
    }

    public CarteBase() {
    }

    @JsonCreator
    public CarteBase(@JsonProperty("type") CardType type, @JsonProperty("covered") boolean covered) {
        this.type = type;
        this.covered = covered;
    }

    public CardType getType() {
        return type;
    }

    public void setType(CardType type) {
        this.type = type;
    }

    public boolean isCovered() {
        return covered;
    }

    public void setCovered() {
        covered = true;
    }
}
