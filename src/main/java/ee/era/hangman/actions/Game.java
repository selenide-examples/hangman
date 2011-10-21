package ee.era.hangman.actions;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.inject.Inject;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;


public class Game extends ActionSupport  {
  @Action(value = "game", results = {
      @Result(name = "success", location = "game.jsp")})
  @Override
  public String execute() throws Exception {
    return SUCCESS;
  }
}
