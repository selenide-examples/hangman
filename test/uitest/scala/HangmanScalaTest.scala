package uitest.scala

import com.codeborne.selenide.Condition._
import com.codeborne.selenide.Selenide._
import com.codeborne.selenide.Selectors._
import com.codeborne.selenide.SelenideElement
import org.scalatest.{FlatSpec, BeforeAndAfter}
import org.openqa.selenium.By
import org.scalatest.Matchers

class HangmanScalaTest extends FlatSpec with BeforeAndAfter with Matchers {
  before {
    open("/hangman")
    $(byText("ENG")).click
  }

  "User" can "see a topic and masked word at the beginning" in {
    $("#topic") shouldHave text("house")
    $("#wordInWork") shouldHave(text("____"))
  }

  "User" can "guess letters" in {
    letter("S").click
    $("#wordInWork") shouldHave text("s___")
    letter("S") shouldHave cssClass("used")
  }

  "User" can "win when all letters are guessed" in {
    //with implicit sugar
    "S" click()
    "O" click()
    "F" click()
    "A" click()
    $("#gameWin") shouldBe(visible)
  }

  "User" can "user no ore than 6 tries" in {
    letter("B").click
    letter("D").click
    letter("E").click
    letter("G").click
    letter("H").click
    letter("I").click
    letter("J").click
    letter("B") shouldHave cssClass("nonused")
    $("#gameLost") shouldBe(visible)
  }

  "User" can "choose language" in {
    $(By.linkText("EST")).click
    $("#topic") shouldHave text("maja")
    $("#wordInWork") shouldHave text("____")
    $$("#alphabet .letter").size should equal(27)

    $(By.linkText("RUS")).click
    $("#topic") shouldHave text("дом")
    $("#wordInWork") shouldHave text("______")
    $$("#alphabet .letter").size should equal(33)

    $(By.linkText("ENG")).click
    $("#topic") shouldHave text("house")
    $("#wordInWork") shouldHave text("____")
    $$("#alphabet .letter").size should equal(26)
  }

  private def letter(letter: String): SelenideElement = $(byText(letter))
  implicit private def stringToSelenideElement(l: String): SelenideElement = letter(l)
}