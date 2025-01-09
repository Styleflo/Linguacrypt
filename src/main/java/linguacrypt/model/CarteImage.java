package linguacrypt.model;

import linguacrypt.utils.CardType;

public class CarteImage extends CarteBase {
    private final String url;


    public CarteImage(String url, CardType carteType) {
        super(carteType);
        this.url = url;
    }

    public String getUrl() {
        return url;
    }
}
