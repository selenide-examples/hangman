package ee.era.hangman;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.server.nio.SelectChannelConnector;
import org.eclipse.jetty.webapp.WebAppContext;

public class Launcher {
  private final int port;
  private final Server server;

  public Launcher(int port) {
    this.port = port;
    server = new Server();
  }

  public Launcher run() throws Exception {
    System.out.println("Start jetty launcher at " + port);

    Connector connector = new SelectChannelConnector();
    connector.setPort(port);
    connector.setMaxIdleTime(30000);
    server.addConnector(connector);

    HandlerCollection webapps = new HandlerCollection();
    webapps.addHandler(new WebAppContext("game/webapp", "/hangman"));
    server.setHandler(webapps);

    addShutdownHook();
    server.start();
    return this;
  }

  private void addShutdownHook() {
    Runtime.getRuntime().addShutdownHook(new Thread() {
      @Override
      public void run() {
        Launcher.this.stop();
      }
    });
  }

  public final void stop() {
    if (server != null) {
      try {
        System.out.println("Shutdown jetty launcher at " + port);
        server.stop();
      } catch (Exception ignore) {
      }
    }
  }

  public static void main(String[] args) throws Exception {
    new Launcher(8081).run();
  }
}