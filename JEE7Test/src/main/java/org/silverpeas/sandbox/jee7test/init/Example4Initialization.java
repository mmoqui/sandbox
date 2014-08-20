package org.silverpeas.sandbox.jee7test.init;

import org.silverpeas.sandbox.jee7test.init.annotation.Initialization;
import org.silverpeas.sandbox.jee7test.init.annotation.Initializer;

import javax.inject.Singleton;

/**
 * Example of a singleton that should be instantiated and invoked at application startup by using
 * a CDI extension.
 *
 * The instantiation and the invocation of the @Initialization-annotated method will be done by the
 * {@code org.silverpeas.sandbox.jee7test.init.ApplicationInitializationExtension} CDI extension at
 * CDI container bootstrap.
 *
 * @author mmoquillon
 */
@Initializer
public class Example4Initialization {

  @Initialization
  public void init() throws InterruptedException {
    System.out.println("Example4Initialization INITIALIZATION...");
    Thread.sleep(2000);
    System.out.println("Example4Initialization INITIALIZED");
  }
}
