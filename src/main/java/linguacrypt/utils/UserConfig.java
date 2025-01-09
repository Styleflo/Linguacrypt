package linguacrypt.utils;

import java.util.List;

public class UserConfig {
    private List<WordsCategory> categories;
    private List<String> addedImages;
    private List<Statistic> statistics;

    public List<WordsCategory> getCategories() {
        return categories;
    }

    public List<String> getAddedImages() {
        return addedImages;
    }

    public List<Statistic> getStatitics() {
        return statistics;
    }
}
