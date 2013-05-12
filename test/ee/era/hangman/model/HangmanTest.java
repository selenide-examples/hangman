package ee.era.hangman.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class HangmanTest {
  Hangman game;

  private void startGame(String word) {
    game = new Hangman(word);
  }

  @Test
  public void userCanGuessLetters() {
    startGame("sofa");
    assertEquals("____", game.getWord());
    assertEquals(0, game.getErrors());

    game.guessLetter('a');
    assertEquals("___a", game.getWord());
    assertEquals(0, game.getErrors());

    game.guessLetter('b');
    assertEquals("___a", game.getWord());
    assertEquals(1, game.getErrors());
  }

  @Test
  public void userWinsWhenAllLettersOfWordAreGuessed() {
    startGame("sofa");
    game.guessLetter('s');
    game.guessLetter('o');
    game.guessLetter('f');
    game.guessLetter('a');
    assertEquals("sofa", game.getWord());
    assertFalse(game.isLost());
    assertTrue(game.isWon());
  }

  @Test
  public void userHas6Tries() {
    startGame("sofa");
    game.guessLetter('b');
    game.guessLetter('c');
    game.guessLetter('d');
    game.guessLetter('e');
    game.guessLetter('g');
    assertEquals("____", game.getWord());
    assertEquals(5, game.getErrors());
    assertFalse(game.isLost());

    game.guessLetter('h');
    assertEquals("sofa", game.getWord());
    assertEquals(6, game.getErrors());
    assertTrue(game.isLost());
    assertFalse(game.isWon());
  }

  @Test
  public void supportsCyrillicCharacters() {
    startGame("Диван");
    game.guessLetter('н');
    game.guessLetter('а');
    game.guessLetter('в');
    game.guessLetter('и');
    game.guessLetter('д');
    assertEquals("Диван", game.getWord());
    assertTrue(game.isWon());
  }

  @Test
  public void duplicateLetters() {
    startGame("hologram");
    game.guessLetter('o');
    assertEquals("_o_o____", game.getWord());
    assertEquals(0, game.getErrors());
  }

  @Test
  public void matchingIsCaseInsensitive() {
    startGame("TopConf");
    game.guessLetter('t');
    game.guessLetter('C');
    game.guessLetter('o');
    assertEquals("To_Co__", game.getWord());
    assertEquals(0, game.getErrors());
  }
}
