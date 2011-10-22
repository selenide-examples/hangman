package ee.era.hangman.actions;

import com.opensymphony.xwork2.ActionSupport;
import ee.era.hangman.model.Word;
import ee.era.hangman.model.Words;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;

public class Game extends ActionSupport {

  Words words = new Words();

  public Word word;

  @Action(value = "game", results = {
      @Result(name = "success", location = "game.jsp")})
  @Override
  public String execute() throws Exception {
    word = words.getRandomWord();
    return SUCCESS;
  }
}
