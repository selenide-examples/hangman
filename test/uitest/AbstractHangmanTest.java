package uitest;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.WebDriverRunner;
import ee.era.hangman.Launcher;
import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.BeforeClass;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.List;
import java.util.logging.Level;

import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static com.codeborne.selenide.WebDriverRunner.hasWebDriverStarted;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.apache.commons.lang3.StringUtils.substring;

public abstract class AbstractHangmanTest {
  private static final Logger log = LoggerFactory.getLogger(AbstractHangmanTest.class);
  private static volatile Launcher launcher;

  @BeforeClass
  public synchronized static void startServer() throws Exception {
    if (launcher == null) {
      Configuration.baseUrl = "http://localhost:9999";
      log.info("Starting {} (user dir: {}) ...", Configuration.baseUrl, System.getProperty("user.dir"));
      launcher = new Launcher("test", "localhost", 9999);
      launcher.run();
      log.info("Started {} (user dir: {})", Configuration.baseUrl, System.getProperty("user.dir"));

      LoggingPreferences logPrefs = new LoggingPreferences();
      logPrefs.enable(LogType.BROWSER, Level.ALL);
      logPrefs.enable(LogType.PERFORMANCE, Level.ALL);
      Configuration.browserCapabilities.setCapability("goog:loggingPrefs", logPrefs);
    }

    String sanityCheck = IOUtils.toString(new URL(Configuration.baseUrl), UTF_8);
    log.info("Sanity check passed: {}", substring(sanityCheck, 0, 42));
  }

  @After
  public void checkBrowserLogs() {
    if (hasWebDriverStarted() && !WebDriverRunner.isFirefox()) {
      try {
        log.info("Checking browser logs after test ...");
        List<LogEntry> browserLogs = getWebDriver().manage().logs().get(LogType.BROWSER).getAll();
        log.info("Found {} browser logs", browserLogs.size());
        if (browserLogs.size() > 0) {
          for (LogEntry browserLog : browserLogs) {
            log.info(browserLog.toString());
          }
        }
      }
      catch (RuntimeException e) {
        log.error("Failed to check browser logs after test", e);
      }
    }
    else {
      log.info("Browser is not opened, cannot check browser logs after test");
    }
  }

  public static void main(String[] args) throws Exception {
    startServer();
  }
}
