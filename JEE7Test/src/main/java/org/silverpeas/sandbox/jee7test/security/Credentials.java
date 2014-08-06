package org.silverpeas.sandbox.jee7test.security;

/**
 * @author mmoquillon
 */
public interface Credentials {

  public String getIdentifier();
  public String getChallenge();

}
