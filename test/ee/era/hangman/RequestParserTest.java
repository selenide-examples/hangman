package ee.era.hangman;

import org.junit.jupiter.api.Test;

import static ee.era.hangman.RequestParser.parse;
import static java.util.Map.of;
import static org.assertj.core.api.Assertions.assertThat;

public class RequestParserTest {
  @Test
  public void parsesParametersFromRequestBody() {
    assertThat(parse("letter=%D0%98&foo=bar")).isEqualTo(of("letter", "И", "foo", "bar"));
    assertThat(parse("language=et")).isEqualTo(of("language", "et"));
    assertThat(parse("language=et&et=ämblük&ру=Иван")).isEqualTo(of("language", "et", "et", "ämblük", "ру", "Иван"));
  }
}