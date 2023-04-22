package uitest.selenium_page_objects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

import static java.time.Duration.ofSeconds;
import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated;

public class HangmanPage {
  private final WebDriver driver;

  @FindBy(id = "topic")
  @CacheLookup
  WebElement topic;

  @FindBy(id = "topic-label")
  @CacheLookup
  WebElement topicLabel;

  @FindBy(id = "wordInWork")
  @CacheLookup
  WebElement wordInWork;

  @FindBy(css = "#alphabet .letter")
  @CacheLookup
  List<WebElement> alphabet;

  @FindBy(id = "gameWin")
  WebElement gameWin;

  @FindBy(id = "gameLost")
  WebElement gameLost;

  public HangmanPage(WebDriver driver) {
    this.driver = driver;
    new WebDriverWait(driver, ofSeconds(3)).until(visibilityOfElementLocated(By.id("topic")));
  }

  public void guessLetter(char letter) {
    letter(letter).click();
  }

  public WebElement letter(char letter) {
    return driver.findElement(By.xpath("//*[@letter='" + letter + "']"));
  }

  public HangmanPage selectLanguage(String language, String expectedTopicLabel) {
    driver.findElement(By.linkText(language)).click();
    HangmanPage newPage = PageFactory.initElements(driver, HangmanPage.class);
    new FluentWait<>(newPage.topicLabel).until(e -> e.isDisplayed() && e.getText().equals(expectedTopicLabel));
    return newPage;
  }
}
