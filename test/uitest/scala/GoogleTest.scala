package ee.uitest.scala

import org.scalatest.{BeforeAndAfter, FlatSpec}
import org.scalatest.Matchers
import com.codeborne.selenide.Selenide._
import com.codeborne.selenide.Condition._
import org.openqa.selenium.By

class GoogleTest extends FlatSpec with BeforeAndAfter with Matchers {
  "User" can "search for any string" in {
    open("http://google.com")
    $(By.name("q")).setValue("Selenide").pressEnter()
    $("#ires .g:eq(3)", 0).shouldHave(text("Selenide - Wikipedia, the free encyclopedia"))
    $("#ires .g", 1).shouldHave(text("codeborne/selenide"))
  }
}
