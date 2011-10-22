package ee.era.hangman.actions;

import ee.era.hangman.model.Word;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;

@ParentPackage("json-default")
public class Guess extends GameActionSupport {
  public char letter;

  private boolean guessed = false;
  private boolean gameOver = false;

  @Action(value = "guess", results = {
      @Result(name = "success", type = "json")})
  public String guessLetter() {
    String word = getWord().getWord();
    String wordInWork = getWordInWork();
    for (int i = 0; i < word.length(); i++) {
      if (word.charAt(i) == Character.toLowerCase(letter)) {
        guessed = true;
        wordInWork = wordInWork.substring(0, i) + word.charAt(i) + wordInWork.substring(i+1);
      }
    }

    if (guessed) {
      setWordInWork(wordInWork);
      if (!wordInWork.contains("_")) {
        gameOver = true;
      }
    }
    else {
      setFailures(getFailures() + 1);
      if (getFailures() > 5) {
//        setWordInWork(getWord().getWord()); // TODO uncomment
        gameOver = true;
      }
    }
    return SUCCESS;
  }

  public boolean isGuessed() {
    return guessed;
  }

  public boolean isGameOver() {
    return gameOver;
  }

  @Override
  public String getWordInWork() {
    return super.getWordInWork();
  }

  @Override
  public int getFailures() {
    return super.getFailures();
  }
}
