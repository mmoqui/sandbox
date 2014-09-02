package org.silverpeas.sandbox.jee7test.jcr.repository;

import javax.enterprise.inject.Produces;
import javax.jcr.Repository;

/**
 * @author mmoquillon
 */
public interface JcrRepositoryProvider {

  public Repository getRepository();
}
