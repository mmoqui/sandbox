package org.silverpeas.jcr.security;

/**
 * @author mmoquillon
 */
public interface Authentication {

  public void authenticate(Credentials credentials);
}
