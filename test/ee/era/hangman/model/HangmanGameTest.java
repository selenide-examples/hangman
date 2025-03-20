package ee.era.hangman.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class HangmanGameTest {
  @Test
  public void duplicateLettersShouldBeGuessedAtOnce() {
    Hangman game = new Hangman("topconf");
    game.guessLetter('o');
    assertThat(game.getWord()).isEqualTo("_o__o__");
  }

  @Test
  public void letterMatchingIsCaseInsensitive() {
    Hangman game = new Hangman("TopConf");
    game.guessLetter('t');
    assertThat(game.getWord()).isEqualTo("T______");
  }


  @Test
  public void correctWordIsShownAfterGameOver() {
    Hangman game = new Hangman("sofa");
    game.guessLetter('b');
    game.guessLetter('c');
    game.guessLetter('d');
    game.guessLetter('e');
    game.guessLetter('g');
    game.guessLetter('h');
    game.guessLetter('i');
    assertThat(game.isLost()).isTrue();
    assertThat(game.getWord()).isEqualTo("sofa");
  }

  @Test
  public void cyrillicLettersAreSupported() {
    Hangman game = new Hangman("диван");
    game.guessLetter('н');
    assertThat(game.getWord()).isEqualTo("____н");
  }
}
