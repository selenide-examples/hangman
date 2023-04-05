package uitest;

import com.codeborne.selenide.Configuration;
import ee.era.hangman.Launcher;
import org.apache.commons.io.IOUtils;
import org.junit.BeforeClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.apache.commons.lang3.StringUtils.substring;

public abstract class AbstractHangmanTest {
  private static final Logger log = LoggerFactory.getLogger(AbstractHangmanTest.class);
  private static volatile Launcher launcher;

  @BeforeClass
  public synchronized static void startServer() throws Exception {
    if (launcher == null) {
      Configuration.baseUrl = "http://localhost:9999";
      log.info("Starting {}/hangman (user dir: {}) ...", Configuration.baseUrl, System.getProperty("user.dir"));
      launcher = new Launcher("test", 9999);
      launcher.run();
      log.info("Started {}/hangman (user dir: {})", Configuration.baseUrl, System.getProperty("user.dir"));
    }

    String sanityCheck = IOUtils.toString(new URL(Configuration.baseUrl + "/hangman"), UTF_8);
    log.info("Sanity check passed: {}", substring(sanityCheck, 0, 42));
  }

  public static void main(String[] args) throws Exception {
    startServer();
  }
}
