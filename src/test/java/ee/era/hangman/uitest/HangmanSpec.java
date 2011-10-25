package ee.era.hangman.uitest;

import com.github.selenide.UITest;
import org.junit.Test;
import org.openqa.selenium.By;

import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class HangmanSpec extends UITest {
  @Test
  public void showsGameControls() {
    open("/game");

    assertTrue(webdriver.findElement(By.id("topic")).isDisplayed());
    assertTrue(webdriver.findElement(By.id("wordInWork")).isDisplayed());
    assertTrue(webdriver.findElement(By.id("alphabet")).isDisplayed());
    assertTrue(webdriver.findElement(By.id("hangmanImageContainer")).isDisplayed());

//    assertThat(webdriver.findElement(By.id("topic")).getText(), equalTo("дом"));
//    assertThat(webdriver.findElement(By.id("wordInWork")).getText(), equalTo("______"));
  }
}
