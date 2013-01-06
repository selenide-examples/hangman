package ee.uitest;

import ee.era.hangman.Launcher;
import ee.era.hangman.model.Word;
import ee.era.hangman.model.Words;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.support.PageFactory;

import static ee.era.hangman.di.DependencyInjection.wire;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class HangmanTestUsingPageObject {
  private static WebDriver driver;
  private HangmanPage hangmanPage;

  private static Launcher launcher;

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


  @BeforeClass
  public static void startBrowser() {
    driver = new HtmlUnitDriver();
//    driver = new FirefoxDriver();
  }

  @AfterClass
  public static void closeBrowser() {
    driver.close();
  }

  @Before
  public void startGame() throws InterruptedException {
    driver.get("http://localhost:8080/hangman");
    hangmanPage = PageFactory.initElements(driver, HangmanPage.class);
    hangmanPage.selectLanguage("ENG");
    Thread.sleep(1000);
  }

  @Test
  public void showsTopic() {
    assertEquals("house", hangmanPage.topic.getText());
  }

  @Test
  public void showsMaskedWord() {
    assertEquals("____", hangmanPage.wordInWork.getText());
  }

  @Test
  public void userCanGuessLetters() throws InterruptedException {
    hangmanPage.guessLetter('S');
    assertEquals("s___", hangmanPage.wordInWork.getText());
    assertThat(hangmanPage.letter('S').getAttribute("class"), containsString("used"));
  }

  @Test
  public void userWinsWhenAllLettersAreGuessed() {
    hangmanPage.guessLetter('S');
    hangmanPage.guessLetter('O');
    hangmanPage.guessLetter('F');
    hangmanPage.guessLetter('A');
    assertThat(hangmanPage.gameWin.isDisplayed(), is(true));
  }

  @Test
  public void userHasNoMoreThan6Tries() {
    hangmanPage.guessLetter('B');
    hangmanPage.guessLetter('D');
    hangmanPage.guessLetter('E');
    hangmanPage.guessLetter('G');
    hangmanPage.guessLetter('H');
    hangmanPage.guessLetter('I');
    hangmanPage.guessLetter('J');
    assertThat(hangmanPage.letter('B').getAttribute("class"), containsString("nonused"));
    assertThat(hangmanPage.gameLost.isDisplayed(), is(true));
  }

  @Test
  public void userCanChooseLanguage() throws InterruptedException {
    hangmanPage = hangmanPage.selectLanguage("EST");
    assertEquals("maja", hangmanPage.topic.getText());
    assertEquals("____", hangmanPage.wordInWork.getText());
    assertEquals(27, hangmanPage.alphabet.size());

    hangmanPage = hangmanPage.selectLanguage("RUS");
    assertEquals("дом", hangmanPage.topic.getText());
    assertEquals("______", hangmanPage.wordInWork.getText());
    assertEquals(33, hangmanPage.alphabet.size());

    hangmanPage = hangmanPage.selectLanguage("ENG");
    assertEquals("house", hangmanPage.topic.getText());
    assertEquals("____", hangmanPage.wordInWork.getText());
    assertEquals(26, hangmanPage.alphabet.size());
  }
}
