package org.silverpeas.sandbox.jee7test.init;

import javax.annotation.PostConstruct;
import javax.ejb.Startup;
import javax.inject.Singleton;

/**
 * Example of a singleton that should be instantiated and invoked at application startup by playing
 * with the CDI dependency injection mechanism. In this way, the @PostConstruct-annotated method
 * should not thrown any exception.
 *
 * The instantiation and the invocation of the @PostConstruct-annotated method will be done by the
 * CDI container when the {@code org.silverpeas.sandbox.jee7test.web.ApplicationInitializer}
 * servlet context listener asks for the instance of all the Initialization interface
 * implementations.
 *
 * @author mmoquillon
 */
@Singleton
public class Example1Initialization implements Initialization {

  @PostConstruct
  public void init() {
    System.out.println("Example1Initialization INITIALIZATION...");
    try {
      Thread.sleep(2000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    System.out.println("Example1Initialization INITIALIZED");
  }
}
