package ee.era.hangman.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class HangmanGameTest {
  @Test
  public void duplicateLettersShouldBeGuessedAtOnce() {
    Hangman game = new Hangman("topconf");
    game.guessLetter('o');
    assertEquals("_o__o__", game.getWord());
  }

  @Test
  public void letterMatchingIsCaseInsensitive() {
    Hangman game = new Hangman("TopConf");
    game.guessLetter('t');
    assertEquals("T______", game.getWord());
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
    assertTrue(game.isLost());
    assertEquals("sofa", game.getWord());
  }

  @Test
  public void cyrillicLettersAreSupported() {
    Hangman game = new Hangman("диван");
    game.guessLetter('н');
    assertEquals("____н", game.getWord());
  }
}
