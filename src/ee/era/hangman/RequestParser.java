package ee.era.hangman;

import com.sun.net.httpserver.HttpExchange;
import org.jspecify.annotations.Nullable;

import java.io.IOException;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static java.net.URLDecoder.decode;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Collections.emptyMap;
import static java.util.stream.Collectors.toMap;

public class RequestParser {
  private static final Pattern regex = Pattern.compile("(.+)=(.*)");

  public static Map<String, String> parseQuery(HttpExchange exchange) {
    return parseQuery(exchange.getRequestURI().getQuery());
  }

  static Map<String, String> parseQuery(String query) {
    return parse(query);
  }

  public static Map<String, String> parse(HttpExchange exchange) throws IOException {
    return parse(new String(exchange.getRequestBody().readAllBytes(), UTF_8));
  }

  static Map<String, String> parse(@Nullable String requestBody) {
    return requestBody == null ? emptyMap() : Stream.of(requestBody.split("&"))
      .map(regex::matcher)
      .collect(toMap(
        matcher -> decode(matcher.replaceFirst("$1"), UTF_8),
        matcher -> decode(matcher.replaceFirst("$2"), UTF_8)
      ));
  }
}
