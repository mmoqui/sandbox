package org.silverpeas.sandbox.jee7test.security;

import javax.inject.Inject;
import javax.inject.Named;
import javax.security.auth.message.AuthException;
import java.security.GeneralSecurityException;

/**
 * @author mmoquillon
 */
public class Authentication {

  @Inject
  private PasswordEncryption encryption;
  @Inject
  @Named("linkedInConnection")
  private SocialNetworkConnection linkedInConnection;

  public void authenticate(BasicCredentials credentials) throws AuthException {
    try {
      String encryptedPassword = encryption.encrypt(credentials.getChallenge());
    } catch (GeneralSecurityException e) {
      e.printStackTrace();
      throw new AuthException("Invalid authentication for user " + credentials.getIdentifier());
    }
  }

  public void authenticate(OAuthCredentials credentials) throws AuthException {
    linkedInConnection.connect(credentials);
  }
}
