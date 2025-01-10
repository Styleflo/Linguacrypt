package linguacrypt.utils;

import java.util.ArrayList;
import java.util.List;

public class ImagesCategory {
    private String name;
    private List<String> words;

    public ImagesCategory(String name, ArrayList<String> words) {
        this.name = name;
        this.words = words;
    }

    public ImagesCategory() {
        words = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getWords() {
        return words;
    }

    public void setWords(List<String> words) {
        this.words = words;
    }
}
