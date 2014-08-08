package org.silverpeas.sandbox.jee7test.web;

import org.silverpeas.sandbox.jee7test.init.Initialization;
import org.silverpeas.sandbox.jee7test.util.ServiceProvider;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.Set;

/**
 * @author mmoquillon
 */
public class ApplicationInitializer implements ServletContextListener {

  @Override
  public void contextInitialized(final ServletContextEvent sce) {
    Set<Initialization> initializations = ServiceProvider.getAllServices(Initialization.class);
  }

  @Override
  public void contextDestroyed(final ServletContextEvent sce) {

  }
}
