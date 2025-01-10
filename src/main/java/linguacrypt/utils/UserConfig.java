package linguacrypt.utils;

import java.util.List;

public class UserConfig {
    private List<WordsCategory> categories;
    private List<String> usedImages;
    private List<String> addedImages;
    private Statistics statistics;

    public List<WordsCategory> getCategories() {
        return categories;
    }

    public List<String> getUsedImages() {
        return usedImages;
    }

    public List<String> getAddedImages() {
        return addedImages;
    }

    public Statistics getStatistics() {
        return statistics;
    }
}
