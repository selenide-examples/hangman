package ee.era.hangman.actions;

import ee.era.hangman.Request;
import ee.era.hangman.Response;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;

public class StaticFiles {
  public Response index() throws IOException {
    return resource("index.html");
  }

  public Response resource(Request request) throws IOException {
    return resource(request.path());
  }

  private Response resource(String path) throws IOException {
    InputStream resource = Thread.currentThread().getContextClassLoader().getResourceAsStream("webapp/" + path);
    if (resource == null) {
      return Response.error(404, "Not found");
    }

    byte[] content = IOUtils.toByteArray(resource);
    return new Response(200, content, contentTypeOf(path), null);
  }

  private String contentTypeOf(String path) {
    if (path.endsWith(".html")) return "text/html";
    if (path.endsWith(".js")) return "text/javascript";
    if (path.endsWith(".jpg")) return "image/jpeg";
    if (path.endsWith(".jpeg")) return "image/jpeg";
    if (path.endsWith(".png")) return "image/png";
    if (path.endsWith(".ico")) return "image/x-icon";
    if (path.endsWith(".css")) return "text/css";
    throw new IllegalArgumentException("Unsupported content type: " + path);
  }
}
