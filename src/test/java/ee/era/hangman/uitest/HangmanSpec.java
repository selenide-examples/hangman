package ee.era.hangman.uitest;

import com.codeborne.selenide.junit.ScreenShooter;
import ee.era.hangman.Launcher;
import ee.era.hangman.model.Word;
import ee.era.hangman.model.Words;
import org.junit.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.DOM.*;
import static com.codeborne.selenide.Navigation.baseUrl;
import static com.codeborne.selenide.Navigation.open;
import static com.codeborne.selenide.junit.ScreenShooter.failedTests;
import static ee.era.hangman.di.DependencyInjection.wire;
import static org.junit.Assert.assertEquals;

public class HangmanSpec {
  @Rule
  public ScreenShooter makeScreenshotOnFailure = failedTests().succeededTests();

  private static Launcher launcher;
  @BeforeClass
  public static void startServer() throws Exception {
    wire(Words.class, WordsMock.class);
    launcher = new Launcher(8080);
    launcher.run();
    baseUrl = "http://localhost:8080/hangman";
  }

  @AfterClass
  public static void stopServer() {
    launcher.stop();
  }

  @Before
  public void startGame() {
    open("/game");
  }

  @Test
  public void showsGameControls() {
    $("#topic").shouldBe(visible);
    $("#wordInWork").shouldBe(visible);
    $("#alphabet").shouldBe(visible);
    $("#hangmanImageContainer").shouldBe(visible);

    $("#topic").shouldHave(text("дом"));
    $("#wordInWork").shouldHave(text("______"));
  }

  @Test
  public void guessLetterByClickingLetter() {
    $(By.xpath("/*//*[@letter='О']")).click();
    waitUntil(By.xpath("/*//*[@letter='О']"), hasClass("used"));

    $(By.xpath("/*//*[@letter='Б']")).click();
    waitUntil(By.xpath("/*//*[@letter='Б']"), hasClass("nonused"));
  }

  @Test
  public void successfulGame() {
    $(By.xpath("/*//*[@letter='О']")).click();
    $(By.xpath("/*//*[@letter='З']")).click();
    $(By.xpath("/*//*[@letter='Д']")).click();
    $(By.xpath("/*//*[@letter='Г']")).click();
    $(By.xpath("/*//*[@letter='В']")).click();
    $(By.xpath("/*//*[@letter='Ь']")).click();
    waitFor("#startGame");
    $("#gameWin").shouldBe(visible);
    $("#wordInWork").shouldHave(text("гвоздь"));
  }

  @Test
  public void userCanChooseLanguage() {
    $(By.linkText("EST")).click();
    assertEquals(27, alphabetLetters().size());
    $("#topic").shouldHave(text("maja"));
    $("#wordInWork").shouldHave(text("____"));

    $(By.linkText("RUS")).click();
    assertEquals(33, alphabetLetters().size());
    $("#topic").shouldHave(text("дом"));
    $("#wordInWork").shouldHave(text("______"));

    $(By.linkText("ENG")).click();
    assertEquals(26, alphabetLetters().size());
    $("#topic").shouldHave(text("house"));
    $("#wordInWork").shouldHave(text("____"));
  }

  private List<WebElement> alphabetLetters() {
    return $$("#alphabet td");
  }

  public static class WordsMock extends Words {
    @Override
    public Word getRandomWord(String language) {
      if ("rus".equals(language))
        return new Word("дом", "гвоздь");
      if ("est".equals(language))
        return new Word("maja", "nael");
      return new Word("house", "nail");
    }
  }
}
