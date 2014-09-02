package org.silverpeas.jcr.security;

/**
 * @author mmoquillon
 */
public interface Credentials {

  public String getIdentifier();
  public String getChallenge();
}
