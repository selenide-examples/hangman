package ee.era.hangman;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jspecify.annotations.Nullable;

import static java.nio.charset.StandardCharsets.UTF_8;

public class Response {

  public final int statusCode;
  public final byte[] body;
  public final String contentType;

  @Nullable
  public final Session session;

  public Response(int statusCode, byte[] body, String contentType, @Nullable Session session) {
    this.statusCode = statusCode;
    this.body = body;
    this.contentType = contentType;
    this.session = session;
  }

  public Response(int statusCode, String body, String contentType, @Nullable Session session) {
    this(statusCode, body.getBytes(UTF_8), contentType, session);
  }

  public static Response error(int statusCode, String responseBody) {
    return new Response(statusCode, responseBody, "text/plain", null);
  }

  public static Response json(int statusCode, Object responseBody) {
    return json(statusCode, responseBody, null);
  }

  public static Response json(int statusCode, Object responseBody, @Nullable Session session) {
    try {
      return new Response(statusCode, new ObjectMapper().writeValueAsString(responseBody), "application/json", session);
    }
    catch (JsonProcessingException e) {
      throw new RuntimeException("Failed to convert object to json", e);
    }
  }
}
