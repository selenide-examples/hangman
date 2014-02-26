package ee.era.hangman.model;

public class Language {
  private final String name;
  private final String alphabet;

  public Language(String name, String alphabet) {
    this.name = name;
    this.alphabet = alphabet;
  }

  public String getName() {
    return name;
  }

  public String getAlphabet() {
    return alphabet;
  }
}
