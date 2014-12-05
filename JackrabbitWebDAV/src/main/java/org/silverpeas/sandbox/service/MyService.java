package org.silverpeas.sandbox.service;

import com.stratelia.webactiv.publication.control.PublicationService;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * @author mmoquillon
 */
@Singleton
public class MyService {

  @Inject
  private PublicationService publicationService;


  public String sayHello() {
    return "hello!";
  }
}
