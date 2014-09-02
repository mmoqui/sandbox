package org.silverpeas.sandbox.jee7test.security;

import org.silverpeas.sandbox.jee7test.security.Credentials;

/**
 * @author mmoquillon
 */
public class OAuthCredentials implements Credentials {

  private String token;
  private String verifier;
  private SocialNetworkService service;

  public OAuthCredentials(final SocialNetworkService service, final String token, final String verifier) {
    this.service = service;
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

  public SocialNetworkService getService() {
    return service;
  }
}
