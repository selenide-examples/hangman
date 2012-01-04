package ee.era.hangman.model;

import java.util.List;

public class Language {
    private final String name;
    private final String alphabet;
    private final List<TopicWords> dictionary;

    public Language(String name, String alphabet, List<TopicWords> dictionary) {
        this.name = name;
        this.alphabet = alphabet;
        this.dictionary = dictionary;
    }

    public String getName() {
        return name;
    }

    public String getAlphabet() {
        return alphabet;
    }

    public List<TopicWords> getDictionary() {
        return dictionary;
    }
}
