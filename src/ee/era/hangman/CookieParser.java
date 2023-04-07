package ee.era.hangman;

import com.sun.net.httpserver.HttpExchange;

import java.util.stream.Stream;

public class CookieParser {
  public static String parse(HttpExchange exchange, String cookieName) {
    String cookiesHeader = exchange.getRequestHeaders().getFirst("Cookie");
    return parse(cookiesHeader, cookieName);
  }

  public static String parse(String cookiesHeader, String cookieName) {
    return cookiesHeader == null ? null : Stream.of(cookiesHeader.split("\\s*;\\s*"))
      .filter(cookie -> cookie.startsWith(cookieName + "="))
      .map(cookie -> cookie.replaceFirst(".+=(.*)", "$1"))
      .findAny()
      .orElse(null);
  }
}
