package linguacrypt.model;

public class CarteImage {
    private final String url;
    private final CarteType carteType;  // 0: bleu ; 1: rouge ; 2: noir ; 3: blanc
    private boolean covered;

    public Carte(String url, int carteType) {
        this.url = url;
        this.carteType = carteType;
        covered = false;
    }

    public int getType() {
        return carteType;
    }

    public String getWord() {
        return carteType;
    }

    public void setCovered() {
        covered = true;
    }

    public boolean isCovered() {
        return covered;
    }
}
