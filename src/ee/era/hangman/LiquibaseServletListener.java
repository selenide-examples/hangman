package ee.era.hangman;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContextEvent;
import javax.sql.DataSource;
import java.sql.Connection;

public class LiquibaseServletListener implements javax.servlet.ServletContextListener {
  private static final Logger log = LoggerFactory.getLogger(LiquibaseServletListener.class);
  
  @Inject @Named("dictionary")
  private static DataSource dataSource;

  @Inject @Named("environment")
  private static String environment;

  @Override
  public void contextInitialized(ServletContextEvent servletContextEvent) {
    log.info("Context initialized: {}, dataSource: {}, environment: {}", servletContextEvent, dataSource, environment);

    try (Connection connection = dataSource.getConnection()) {
      log.info("Initialize database {}", connection.getMetaData().getURL());

      try (JdbcConnection jdbc = new JdbcConnection(connection)) {
        Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(jdbc);
        Liquibase liquibase = new Liquibase("changelog.xml", new ClassLoaderResourceAccessor(), database);
        liquibase.update(environment);
      }
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    log.info("Database migration completed");
  }

  @Override
  public void contextDestroyed(ServletContextEvent servletContextEvent) {
    log.info("Context destroyed: {}", servletContextEvent);
  }
}
