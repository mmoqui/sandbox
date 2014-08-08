package org.silverpeas.sandbox.jee7test.init;

import org.silverpeas.sandbox.jee7test.init.annotation.Initialization;

import javax.annotation.PostConstruct;
import javax.inject.Singleton;

/**
 * @author mmoquillon
 */
@Initialization
public class Example3Initialization {

  @Initialization
  public void init() throws InterruptedException {
    System.out.println("Example3Initialization INITIALIZATION...");
    Thread.sleep(2000);
    System.out.println("Example3Initialization INITIALIZED");
  }
}
