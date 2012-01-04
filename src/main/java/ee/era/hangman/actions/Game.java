package ee.era.hangman.actions;

import ee.era.hangman.model.Word;
import ee.era.hangman.model.Words;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;

import static ee.era.hangman.di.DependencyInjection.inject;

public class Game extends GameActionSupport {
  Words words = inject(Words.class);

  public String language;

  @Action(value = "game", results = {
      @Result(name = "success", location = "game.jsp")})
  public String startGame() {
    Word randomWord = words.getRandomWord(getLanguage());
    setWord(randomWord);
    setWordInWork(randomWord.getWord().replaceAll(".", "_"));
    setFailures(0);
    return SUCCESS;
  }

  public String getAlphabet() {
    return words.getAlphabet(getLanguage());
  }

  private String getLanguage() {
    if (language == null) {
      language = (String) session.get("language");
      if (language == null) {
        language = "rus";
      }
    }

    session.put("language", language);
    return language;
  }
}
