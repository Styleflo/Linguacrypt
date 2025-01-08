package linguacrypt.model;

import linguacrypt.utils.CardType;

public abstract class CarteBase {
    protected final CardType type;  // 0: bleu ; 1: rouge ; 2: noir ; 3: blanc
    protected boolean covered;

    public CarteBase(CardType type) {
        this.type = type;
    }

    public CardType getType() {
        return type;
    }

    public boolean isCovered() {
        return covered;
    }

    public void setCovered() {
        covered = true;
    }

    public abstract String getUrl();

    public abstract String getWord();

}
