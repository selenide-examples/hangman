package ee.era.hangman;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.server.nio.SelectChannelConnector;
import org.eclipse.jetty.webapp.WebAppContext;

import java.io.File;

public class Launcher {
  private final int port;
  private final Server server;
  private final String workingDir;

  public Launcher(int port) {
    this(port, ".");
  }

  public Launcher(int port, String folder) {
    this.port = port;
    server = new Server();
    workingDir = folder;
  }

  public Launcher run() throws Exception {
    System.out.println("Start jetty launcher at " + port);
    System.out.println("Start hangman webapp at " + new File(workingDir + "/game/webapp").getAbsolutePath());

    Connector connector = new SelectChannelConnector();
    connector.setPort(port);
    connector.setMaxIdleTime(30000);
    server.addConnector(connector);

    HandlerCollection webapps = new HandlerCollection();
    webapps.addHandler(new WebAppContext(workingDir + "/game/webapp", "/hangman"));
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
    new Launcher(8081, ".").run();
  }
}