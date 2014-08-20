package org.silverpeas.sandbox.jee7test.init;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;

/**
 * Example of a singleton that should be instantiated and invoked at application startup by using
 * a singleton session bean. In this way, the @PostConstruct-annotated method should not thrown any
 * exception.
 *
 * The instantiation and the invocation of the @PostConstruct-annotated method will be done by the
 * EJB container at application startup.
 *
 * @author mmoquillon
 */
@Singleton
@Startup
public class Example6Initialization {

  @PostConstruct
  public void init() {
    System.out.println("Example6Initialization INITIALIZATION...");
    try {
      Thread.sleep(2000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    System.out.println("Example6Initialization INITIALIZED");
  }
}
