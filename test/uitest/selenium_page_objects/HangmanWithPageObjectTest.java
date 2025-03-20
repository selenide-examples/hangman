package uitest.selenium_page_objects;

import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.FluentWait;
import uitest.AbstractHangmanTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.openqa.selenium.support.ui.ExpectedConditions.textToBePresentInElement;

public class HangmanWithPageObjectTest extends AbstractHangmanTest {
  @Nullable
  private static WebDriver driver;
  private HangmanPage hangmanPage;

  @BeforeAll
  public static void startBrowser() {
    driver = SeleniumFactory.createDriver();
  }

  @AfterAll
  public static void closeBrowser() {
    if (driver != null) {
      driver.quit();
    }
  }

  @BeforeEach
  public void startGame() {
    driver.get("http://localhost:9999");
    hangmanPage = PageFactory.initElements(driver, HangmanPage.class);
    hangmanPage = hangmanPage.selectLanguage("ENG", "Topic");
  }

  @Test
  public void showsTopic() {
    assertThat(hangmanPage.topic.getText()).isEqualTo("house");
  }

  @Test
  public void showsMaskedWord() {
    assertThat(hangmanPage.wordInWork.getText()).isEqualTo("____");
  }

  @Test
  public void userCanGuessLetters() {
    hangmanPage.guessLetter('S');
    assertThat(hangmanPage.wordInWork.getText()).isEqualTo("s___");
    assertThat(hangmanPage.letter('S').getDomAttribute("class")).contains("used");
  }

  @Test
  public void userWinsWhenAllLettersAreGuessed() {
    hangmanPage.guessLetter('S');
    hangmanPage.guessLetter('O');
    hangmanPage.guessLetter('F');
    hangmanPage.guessLetter('A');
    assertThat(hangmanPage.gameWin.isDisplayed()).isTrue();
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
    assertThat(hangmanPage.letter('B').getDomAttribute("class")).contains("nonused");
    assertThat(hangmanPage.gameLost.isDisplayed()).isTrue();
  }

  @Test
  public void userCanChooseLanguage() {
    hangmanPage = hangmanPage.selectLanguage("EST", "Teema");
    assertThat(hangmanPage.topic.getText()).isEqualTo("maja");
    assertThat(hangmanPage.wordInWork.getText()).isEqualTo("____");
    assertThat(hangmanPage.alphabet).hasSize(27);

    hangmanPage = hangmanPage.selectLanguage("RUS", "Тема");
    new FluentWait<>(driver).until(textToBePresentInElement(hangmanPage.topic, "дом"));
    assertThat(hangmanPage.wordInWork.getText()).isEqualTo("______");
    assertThat(hangmanPage.alphabet).hasSize(33);

    hangmanPage = hangmanPage.selectLanguage("ENG", "Topic");
    new FluentWait<>(driver).until(textToBePresentInElement(hangmanPage.topic, "house"));
    assertThat(hangmanPage.wordInWork.getText()).isEqualTo("____");
    assertThat(hangmanPage.alphabet).hasSize(26);
  }
}
