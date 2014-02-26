package uitest;

import com.codeborne.selenide.junit.ScreenShooter;
import ee.era.hangman.Launcher;
import ee.era.hangman.actions.Game;
import ee.era.hangman.model.Word;
import ee.era.hangman.model.Words;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;

public abstract class AbstractHangmanTest {
  @Rule
  public ScreenShooter makeScreenshotOnFailure = ScreenShooter.failedTests();

  private static Launcher launcher;

  @BeforeClass
  public static void startServer() throws Exception {
    launcher = new Launcher(8080);
    launcher.run();
    Game.words = new WordsMock();
  }

  @AfterClass
  public static void stopServer() {
    if (launcher != null) {
      launcher.stop();
      launcher = null;
    }
  }

  public static class WordsMock extends Words {
    @Override
    public Word getRandomWord(String language) {
      if ("ru".equals(language))
        return new Word("дом", "гвоздь");
      if ("et".equals(language))
        return new Word("maja", "nael");
      return new Word("house", "sofa");
    }
  }

  public static void main(String[] args) throws Exception {
    startServer();
  }
}
