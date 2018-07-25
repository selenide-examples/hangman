package ee.era.hangman.model;

import static java.lang.Character.toLowerCase;
import static org.apache.commons.lang3.StringUtils.repeat;

public class Hangman {
  private final String correctWord;
  private String word;

  private int errors;

  public Hangman(String correctWord) {
    this.correctWord = correctWord;
    word = repeat('_', correctWord.length());
  }

  public int getErrors() {
    return errors;
  }

  public boolean guessLetter(char letter) {
    boolean guessed = false;

    StringBuilder newWord = new StringBuilder(word.length());
    for (int i = 0; i < correctWord.length(); i++) {
      if (toLowerCase(correctWord.charAt(i)) == toLowerCase(letter)) {
        newWord.append(correctWord.charAt(i));
        guessed = true;
      }
      else {
        newWord.append(word.charAt(i));
      }
    }

    if (guessed)
      word = newWord.toString();
    else {
      errors++;
      if (isLost()) {
        word = correctWord;
      }
    }

    return guessed;
  }

  public String getWord() {
    return word;
  }

  public boolean isLost() {
    return errors >= 6;
  }

  public boolean isWon() {
    return errors < 6 && word.equals(correctWord);
  }
}
