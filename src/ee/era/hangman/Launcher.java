package ee.era.hangman;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.webapp.WebAppContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class Launcher {
  private static final Logger log = LoggerFactory.getLogger(Launcher.class);
  
  private final int port;
  private final Server server;

  public Launcher(String environment, int port) {
    GuiceListener.environment = environment;
    this.port = port;
    server = new Server(port);
  }

  public void run() throws Exception {
    log.info("Start jetty launcher at {}", port);
    log.info("Start hangman webapp at {}", new File("webapp").getAbsolutePath());

    HandlerCollection webapps = new HandlerCollection();
    webapps.addHandler(new WebAppContext("webapp", "/hangman"));
    server.setHandler(webapps);

    addShutdownHook();
    server.start();
  }

  private void addShutdownHook() {
    Runtime.getRuntime().addShutdownHook(new Thread(Launcher.this::stop));
  }

  public final void stop() {
    try {
      log.info("Shutdown jetty launcher at {}", port);
      server.stop();
    } catch (Exception e) {
      log.warn("Failed to shutdown jetty launcher at {}", port, e);
    }
  }

  public static void main(String[] args) throws Exception {
    new Launcher("prod", 8080).run();
  }
}