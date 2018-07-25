package ee.era.hangman.actions;

import com.google.inject.Inject;
import ee.era.hangman.model.Hangman;
import ee.era.hangman.model.Word;
import ee.era.hangman.model.Words;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;

public class Game extends GameActionSupport {
  @Inject
  private Words words;

  private Word randomWord;
  private Hangman game;

  @Action(value = "game", results = {
      @Result(name = "success", type = "freemarker", location = "game.ftl")
  })
  public String startGame() {
    randomWord = words.getRandomWord(getLanguage());
    game = new Hangman(randomWord.getWord());
    session.put("hangman", game);
    return SUCCESS;
  }

  public String getAlphabet() {
    return words.getAlphabet(getLanguage());
  }

  private String getLanguage() {
    return getLocale().getLanguage();
  }

  public String getWord() {
    return game.getWord();
  }

  public String getTopic() {
    return randomWord.getTopic();
  }
}
