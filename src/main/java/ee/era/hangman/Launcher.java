package ee.era.hangman;


import org.mortbay.jetty.Connector;
import org.mortbay.jetty.Handler;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.nio.SelectChannelConnector;
import org.mortbay.jetty.webapp.WebAppContext;
import org.mortbay.thread.QueuedThreadPool;

public class Launcher {
  private final int port;
  private final Server server;

  public Launcher(int port) {
    this.port = port;
    this.server = new Server();
  }

  public Launcher run() throws Exception {
    System.out.println("Start jetty launcher at " + port);

    server.setThreadPool(new QueuedThreadPool(100));

    Connector connector = new SelectChannelConnector();
    connector.setPort(port);
    connector.setMaxIdleTime(30000);
    server.setConnectors(new Connector[]{connector});

    server.setHandlers(createWebapps());
    addShutdownHook();
    server.start();
    return this;
  }

  private void addShutdownHook() {
    Runtime.getRuntime().addShutdownHook(new Thread() {
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

  protected WebAppContext createWebApp(String contextPath, String webappLocation) {
    WebAppContext webapp = new WebAppContext();
    webapp.setContextPath(contextPath);
    webapp.setWar(webappLocation);
    return webapp;
  }

  protected Handler[] createWebapps() {
    return new Handler[]{
        createWebApp("/logs", "logs"),
        createWebApp("/hangman", "src/main/webapp")
    };
  }

  public static void main(String[] args) throws Exception {
    new Launcher(8081).run();
  }
}