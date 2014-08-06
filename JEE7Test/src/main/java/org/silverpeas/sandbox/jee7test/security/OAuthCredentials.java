package org.silverpeas.sandbox.jee7test.security;

/**
 * @author mmoquillon
 */
public class OAuthCredentials implements Credentials {

  private String token;
  private String verifier;

  public OAuthCredentials(final String token, final String verifier) {
    this.token = token;
    this.verifier = verifier;
  }

  public String getToken() {
    return getIdentifier();
  }

  public String getVerifier() {
    return getChallenge();
  }

  @Override
  public String getIdentifier() {
    return token;
  }

  @Override
  public String getChallenge() {
    return verifier;
  }
}
