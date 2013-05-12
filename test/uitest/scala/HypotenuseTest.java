package ee.uitest.scala;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class HypotenuseTest {
  @Test
  public void hypotenuseIsRootOfLegsSquares() {
    assertEquals(5, hypotenuse(3, 4), 0.000001);
    assertEquals(1.4142135623730951, hypotenuse(1, 1), 0.000001);
  }

  private double hypotenuse(double legA, double legB) {
    return Math.sqrt(legA*legA + legB*legB);
  }
}
