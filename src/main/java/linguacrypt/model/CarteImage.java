package linguacrypt.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import linguacrypt.utils.CardType;

public class CarteImage extends CarteBase {
    private final String url;


    public CarteImage(String url, CardType carteType) {
        super(carteType);
        this.url = url;
    }

    public CarteImage(String url, CardType type, boolean covered) {
        super(type, covered);
        this.url = url;
    }

    @JsonCreator
    public CarteImage(@JsonProperty("type") CardType type, @JsonProperty("covered") boolean covered,
                      @JsonProperty("url") String url) {
        super(type, covered);
        this.url = url;
    }

    public String getUrl() {
        return url;
    }
}
