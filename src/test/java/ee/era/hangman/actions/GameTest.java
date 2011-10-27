package ee.era.hangman.actions;

import ee.era.hangman.model.Word;
import ee.era.hangman.model.Words;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class GameTest {
  Game game = new Game();

  @Before
  public void startGame() {
    game.setSession(new HashMap<String, Object>());
    game.words = new Words() {
      @Override
      public Word getRandomWord() {
        return new Word("software development", "agile");
      }
    };
  }

  @Test
  public void initializesRandomWord() {
    game.startGame();
    assertThat(game.getWord().getTopic(), equalTo("software development"));
    assertThat(game.getWord().getWord(), equalTo("agile"));
    assertThat(game.getWordInWork(), equalTo("_____"));
    assertThat(game.getFailures(), equalTo(0));
  }
}
