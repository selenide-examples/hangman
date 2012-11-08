package ee.era.hangman.actions;

import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.interceptor.SessionAware;

import java.util.Date;
import java.util.Map;

class GameActionSupport extends ActionSupport implements SessionAware {

  protected Map<String, Object> session;
  private static final String startTime = new Date().toString();

  @Override
  public void setSession(Map<String, Object> session) {
    this.session = session;
  }

  public String getStartTime() {
    return startTime;
  }
}
