package uitest.selenide_page_objects;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uitest.AbstractHangmanTest;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Selenide.open;

public class HangmanSpec extends AbstractHangmanTest {
  private HangmanPage page;

  @BeforeEach
  public void startGame() {
    page = open("/", HangmanPage.class);
    page.selectLanguage("ENG");
    page.chooseUntimedMode();
  }

  @Test
  public void showsMaskedWordAtTheBeginning() {
    page.shouldHaveWord("____");
  }

  @Test
  public void hintRevealsTopicAfterThreeWrongGuesses() {
    page.guessLetter("B");
    page.guessLetter("C");
    page.guessLetter("D");
    page.revealHint();
    page.shouldHaveTopic("house");
  }

  @Test
  public void userCanGuessLetters() {
    page.guessLetter("S");
    page.shouldHaveWord("s___");
    page.assertLetterIsUsed("S");
  }

  @Test
  public void userWinsWhenAllLettersAreGuessed() {
    page.guessLetter("S");
    page.guessLetter("O");
    page.guessLetter("F");
    page.guessLetter("A");
    page.assertGameIsWon();
  }

  @Test
  public void userHasNoMoreThan6Tries() {
    page.guessLetter("B");
    page.guessLetter("D");
    page.guessLetter("E");
    page.guessLetter("G");
    page.guessLetter("H");
    page.guessLetter("I");
    page.guessLetter("J");
    page.assertLetterIsNotUsed("B");
    page.assertGameIsLost();
  }

  @Test
  public void userCanChooseLanguage() {
    page.selectLanguage("EST");
    page.chooseUntimedMode();
    page.shouldHaveWord("____");
    page.alphabet().shouldHave(size(27));

    page.selectLanguage("RUS");
    page.chooseUntimedMode();
    page.shouldHaveWord("______");
    page.alphabet().shouldHave(size(33));

    page.selectLanguage("ENG");
    page.chooseUntimedMode();
    page.shouldHaveWord("____");
    page.alphabet().shouldHave(size(26));
  }
}
