package ee.uitest;

import com.codeborne.selenide.ShouldableWebElement;
import com.codeborne.selenide.junit.ScreenShooter;
import ee.era.hangman.Launcher;
import ee.era.hangman.model.Word;
import ee.era.hangman.model.Words;
import org.junit.*;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.DOM.$;
import static com.codeborne.selenide.DOM.$$;
import static com.codeborne.selenide.Navigation.open;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.junit.ScreenShooter.failedTests;
import static ee.era.hangman.di.DependencyInjection.wire;
import static org.junit.Assert.assertEquals;

public class HangmanSpec_1_9 {
  private static Launcher launcher;
  @Rule
  public ScreenShooter makeScreenshotOnFailure = failedTests().succeededTests();

  @BeforeClass
  public static void startServer() throws Exception {
    wire(Words.class, WordsMock.class);
    launcher = new Launcher(8080);
    launcher.run();
  }

  @AfterClass
  public static void stopServer() {
    if (launcher != null) {
      launcher.stop();
      launcher = null;
    }
  }

  @Before
  public void startGame() {
    open("/hangman");
    $(byText("ENG")).click();
  }

  @Test
  public void showsTopicAndMaskedWordAtTheBeginning() {
    $("#topic").shouldHave(text("house"));
    $("#wordInWork").shouldHave(text("____"));
  }

  @Test
  public void userCanGuessLetters() {
    letter("S").click();
    $("#wordInWork").shouldHave(text("s___"));
    letter("S").shouldHave(cssClass("used"));
  }

  @Test
  public void userWinsWhenAllLettersAreGuessed() {
    letter("S").click();
    letter("O").click();
    letter("F").click();
    letter("A").click();
    $("#gameWin").shouldBe(visible);
  }

  @Test
  public void userHasNoMoreThan6Tries() {
    letter("B").click();
    letter("D").click();
    letter("E").click();
    letter("G").click();
    letter("H").click();
    letter("I").click();
    letter("J").click();
    letter("B").shouldHave(cssClass("nonused"));
    $("#gameLost").shouldBe(visible);
  }

  @Test
  public void userCanChooseLanguage() {
    $(By.linkText("EST")).click();
    $("#topic").shouldHave(text("maja"));
    $("#wordInWork").shouldHave(text("____"));
    assertEquals(27, $$("#alphabet .letter").size());

    $(By.linkText("RUS")).click();
    $("#topic").shouldHave(text("дом"));
    $("#wordInWork").shouldHave(text("______"));
    assertEquals(33, $$("#alphabet .letter").size());

    $(By.linkText("ENG")).click();
    $("#topic").shouldHave(text("house"));
    $("#wordInWork").shouldHave(text("____"));
    assertEquals(26, $$("#alphabet .letter").size());
  }

  private ShouldableWebElement letter(String letter) {
    return $(byText(letter));
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
}
