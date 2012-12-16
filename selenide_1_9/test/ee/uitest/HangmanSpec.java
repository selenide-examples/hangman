package ee.uitest;

import com.codeborne.selenide.ShouldableWebElement;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.DOM.$;
import static com.codeborne.selenide.DOM.$$;
import static com.codeborne.selenide.Navigation.open;
import static com.codeborne.selenide.Selectors.byText;
import static org.junit.Assert.assertEquals;

public class HangmanSpec extends AbstractHangmanTest {
  @Before
  public void startGame() {
    open("/hangman");
    $(byText("ENG")).click();
  }

  @Test
  public void showsTopicAndMaskedWordAtTheBeginning() {
    $("#topic").shouldHave(text("house"));
    $("#wordInWork").shouldHave(text("____"));
  }

  @Test
  public void userCanGuessLetters() {
    letter("S").click();
    $("#wordInWork").shouldHave(text("s___"));
    letter("S").shouldHave(cssClass("used"));
  }

  @Test
  public void userWinsWhenAllLettersAreGuessed() {
    letter("S").click();
    letter("O").click();
    letter("F").click();
    letter("A").click();
    $("#gameWin").shouldBe(visible);
  }

  @Test
  public void userHasNoMoreThan6Tries() {
    letter("B").click();
    letter("D").click();
    letter("E").click();
    letter("G").click();
    letter("H").click();
    letter("I").click();
    letter("J").click();
    letter("B").shouldHave(cssClass("nonused"));
    $("#gameLost").shouldBe(visible);
  }

  @Test
  public void userCanChooseLanguage() {
    $(By.linkText("EST")).click();
    $("#topic").shouldHave(text("maja"));
    $("#wordInWork").shouldHave(text("____"));
    assertEquals(27, $$("#alphabet .letter").size());

    $(By.linkText("RUS")).click();
    $("#topic").shouldHave(text("дом"));
    $("#wordInWork").shouldHave(text("______"));
    assertEquals(33, $$("#alphabet .letter").size());

    $(By.linkText("ENG")).click();
    $("#topic").shouldHave(text("house"));
    $("#wordInWork").shouldHave(text("____"));
    assertEquals(26, $$("#alphabet .letter").size());
  }

  private ShouldableWebElement letter(String letter) {
    return $(byText(letter));
  }
}
