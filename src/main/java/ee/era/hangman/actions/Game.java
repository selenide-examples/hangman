package ee.era.hangman.actions;

import ee.era.hangman.model.Word;
import ee.era.hangman.model.Words;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;

import static ee.era.hangman.di.DependencyInjection.inject;

public class Game extends GameActionSupport {
  Words words = inject(Words.class);

  @Action(value = "game", results = {
      @Result(name = "success", location = "game.jsp")})
  @Override
  public String execute() {
    Word randomWord = words.getRandomWord();
    setWord(randomWord);
    setWordInWork(randomWord.getWord().replaceAll(".", "_"));
    setFailures(0);
    return SUCCESS;
  }

  public String getAlphabet() {
    return "АБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯ";
    //  return "ABCDEFGHIJKLMNOPQRSŠZŽTUVWÕÄÖÜXY";
    //  return "ABCDEFGHIJKLMNOPRSTUVWXYZ";
  }
}
