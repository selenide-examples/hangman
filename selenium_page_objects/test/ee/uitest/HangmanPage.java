package ee.uitest;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.List;

public class HangmanPage {
  private final WebDriver driver;

  @FindBy(id = "topic")
  @CacheLookup
  WebElement topic;

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
  }

  public void guessLetter(char letter) {
    letter(letter).click();
  }

  public WebElement letter(char letter) {
    return driver.findElement(By.xpath("//*[@letter='" + letter + "']"));
  }

  public HangmanPage selectLanguage(String language) {
    driver.findElement(By.linkText(language)).click();
    return PageFactory.initElements(driver, HangmanPage.class);
  }
}
