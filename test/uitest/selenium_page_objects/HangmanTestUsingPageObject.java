package uitest.selenium_page_objects;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.PageFactory;
import uitest.AbstractHangmanTest;

import static java.lang.Thread.sleep;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class HangmanTestUsingPageObject extends AbstractHangmanTest {
  private static WebDriver driver;
  private HangmanPage hangmanPage;

  @BeforeClass
  public static void startBrowser() {
    String browser = System.getProperty("selenide.browser");
    if ("chrome".equals(browser)) {
      WebDriverManager.chromedriver().setup();
      ChromeOptions options = new ChromeOptions();
      options.setHeadless("true".equals(System.getProperty("selenide.headless")));
      driver = new ChromeDriver(options);
    }
    else if ("firefox".equals(browser)) {
      WebDriverManager.firefoxdriver().setup();
      FirefoxOptions options = new FirefoxOptions();
      options.setHeadless("true".equals(System.getProperty("selenide.headless")));
      driver = new FirefoxDriver(options);
    }
    else if ("edge".equals(browser)) {
      WebDriverManager.edgedriver().setup();
      driver = new EdgeDriver();
    }
    else {
      throw new IllegalArgumentException("Unknown selenide.browser='" + browser + "'");
    }
  }

  @AfterClass
  public static void closeBrowser() {
    if (driver != null) {
      driver.close();
    }
  }

  @Before
  public void startGame() throws InterruptedException {
    driver.get("http://localhost:9999/hangman");
    hangmanPage = PageFactory.initElements(driver, HangmanPage.class);
    hangmanPage.selectLanguage("ENG");
    sleep(1000);
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
    hangmanPage = hangmanPage.selectLanguage("EST");
    assertThat(hangmanPage.topic.getText(), is("maja"));
    assertThat(hangmanPage.wordInWork.getText(), is("____"));
    assertThat(hangmanPage.alphabet.size(), is(27));

    hangmanPage = hangmanPage.selectLanguage("RUS");
    assertThat(hangmanPage.topic.getText(), is("дом"));
    assertThat(hangmanPage.wordInWork.getText(), is("______"));
    assertThat(hangmanPage.alphabet.size(), is(33));

    hangmanPage = hangmanPage.selectLanguage("ENG");
    assertThat(hangmanPage.topic.getText(), is("house"));
    assertThat(hangmanPage.wordInWork.getText(), is("____"));
    assertThat(hangmanPage.alphabet.size(), is(26));
  }
}
