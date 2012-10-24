package ee.era.hangman.uitest;

import com.codeborne.selenide.ShouldableWebElement;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.DOM.$;
import static com.codeborne.selenide.Navigation.open;
import static com.codeborne.selenide.Selectors.byText;

public class TopConfTest extends AbstractHangmanTest {
  @Before
  public void startGame() {
    open("/");
    $(byText("ENG")).click();
  }

  @Test
  public void showsTopic() {
    $("#topic").shouldHave(text("beer parties"));
  }

  @Test
  public void wordIsMaskedAtGameStart() {
    $("#wordInWork").shouldHave(text("______"));
  }

  @Test
  public void userCanGuessLetters() {
    letter('O').click();
    letter('P').click();
    letter('T').click();
    letter('F').click();
    letter('C').click();
    letter('N').click();
    $("#gameWin").shouldBe(visible);
    $("#wordInWork").shouldHave(text("TopConf"));
  }

  private ShouldableWebElement letter(final char letter) {
    return $(By.xpath("//*[@letter='" + letter + "']"));
  }

  @Test
  public void userHasAtLeast6Tries() {
    letter('A').click();
    letter('B').click();
    letter('C').click();
    letter('E').click();
    letter('F').click();
    letter('G').click();
    letter('H').click();
    letter('I').click();
    $("#gameLost").shouldBe(visible);
    $("#wordInWork").shouldHave(text("TopConf"));
  }
}
