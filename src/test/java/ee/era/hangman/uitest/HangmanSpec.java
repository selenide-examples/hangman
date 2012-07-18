package ee.era.hangman.uitest;

import static com.codeborne.selenide.DOM.*;
import static com.codeborne.selenide.Navigation.*;

import ee.era.hangman.Launcher;
import ee.era.hangman.model.Word;
import ee.era.hangman.model.Words;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

import static com.codeborne.selenide.Condition.hasClass;
import static com.codeborne.selenide.Condition.hasText;
import static com.codeborne.selenide.Condition.visible;
import static ee.era.hangman.di.DependencyInjection.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class HangmanSpec {
  @BeforeClass
  public static void startServer() throws Exception {
    wire(Words.class, WordsMock.class);
    new Launcher(8080).run();
    baseUrl = "http://localhost:8080/hangman";
  }

  @Before
  public void startGame() {
    open("/game");
  }

  @Test
  public void showsGameControls() {
    assertTrue(getElement(By.id("topic")).isDisplayed());
    assertTrue(getElement(By.id("wordInWork")).isDisplayed());
    assertTrue(getElement(By.id("alphabet")).isDisplayed());
    assertTrue(getElement(By.id("hangmanImageContainer")).isDisplayed());

    assertThat(getElement(By.id("topic")).getText(), equalTo("дом"));
    assertThat(getElement(By.id("wordInWork")).getText(), equalTo("______"));
  }

  @Test
  public void guessLetterByClickingLetter() {
    getElement(By.xpath("//*[@letter='О']")).click();
    waitUntil(By.xpath("//*[@letter='О']"), hasClass("used"));

    getElement(By.xpath("//*[@letter='Б']")).click();
    waitUntil(By.xpath("//*[@letter='Б']"), hasClass("nonused"));
  }

  @Test
  public void successfulGame() {
    getElement(By.xpath("//*[@letter='О']")).click();
    getElement(By.xpath("//*[@letter='З']")).click();
    getElement(By.xpath("//*[@letter='Д']")).click();
    getElement(By.xpath("//*[@letter='Г']")).click();
    getElement(By.xpath("//*[@letter='В']")).click();
    getElement(By.xpath("//*[@letter='Ь']")).click();
    waitFor(By.id("startGame"));
    assertElement(By.id("gameWin"), visible);
    assertElement(By.id("wordInWork"), hasText("гвоздь"));
  }

  @Test
  public void userCanChooseLanguage() {
    getElement(By.linkText("EST")).click();
    assertEquals(27, alphabetLetters().size());
    assertElement(By.id("topic"), hasText("maja"));
    assertElement(By.id("wordInWork"), hasText("____"));

    getElement(By.linkText("RUS")).click();
    assertEquals(33, alphabetLetters().size());
    assertElement(By.id("topic"), hasText("дом"));
    assertElement(By.id("wordInWork"), hasText("______"));

    getElement(By.linkText("ENG")).click();
    assertEquals(26, alphabetLetters().size());
    assertElement(By.id("topic"), hasText("house"));
    assertElement(By.id("wordInWork"), hasText("____"));
  }

  private List<WebElement> alphabetLetters() {
    return getElements(By.xpath("//*[@id='alphabet']//td"));
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
