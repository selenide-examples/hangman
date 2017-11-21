package uitest.selenide_page_objects;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.cssClass;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class HangmanPage {
  public void selectLanguage(String language) {
    $(byText(language)).click();
  }

  public void shouldHaveTopic(String topic) {
    $("#topic").shouldHave(text(topic));
  }

  public void shouldHaveWord(String word) {
    $("#wordInWork").shouldHave(text(word));
  }

  public void guessLetter(String letter) {
    letter(letter).click();
  }

  public void assertLetterIsUsed(String letter) {
    letter(letter).shouldHave(cssClass("used"));
  }

  public void assertLetterIsNotUsed(String letter) {
    letter(letter).shouldHave(cssClass("nonused"));
  }

  private SelenideElement letter(String letter) {
    return $(byText(letter));
  }

  public void assertGameIsWon() {
    $("#gameWin").shouldBe(visible);
  }

  public void assertGameIsLost() {
    $("#gameLost").shouldBe(visible);
  }

  public ElementsCollection alphabet() {
    return $$("#alphabet .letter");
  }
}
