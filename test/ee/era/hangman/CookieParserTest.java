package ee.era.hangman;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class CookieParserTest {
  @Test
  public void parsesCookieValueFromHeader() {
    assertEquals("423fd", CookieParser.parse("SID=423fd; SAD=423fdb1e", "SID"));
    assertEquals("423fdb1e", CookieParser.parse("SID=423fd; SAD=423fdb1e", "SAD"));
  }

  @Test
  public void returnsNullIfCookieIsMissing() {
    assertNull(CookieParser.parse("SID=423fd; SAD=423fdb1e", "Another"));
    assertNull(CookieParser.parse("", "Another"));
    assertNull(CookieParser.parse((String) null, "Another"));
  }
}