package org.silverpeas.jcr.security;

/**
 * @author mmoquillon
 */
public class AuthenticationProvider {

  private static final Authentication defaultAuthentication = new SilverpeasAuthentication();

  public static Authentication getAuthentication() {
    return defaultAuthentication;
  }
}
