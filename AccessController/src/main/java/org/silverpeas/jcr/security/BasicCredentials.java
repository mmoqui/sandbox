package org.silverpeas.jcr.security;

/**
 * @author mmoquillon
 */
public class BasicCredentials implements Credentials {

  private String password;
  private String login;

  public BasicCredentials(String login, String password) {
    this.login = login;
    this.password = password;
  }

  @Override
  public String getIdentifier() {
    return login;
  }

  @Override
  public String getChallenge() {
    return password;
  }
}
