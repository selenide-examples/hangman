package ee.era.hangman;

import com.sun.net.httpserver.HttpExchange;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.io.IOException;
import java.util.UUID;
import java.util.function.Function;

import static ee.era.hangman.RequestParser.parseQuery;

@ParametersAreNonnullByDefault
public class Request {
  private final HttpExchange exchange;
  private final Function<String, Session> sessionProvider;

  Request(HttpExchange exchange, Function<String, Session> sessionProvider) {
    this.exchange = exchange;
    this.sessionProvider = sessionProvider;
  }
  
  public String getMethod() {
    return exchange.getRequestMethod();
  }

  public Session getSession() {
    String sessionId = getSessionId();
    if (sessionId == null) throw new SessionException("Session ID not provided");
    Session session = sessionProvider.apply(sessionId);
    if (session == null) throw new SessionException("Session not found");
    if (session.isExpired()) throw new SessionException("Session is expired");
    session.updateTimestamp();
    return session;
  }

  @Nullable
  public String getSessionId() {
    return CookieParser.parse(exchange, "SID");
  }

  @Nullable
  public String queryParam(String name) {
    return parseQuery(exchange).get(name);
  }

  public String param(String name) throws IOException {
    return RequestParser.parse(exchange).get(name);
  }

  public String path() {
    return exchange.getRequestURI().getPath();
  }

  public Session getOrCreateSession() {
    try {
      return getSession();
    }
    catch (SessionException ignore) {
      return new Session(UUID.randomUUID().toString());
    }
  }
}
