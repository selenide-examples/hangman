package uitest.selenide;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.junit.ScreenShooter;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.openqa.selenium.By;
import uitest.AbstractHangmanTest;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;

public class HangmanSpec extends AbstractHangmanTest {
  @Rule
  public ScreenShooter makeScreenshotOnFailure = ScreenShooter.failedTests().succeededTests();

  @Before
  public void startGame() {
    open("/hangman");
    $(byText("ENG")).click();
  }

  @Test
  public void showsTopicAndMaskedWordAtTheBeginning() {
    $("#topic").shouldHave(Condition.text("house"));
    $("#wordInWork").shouldHave(Condition.text("____"));
  }

  @Test
  public void userCanGuessLetters() {
    letter("S").click();
    $("#wordInWork").shouldHave(Condition.text("s___"));
    letter("S").shouldHave(Condition.cssClass("used"));
  }

  @Test
  public void userWinsWhenAllLettersAreGuessed() {
    letter("S").click();
    letter("O").click();
    letter("F").click();
    letter("A").click();
    $("#gameWin").shouldBe(Condition.visible);
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
    letter("B").shouldHave(Condition.cssClass("nonused"));
    $("#gameLost").shouldBe(Condition.visible);
  }

  @Test
  public void userCanChooseLanguage() {
    $(By.linkText("EST")).click();
    $("#topic").shouldHave(Condition.text("maja"));
    $("#wordInWork").shouldHave(Condition.text("____"));
    Assert.assertEquals(27, $$("#alphabet .letter").size());

    $(By.linkText("RUS")).click();
    $("#topic").shouldHave(Condition.text("дом"));
    $("#wordInWork").shouldHave(Condition.text("______"));
    Assert.assertEquals(33, $$("#alphabet .letter").size());

    $(By.linkText("ENG")).click();
    $("#topic").shouldHave(Condition.text("house"));
    $("#wordInWork").shouldHave(Condition.text("____"));
    Assert.assertEquals(26, $$("#alphabet .letter").size());
  }

  private SelenideElement letter(String letter) {
    return $(byText(letter));
  }
}
