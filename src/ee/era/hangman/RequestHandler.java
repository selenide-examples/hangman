package ee.era.hangman;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ee.era.hangman.actions.Game;
import ee.era.hangman.actions.Guess;
import ee.era.hangman.actions.Setup;
import ee.era.hangman.actions.StaticFiles;
import ee.era.hangman.model.WordsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

class RequestHandler implements HttpHandler {
  private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);
  private final Map<String, Session> sessions = new HashMap<>();
  private final StaticFiles staticFiles = new StaticFiles();
  private final Setup setup = new Setup();
  private final Game game;
  private final Guess guess = new Guess();

  RequestHandler(WordsService wordsService) {
    game = new Game(wordsService);
  }

  @Override
  public void handle(HttpExchange exchange) {
    logRequest(exchange);
    Request request = new Request(exchange, sessions::get);

    Response response = routeRequest(request);
    try {
      sendResponse(exchange, response);
    }
    catch (Exception e) {
      log.error("Failed to handle {} {}", exchange.getRequestMethod(), exchange.getRequestURI().getPath(), e);
    }
    finally {
      log(request, exchange);
      exchange.close();
    }
  }

  private Response routeRequest(Request request) {
    try {
      return switch (request.getMethod()) {
        case "GET" -> routeGet(request);
        case "POST" -> routePost(request);
        default -> Response.error(404, "Not found");
      };
    }
    catch (SessionException e) {
      log.info("Problem with session when handling {} {}: {}", request.getMethod(), request.path(), e.getMessage());
      return Response.error(401, "Unauthenticated");
    }
    catch (Exception e) {
      log.error("Failed to handle {} {}", request.getMethod(), request.path(), e);
      return Response.error(500, "Server error");
    }
  }

  private Response routeGet(Request request) throws IOException {
    return switch (request.path()) {
      case "/" -> staticFiles.index();
      case "/init" -> setup.init(request);
      case "/game" -> game.startGame(request);
      default -> staticFiles.resource(request);
    };
  }

  private Response routePost(Request request) throws IOException {
    return switch (request.path()) {
      case "/guess" -> guess.guessLetter(request);
      default -> Response.error(404, "Not found");
    };
  }

  private void sendResponse(HttpExchange exchange, Response response) throws IOException {
    if (response.session != null) {
      sessions.put(response.session.id(), response.session);
      String time = LocalDateTime.now().plusYears(100).atZone(ZoneId.of("GMT")).format(DateTimeFormatter.RFC_1123_DATE_TIME);
      String secure = ""; // TODO Change to "Secure; " if server is run on https
      exchange.getResponseHeaders().add("Set-Cookie", "SID=%s; Expires=%s; %sHttpOnly".formatted(response.session.id(), time, secure));
    }

    if (response.body == null) {
      exchange.sendResponseHeaders(response.statusCode, -1);
    }
    else {
      exchange.getResponseHeaders().add("Content-Type", response.contentType);
      exchange.sendResponseHeaders(response.statusCode, response.body.length);
      try (OutputStream responseBody = exchange.getResponseBody()) {
        responseBody.write(response.body);
      }
    }
  }

  private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
  private static final AtomicLong counter = new AtomicLong(0);

  private void logRequest(HttpExchange exchange) {
    long requestId = counter.incrementAndGet();
    exchange.setAttribute("requestId", requestId);
    log.info("[{}] {} {} {}", requestId, LocalDateTime.now().format(formatter), exchange.getRequestMethod(), exchange.getRequestURI());
  }

  private void log(Request request, HttpExchange exchange) {
    long requestId = (long) exchange.getAttribute("requestId");
    System.out.println("[" + requestId + "] " +
                       LocalDateTime.now().format(formatter) + " "
                       + request.getSessionId() + " "
                       + exchange.getRequestMethod() + " "
                       + exchange.getRequestURI() + " --> "
                       + exchange.getResponseCode());
  }
}
