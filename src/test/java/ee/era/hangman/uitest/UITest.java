package ee.era.hangman.uitest;

import ee.era.hangman.Launcher;
import org.junit.After;
import org.junit.Before;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public abstract class UITest {
  WebDriver browser;
  Launcher server;

  @Before
  public void startServer() throws Exception {
    server = new Launcher(8888);
    server.run();
  }

  @Before
  public void openBrowser() {
    browser = new FirefoxDriver();
    browser.get("http://localhost:8888/game");
  }

  @After
  public void closeBrowser() {
    if (browser != null) {
      browser.close();
      browser.quit();
      browser = null;
    }
  }

  @After
  public void shutdownServer() {
    if (server != null) {
      server.stop();
      server = null;
    }
  }
}
