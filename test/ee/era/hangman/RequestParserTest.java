package ee.era.hangman;

import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertEquals;

public class RequestParserTest {
  @Test
  public void parsesParametersFromRequestBody() {
    assertEquals(Map.of("letter", "И", "foo", "bar"), RequestParser.parse("letter=%D0%98&foo=bar"));
    assertEquals(Map.of("language", "et"), RequestParser.parse("language=et"));
    assertEquals(Map.of("language", "et", "et", "ämblük", "ру", "Иван"), RequestParser.parse("language=et&et=ämblük&ру=Иван"));
  }
}