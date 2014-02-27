package uitest;

import com.codeborne.selenide.junit.ScreenShooter;
import ee.era.hangman.Launcher;
import ee.era.hangman.actions.Game;
import ee.era.hangman.model.Word;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

public abstract class AbstractHangmanTest {
  @Rule
  public ScreenShooter makeScreenshotOnFailure = ScreenShooter.failedTests();

  private static Launcher launcher;

  @BeforeClass
  public static void startServer() throws Exception {
    launcher = new Launcher(8080);
    launcher.run();
    Game.words = spy(Game.words);
    doReturn(new Word("дом", "гвоздь")).when(Game.words).getRandomWord("ru");
    doReturn(new Word("maja", "nael")).when(Game.words).getRandomWord("et");
    doReturn(new Word("house", "sofa")).when(Game.words).getRandomWord("en");
  }

  @AfterClass
  public static void stopServer() {
    if (launcher != null) {
      launcher.stop();
      launcher = null;
    }
  }

  public static void main(String[] args) throws Exception {
    startServer();
  }
}
