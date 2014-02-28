package ee.uitest.scala

import org.scalatest.{BeforeAndAfter, FlatSpec}
import org.scalatest.Matchers

class TriangleTest extends FlatSpec with BeforeAndAfter with Matchers {

  "Hyphoten" should "be root of legs squares" in {
    hypotenuse(3,4) should equal(5)
    hypotenuse(1,1) should equal(1.4142135623730951)
  }

  private def hypotenuse(legA: Double, legB: Double) = {
    Math.sqrt(legA*legA + legB*legB)
  }
}
