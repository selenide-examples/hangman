package ee.era.hangman;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.google.inject.servlet.GuiceServletContextListener;
import com.google.inject.servlet.ServletModule;
import com.google.inject.struts2.Struts2GuicePluginModule;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import ee.era.hangman.actions.Game;

import javax.sql.DataSource;
import java.beans.PropertyVetoException;

public class GuiceListener extends GuiceServletContextListener {

  public static String environment = "prod";

  public Injector getInjector() {
    return Guice.createInjector(
        new Struts2GuicePluginModule(),
        new ServletModule() {
          @Override
          protected void configureServlets() {
//            bind(StrutsPrepareAndExecuteFilter.class).in(Singleton.class);
//            filter("/*").through(StrutsPrepareAndExecuteFilter.class);

            requestStaticInjection(LiquibaseServletListener.class);
            requestStaticInjection(Game.class);
          }

          @Provides @Named("dictionary") @Singleton
          public DataSource addDataSource() throws PropertyVetoException {
            ComboPooledDataSource ds = new ComboPooledDataSource();
            ds.setDriverClass("org.h2.Driver");
            ds.setJdbcUrl("jdbc:h2:mem:");
            ds.setUser("sa");
            ds.setPassword("");
            ds.setMinPoolSize(1);
            ds.setMaxPoolSize(1);
            ds.setMaxStatementsPerConnection(3);
            return ds;
          }

          @Provides @Named("environment")
          public String getEnvironment() {
            return environment;
          }
        });
  }
}