package linguacrypt.model;

public abstract class CarteBase {
    protected final int type;  // 0: bleu ; 1: rouge ; 2: noir ; 3: blanc
    protected boolean covered;

    public CarteBase(int type) {
        this.type = type;
    }

    public int getType() {
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
