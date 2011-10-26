package ee.era.hangman.uitest;

import com.github.selenide.UITest;
import ee.era.hangman.model.Words;
import org.junit.Test;
import org.openqa.selenium.By;

import static ee.era.hangman.di.DependencyInjection.*;
import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class HangmanSpec extends UITest {
  @Test
  public void showsGameControls() {
    wire(Words.class, WordsMock.class);
    open("/game");

    assertTrue(webdriver.findElement(By.id("topic")).isDisplayed());
    assertTrue(webdriver.findElement(By.id("wordInWork")).isDisplayed());
    assertTrue(webdriver.findElement(By.id("alphabet")).isDisplayed());
    assertTrue(webdriver.findElement(By.id("hangmanImageContainer")).isDisplayed());

    assertThat(webdriver.findElement(By.id("topic")).getText(), equalTo("дом"));
    assertThat(webdriver.findElement(By.id("wordInWork")).getText(), equalTo("______"));
  }

  public static class WordsMock extends Words {
    public WordsMock() {
      topics.clear();
      topics.put("дом", asList("гвоздь"));
    }
  }
}
