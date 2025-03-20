package ee.era.hangman;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class CookieParserTest {
  @Test
  public void parsesCookieValueFromHeader() {
    assertThat(CookieParser.parse("SID=423fd; SAD=423fdb1e", "SID")).isEqualTo("423fd");
    assertThat(CookieParser.parse("SID=423fd; SAD=423fdb1e", "SAD")).isEqualTo("423fdb1e");
  }

  @Test
  public void returnsNullIfCookieIsMissing() {
    assertThat(CookieParser.parse("SID=423fd; SAD=423fdb1e", "Another")).isNull();
    assertThat(CookieParser.parse("", "Another")).isNull();
    assertThat(CookieParser.parse((String) null, "Another")).isNull();
  }
}