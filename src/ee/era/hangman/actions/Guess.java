package ee.era.hangman.actions;

import ee.era.hangman.model.Hangman;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;

@ParentPackage("json-default")
public class Guess extends GameActionSupport {
  public char letter;
  private boolean guessed;

  private Hangman game;

  @Action(value = "guess", results = {
      @Result(name = "success", type = "json")})
  public String guessLetter() {
    game = (Hangman) session.get("hangman");
    guessed = game.guessLetter(letter);
    return SUCCESS;
  }

  public String getWord() {
    return game.getWord();
  }

  public boolean isGuessed() {
    return guessed;
  }

  public boolean isGameOver() {
    return game.isLost() || game.isWon();
  }

  public int getFailures() {
    return game.getErrors();
  }
}
