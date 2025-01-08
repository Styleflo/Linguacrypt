package linguacrypt.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class WordsFileHandler {
    private final WordsCategories wordsCategories;

    public WordsFileHandler(String filePath) throws IOException {
        InputStream resource = getClass().getClassLoader().getResourceAsStream(filePath);

        DataUtils.assertNotNull(resource, "Le fichier " + filePath + " n'existe pas.");

        ObjectMapper objectMapper = new ObjectMapper();
        wordsCategories = objectMapper.readValue(resource, WordsCategories.class);
    }

    public void writeJsonFile() {
        //objectMapper.writeValue(file, wordsCategories);  // todo
    }

    public ArrayList<String> getWordsByTheme(String theme) {
        List<WordsCategory> categories = wordsCategories.getCategories();

        for (WordsCategory category : categories) {
            if (category.getName().equals(theme)) {
                return new ArrayList<>(category.getWords());
            }
        }

        return null;
    }

    public ArrayList<String> getWordsByThemes(ArrayList<String> themes) {
        ArrayList<String> words = new ArrayList<>();

        List<WordsCategory> categories = wordsCategories.getCategories();

        for (WordsCategory category : categories) {
            if (themes.contains(category.getName())) {
                words.addAll(category.getWords());
            }
        }

        return words;
    }

    public ArrayList<String> getAllThemes() {
        ArrayList<String> themes = new ArrayList<>();

        List<WordsCategory> categories = wordsCategories.getCategories();

        for (WordsCategory category : categories) {
            themes.add(category.getName());
        }

        return themes;
    }

    public boolean addCategory(String categoryName) {
        if (themeExists(categoryName)) return false;

        wordsCategories.getCategories().add(new WordsCategory(categoryName, new ArrayList<>()));

        return true;
    }

    public boolean themeExists(String category) {
        for (WordsCategory cat : wordsCategories.getCategories()) {
            if (cat.getName().equals(category)) {
                return true;
            }
        }
        return false;
    }

    public Object[] addWordToCategory(String category, String word) {
        WordsCategory previousCategory = getCategoryByWord(word);
        if (previousCategory != null) {
            return new Object[]{false, "Le mot " + word + " existe déjà dans le thème " + previousCategory.getName() + "."};
        }

        for (WordsCategory cat : wordsCategories.getCategories()) {
            if (cat.getName().equals(category)) {
                cat.getWords().add(word);
                return new Object[]{true, ""};
            }
        }

        return new Object[]{false, "La catégorie " + category + " n'existe pas."};
    }

    public WordsCategory getCategoryByWord(String word) {
        for (WordsCategory cat : wordsCategories.getCategories()) {
            if (cat.getWords().contains(word)) {
                return cat;
            }
        }
        return null;
    }

    public void removeWordFromCategory(String category, String word) {
        List<WordsCategory> categories = wordsCategories.getCategories();

        for (WordsCategory cat : categories) {
            if (cat.getName().equals(category)) {
                if (cat.getWords().contains(word)) {
                    cat.getWords().remove(word);

                    if (cat.getWords().isEmpty()) {
                        categories.remove(cat);
                    }

                    return;
                }
            }
        }
    }
}
