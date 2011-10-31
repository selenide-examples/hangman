package ee.era.hangman.uitest;

import com.github.selenide.Condition;
import com.github.selenide.UITest;
import ee.era.hangman.model.Word;
import ee.era.hangman.model.Words;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;

import static com.github.selenide.Condition.hasClass;
import static com.github.selenide.Condition.hasText;
import static com.github.selenide.Condition.visible;
import static ee.era.hangman.di.DependencyInjection.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class HangmanSpec extends UITest {
  @Before
  public void startGame() {
    wire(Words.class, WordsMock.class);
    open("/game");
  }

  @Test
  public void showsGameControls() {
    assertTrue(webdriver.findElement(By.id("topic")).isDisplayed());
    assertTrue(webdriver.findElement(By.id("wordInWork")).isDisplayed());
    assertTrue(webdriver.findElement(By.id("alphabet")).isDisplayed());
    assertTrue(webdriver.findElement(By.id("hangmanImageContainer")).isDisplayed());

    assertThat(webdriver.findElement(By.id("topic")).getText(), equalTo("дом"));
    assertThat(webdriver.findElement(By.id("wordInWork")).getText(), equalTo("______"));
  }

  @Test
  public void guessLetterByClickingLetter() {
    getElement(By.xpath("//*[@letter='О']")).click();
    waitFor(By.xpath("//*[@letter='О']"), hasClass("used"));

    getElement(By.xpath("//*[@letter='Б']")).click();
    waitFor(By.xpath("//*[@letter='Б']"), hasClass("nonused"));
  }

  @Test
  public void successfulGame() {
    getElement(By.xpath("//*[@letter='О']")).click();
    getElement(By.xpath("//*[@letter='З']")).click();
    getElement(By.xpath("//*[@letter='Д']")).click();
    getElement(By.xpath("//*[@letter='Г']")).click();
    getElement(By.xpath("//*[@letter='В']")).click();
    getElement(By.xpath("//*[@letter='Ь']")).click();
    waitFor(By.id("startGame"), visible);
    assertElement(By.id("gameWin"), visible);
    assertElement(By.id("wordInWork"), hasText("гвоздь"));
  }

  public static class WordsMock extends Words {
    @Override
    public Word getRandomWord() {
      return new Word("дом", "гвоздь");
    }
  }
}
