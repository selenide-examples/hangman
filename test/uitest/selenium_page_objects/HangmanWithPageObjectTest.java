package uitest.selenium_page_objects;

import org.jspecify.annotations.Nullable;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.FluentWait;
import uitest.AbstractHangmanTest;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.openqa.selenium.support.ui.ExpectedConditions.textToBePresentInElement;

public class HangmanWithPageObjectTest extends AbstractHangmanTest {
  @Nullable
  private static WebDriver driver;
  private HangmanPage hangmanPage;

  @BeforeClass
  public static void startBrowser() {
    driver = SeleniumFactory.createDriver();
  }

  @AfterClass
  public static void closeBrowser() {
    if (driver != null) {
      driver.quit();
    }
  }

  @Before
  public void startGame() {
    driver.get("http://localhost:9999");
    hangmanPage = PageFactory.initElements(driver, HangmanPage.class);
    hangmanPage = hangmanPage.selectLanguage("ENG", "Topic");
  }

  @Test
  public void showsTopic() {
    assertThat(hangmanPage.topic.getText(), is("house"));
  }

  @Test
  public void showsMaskedWord() {
    assertThat(hangmanPage.wordInWork.getText(), is("____"));
  }

  @Test
  public void userCanGuessLetters() {
    hangmanPage.guessLetter('S');
    assertThat(hangmanPage.wordInWork.getText(), is("s___"));
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
  public void userCanChooseLanguage() {
    hangmanPage = hangmanPage.selectLanguage("EST", "Teema");
    assertThat(hangmanPage.topic.getText(), is("maja"));
    assertThat(hangmanPage.wordInWork.getText(), is("____"));
    assertThat(hangmanPage.alphabet.size(), is(27));

    hangmanPage = hangmanPage.selectLanguage("RUS", "Тема");
    new FluentWait<>(driver).until(textToBePresentInElement(hangmanPage.topic, "дом"));
    assertThat(hangmanPage.wordInWork.getText(), is("______"));
    assertThat(hangmanPage.alphabet.size(), is(33));

    hangmanPage = hangmanPage.selectLanguage("ENG", "Topic");
    new FluentWait<>(driver).until(textToBePresentInElement(hangmanPage.topic, "house"));
    assertThat(hangmanPage.wordInWork.getText(), is("____"));
    assertThat(hangmanPage.alphabet.size(), is(26));
  }
}
