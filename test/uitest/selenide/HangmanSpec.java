package uitest.selenide;

import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import uitest.AbstractHangmanTest;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Condition.cssClass;
import static com.codeborne.selenide.Condition.cssValue;
import static com.codeborne.selenide.Condition.disabled;
import static com.codeborne.selenide.Condition.enabled;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.Selenide.open;

public class HangmanSpec extends AbstractHangmanTest {
  @BeforeEach
  public void startGame() {
    open("/");
    $(byText("ENG")).click();
    $("#modeUntimed").shouldBe(visible).click();
  }

  @Test
  public void showsMaskedWordAndLockedHintAtTheBeginning() {
    $("#wordInWork").shouldHave(text("____"));
    $("#hintButton").shouldBe(disabled);
    $("#topic").shouldNotBe(visible);
  }

  @Test
  public void hintUnlocksAfterThreeWrongGuessesAndRevealsTopic() {
    letter("B").click();
    letter("C").click();
    $("#hintButton").shouldBe(disabled);
    letter("D").click();
    $("#hintButton").shouldBe(enabled).click();
    $("#topic").shouldBe(visible).shouldHave(text("house"));
  }

  @Test
  public void gallowsBuildsUpWithEachWrongGuess() {
    $("#gallows1").shouldHave(cssValue("display", "none"));
    letter("B").click();
    $("#gallows1").shouldNotHave(cssValue("display", "none"));
    $("#gallows2").shouldHave(cssValue("display", "none"));
    letter("C").click();
    $("#gallows2").shouldNotHave(cssValue("display", "none"));
    letter("D").click();
    $("#gallows3").shouldNotHave(cssValue("display", "none"));
    letter("E").click();
    $("#noose").shouldNotHave(cssValue("display", "none"));
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
  public void timedModeShowsCountdown() {
    open("/");
    $(byText("ENG")).click();
    $("#modeTimed").shouldBe(visible).click();
    $("#timer").shouldBe(visible);
  }

  @Test
  public void howToPlayGuideOpensAndCloses() {
    $("#howToPlay").click();
    $("#howToPlayModal").shouldBe(visible);
    $("#howToPlayClose").click();
    $("#howToPlayModal").shouldNotBe(visible);
  }

  @Test
  public void userCanChooseLanguage() {
    $(By.linkText("EST")).click();
    $("#modeUntimed").shouldBe(visible).click();
    $("#wordInWork").shouldHave(text("____"));
    $$("#alphabet .letter").shouldHave(size(27));

    $(By.linkText("RUS")).click();
    $("#modeUntimed").shouldBe(visible).click();
    $("#wordInWork").shouldHave(text("______"));
    $$("#alphabet .letter").shouldHave(size(33));

    $(By.linkText("ENG")).click();
    $("#modeUntimed").shouldBe(visible).click();
    $("#wordInWork").shouldHave(text("____"));
    $$("#alphabet .letter").shouldHave(size(26));
  }

  private SelenideElement letter(String letter) {
    return $("#alphabet").$(byText(letter));
  }
}
