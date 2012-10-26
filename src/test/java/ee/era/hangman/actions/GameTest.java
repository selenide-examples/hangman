package ee.era.hangman.actions;

import ee.era.hangman.model.Word;
import ee.era.hangman.model.Words;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Locale;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class GameTest {
  Game game = new Game() {
    @Override
    public Locale getLocale() {
      return Locale.ENGLISH;
    }
  };

  @Before
  public void startGame() {
    game.setSession(new HashMap<String, Object>());
    game.words = new Words() {
      @Override
      public Word getRandomWord(String language) {
        return new Word("Software Development", "agiLe");
      }
    };
  }

  @Test
  public void initializesRandomWord() {
    game.startGame();
    assertThat(game.getWord().getTopic(), equalTo("Software Development"));
    assertThat(game.getWord().getWord(), equalTo("agiLe"));
    assertThat(game.getWordInWork(), equalTo("_____"));
    assertThat(game.getFailures(), equalTo(0));
  }
}
