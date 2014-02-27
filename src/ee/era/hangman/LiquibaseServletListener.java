package ee.era.hangman;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;

import javax.servlet.ServletContextEvent;
import javax.sql.DataSource;
import java.sql.Connection;

public class LiquibaseServletListener implements javax.servlet.ServletContextListener {
  @Inject @Named("dictionary")
  private static DataSource dataSource;

  @Inject @Named("environment")
  private static String environment;

  @Override
  public void contextInitialized(ServletContextEvent servletContextEvent) {
    try (Connection connection = dataSource.getConnection()) {
      System.out.println("Initialize database " + connection.getMetaData().getURL());

      JdbcConnection jdbc = new JdbcConnection(connection);
      try {
        Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(jdbc);
        Liquibase liquibase = new Liquibase("changelog.xml", new ClassLoaderResourceAccessor(), database);
        liquibase.update(environment);
      }
      finally {
        jdbc.close();
      }
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void contextDestroyed(ServletContextEvent servletContextEvent) {

  }
}
