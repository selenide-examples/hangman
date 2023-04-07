package ee.era.hangman.actions;

import ee.era.hangman.Request;
import ee.era.hangman.Response;
import ee.era.hangman.Session;

import javax.annotation.ParametersAreNonnullByDefault;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

import static ee.era.hangman.Response.json;
import static java.util.Objects.requireNonNullElse;

@ParametersAreNonnullByDefault
public class Setup {

  private static final String DEFAULT_LANGUAGE = "en";

  public Response init(Request request) {
    Session session = request.getOrCreateSession();

    String language = requireNonNullElse(
      request.queryParam("language"),
      requireNonNullElse(session.getLanguage(), DEFAULT_LANGUAGE)
    );
    session.setLanguage(language);

    return json(200, Map.of(
      "i18n", loadMessages(language)
    ), session);
  }

  private Properties loadMessages(String language) {
    String file = String.format("messages_%s.properties", language);
    try (InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream(file)) {
      Properties p = new Properties();
      p.load(in);
      return p;
    }
    catch (IOException e) {
      throw new IllegalArgumentException("Messages file not found: " + file);
    }
  }

}
