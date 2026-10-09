package ee.era.hangman.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class HangmanTest {
  private Hangman startGame(String word) {
    return new Hangman(word);
  }

  @Test
  public void userCanGuessLetters() {
    Hangman game = startGame("sofa");
    assertThat(game.getWord()).isEqualTo("____");
    assertThat(game.getErrors()).isEqualTo(0);

    game.guessLetter('a');
    assertThat(game.getWord()).isEqualTo("___a");
    assertThat(game.getErrors()).isEqualTo(0);

    game.guessLetter('b');
    assertThat(game.getWord()).isEqualTo("___a");
    assertThat(game.getErrors()).isEqualTo(1);
  }

  @Test
  public void userWinsWhenAllLettersOfWordAreGuessed() {
    Hangman game = startGame("sofa");
    game.guessLetter('s');
    game.guessLetter('o');
    game.guessLetter('f');
    game.guessLetter('a');
    assertThat(game.getWord()).isEqualTo("sofa");
    assertThat(game.isLost()).isFalse();
    assertThat(game.isWon()).isTrue();
  }

  @Test
  public void userHas6Tries() {
    Hangman game = startGame("sofa");
    game.guessLetter('b');
    game.guessLetter('c');
    game.guessLetter('d');
    game.guessLetter('e');
    game.guessLetter('g');
    assertThat(game.getWord()).isEqualTo("____");
    assertThat(game.getErrors()).isEqualTo(5);
    assertThat(game.isLost()).isFalse();

    game.guessLetter('h');
    assertThat(game.getWord()).isEqualTo("sofa");
    assertThat(game.getErrors()).isEqualTo(6);
    assertThat(game.isLost()).isTrue();
    assertThat(game.isWon()).isFalse();
  }

  @Test
  public void supportsCyrillicCharacters() {
    Hangman game = startGame("Диван");
    game.guessLetter('н');
    game.guessLetter('а');
    game.guessLetter('в');
    game.guessLetter('и');
    game.guessLetter('д');
    assertThat(game.getWord()).isEqualTo("Диван");
    assertThat(game.isWon()).isTrue();
  }

  @Test
  public void duplicateLetters() {
    Hangman game = startGame("hologram");
    game.guessLetter('o');
    assertThat(game.getWord()).isEqualTo("_o_o____");
    assertThat(game.getErrors()).isEqualTo(0);
  }

  @Test
  public void longerWordsGetMoreTries() {
    // "sofa" = 4 letters → 6 max errors (base)
    assertThat(startGame("sofa").getMaxErrors()).isEqualTo(6);
    // "piano" = 5 letters → 6 max errors (base)
    assertThat(startGame("piano").getMaxErrors()).isEqualTo(6);
    // "guitar" = 6 letters → 7 max errors
    assertThat(startGame("guitar").getMaxErrors()).isEqualTo(7);
    // "hologram" = 8 letters → 9 max errors
    assertThat(startGame("hologram").getMaxErrors()).isEqualTo(9);
    // "refrigerator" = 12 letters → 13 max errors
    assertThat(startGame("refrigerator").getMaxErrors()).isEqualTo(13);
  }

  @Test
  public void longerWordNotLostUntilMaxErrors() {
    Hangman game = startGame("guitar"); // 6 letters → 7 max errors
    for (int i = 0; i < 6; i++) {
      game.guessLetter((char) ('1' + i));
    }
    assertThat(game.getErrors()).isEqualTo(6);
    assertThat(game.isLost()).isFalse();

    game.guessLetter('0');
    assertThat(game.getErrors()).isEqualTo(7);
    assertThat(game.isLost()).isTrue();
  }

  @Test
  public void matchingIsCaseInsensitive() {
    Hangman game = startGame("TopConf");
    game.guessLetter('t');
    game.guessLetter('C');
    game.guessLetter('o');
    assertThat(game.getWord()).isEqualTo("To_Co__");
    assertThat(game.getErrors()).isEqualTo(0);
  }
}
