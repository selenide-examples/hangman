package ee.era.hangman;

import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.sun.net.httpserver.HttpServer;
import ee.era.hangman.model.WordsService;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.beans.PropertyVetoException;
import java.net.InetSocketAddress;

public class Launcher {
  private static final Logger log = LoggerFactory.getLogger(Launcher.class);

  private final String environment;
  private final String host;
  private final int port;
  @Nullable
  private HttpServer server;

  private final DataSource dataSource = createDataSource();
  private final WordsService wordsService = new WordsService(dataSource);

  public Launcher(String environment, String host, int port) {
    this.environment = environment;
    this.host = host;
    this.port = port;
  }

  @CanIgnoreReturnValue
  public Launcher run() throws Exception {
    log.info("Start hangman webapp at http://{}:{} from {}", host, port, System.getProperty("user.dir"));

    new DatabaseMigration(dataSource, environment).migrate();

    InetSocketAddress addr = new InetSocketAddress(host, port);
    server = HttpServer.create(addr, 0);
    server.createContext("/", new RequestHandler(wordsService));
    server.start();

    addShutdownHook();
    return this;
  }

  private void addShutdownHook() {
    Runtime.getRuntime().addShutdownHook(new Thread(Launcher.this::stop));
  }

  public final void stop() {
    if (server != null) {
      try {
        log.info("Shutdown jetty launcher at {}...", port);
        server.stop(0);
        server = null;
        log.info("Shutdown complete at {}", port);
      } catch (Exception e) {
        log.warn("Failed to shutdown jetty launcher at {}", port, e);
      }
    }
  }

  private DataSource createDataSource() {
    try {
      ComboPooledDataSource ds = new ComboPooledDataSource();
      ds.setDriverClass("org.h2.Driver");
      ds.setJdbcUrl("jdbc:h2:mem:hangman");
      ds.setUser("sa");
      ds.setPassword("");
      ds.setMinPoolSize(1);
      ds.setMaxPoolSize(5);
      ds.setMaxStatementsPerConnection(10);
      return ds;
    }
    catch (PropertyVetoException e) {
      throw new RuntimeException(e);
    }
  }

  public static void main(String[] args) throws Exception {
    new Launcher("prod", "localhost", 8080).run();
  }
}