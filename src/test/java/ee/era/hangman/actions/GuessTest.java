package ee.era.hangman.actions;

import ee.era.hangman.model.Word;
import ee.era.hangman.model.Words;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.*;

public class GuessTest {
  Game game = new Game();
  Guess guess = new Guess();
  @Before
  public void startGame() {
    HashMap<String, Object> session = new HashMap<String, Object>();
    game.setSession(session);
    guess.setSession(session);
    game.words = new Words() {
      @Override
      public Word getRandomWord(String language) {
        return new Word("software development", "agile");
      }
    };
    game.startGame();
  }

  @Test
  public void ifLetterIsGuessedItsShown() {
    assertEquals("_____", guess.getWordInWork());
    guess.letter = 'G';
    guess.guessLetter();
    assertEquals("_g___", guess.getWordInWork());
    assertTrue(guess.isGuessed());
  }

  @Test
  public void ifLetterIsNotGuessedThenFailuresCounterGrows() {
    guess.letter = 'x';
    guess.guessLetter();
    assertEquals("_____", guess.getWordInWork());
    assertFalse(guess.isGuessed());
    assertThat(game.getFailures(), equalTo(1));
  }
}
