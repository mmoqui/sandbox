package org.silverpeas.sandbox.jee7test.init;

import javax.annotation.PostConstruct;
import javax.inject.Singleton;

/**
 * @author mmoquillon
 */
@Singleton
public class Example2Initialization implements Initialization {

  @PostConstruct
  public void init() throws InterruptedException {
    System.out.println("Example2Initialization INITIALIZATION...");
    Thread.sleep(2000);
    System.out.println("Example2Initialization INITIALIZED");
  }
}
