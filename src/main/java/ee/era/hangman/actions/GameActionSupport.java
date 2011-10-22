package ee.era.hangman.actions;

import com.opensymphony.xwork2.ActionSupport;
import ee.era.hangman.model.Word;
import org.apache.struts2.interceptor.SessionAware;

import java.util.Map;

abstract class GameActionSupport extends ActionSupport implements SessionAware {

  private Map<String, Object> session;

  @Override
  public void setSession(Map<String, Object> session) {
    this.session = session;
  }

  public Word getWord() {
    return (Word) session.get("word");
  }

  protected void setWord(Word word) {
    session.put("word", word);
  }

  public String getWordInWork() {
    return (String) session.get("wordInWork");
  }

  protected void setWordInWork(String wordInWork) {
    session.put("wordInWork", wordInWork);
  }

  public int getFailures() {
    return (Integer) session.get("failures");
  }

  public void setFailures(int failures) {
    session.put("failures", failures);
  }
}
