package uitest;

import com.codeborne.selenide.Configuration;
import ee.era.hangman.Launcher;
import org.apache.commons.io.IOUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.apache.commons.lang3.StringUtils.substring;

public abstract class AbstractHangmanTest {
  private static final Logger log = LoggerFactory.getLogger(AbstractHangmanTest.class);
  private static Launcher launcher;

  @BeforeClass
  public static void startServer() throws Exception {
    Configuration.baseUrl = "http://localhost:9999";
    log.info("Starting {}/hangman (user dir: {}) ...", Configuration.baseUrl, System.getProperty("user.dir"));
    launcher = new Launcher("test", 9999);
    launcher.run();
    log.info("Started {}/hangman (user dir: {})", Configuration.baseUrl, System.getProperty("user.dir"));

    String sanityCheck = IOUtils.toString(new URL(Configuration.baseUrl + "/hangman"), UTF_8);
    log.info("Sanity check passed: {}", substring(sanityCheck, 0, 42));
  }

  @AfterClass
  public static void stopServer() {
    if (launcher != null) {
      log.info("Stopping {}/hangman ...", Configuration.baseUrl);
      launcher.stop();
      log.info("Stopped {}/hangman", Configuration.baseUrl);
      launcher = null;
    }
  }

  public static void main(String[] args) throws Exception {
    startServer();
  }
}
