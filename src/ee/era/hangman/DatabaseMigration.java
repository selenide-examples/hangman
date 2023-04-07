package ee.era.hangman;

import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;

public class DatabaseMigration {
  private static final Logger log = LoggerFactory.getLogger(DatabaseMigration.class);
  
  private final DataSource dataSource;
  private final String environment;

  public DatabaseMigration(DataSource dataSource, String environment) {
    this.dataSource = dataSource;
    this.environment = environment;
  }

  public void migrate() {
    log.info("Context initialized - dataSource: {}, environment: {}", dataSource, environment);

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
}
