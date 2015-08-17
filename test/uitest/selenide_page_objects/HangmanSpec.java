package uitest.selenide_page_objects;

import org.junit.Before;
import org.junit.Test;
import uitest.AbstractHangmanTest;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Selenide.open;

public class HangmanSpec extends AbstractHangmanTest {
  HangmanPage page;
  
  @Before
  public void startGame() {
    page = open("/hangman", HangmanPage.class);
    page.selectLanguage("ENG");
  }

  @Test
  public void showsTopicAndMaskedWordAtTheBeginning() {
    page.shouldHaveTopic("house");
    page.shouldHaveWord("____");    
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
    page.shouldHaveTopic("maja");
    page.shouldHaveWord("____");
    page.alphabet().shouldHave(size(27));

    page.selectLanguage("RUS");
    page.shouldHaveTopic("дом");
    page.shouldHaveWord("______");
    page.alphabet().shouldHave(size(33));

    page.selectLanguage("ENG");
    page.shouldHaveTopic("house");
    page.shouldHaveWord("____");
    page.alphabet().shouldHave(size(26));
  }
}
