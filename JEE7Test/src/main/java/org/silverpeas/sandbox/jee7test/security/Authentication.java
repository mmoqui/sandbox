package org.silverpeas.sandbox.jee7test.security;

import org.silverpeas.sandbox.jee7test.security.annotation.SocialNetwork;
import org.springframework.social.oauth1.OAuthToken;

import javax.enterprise.inject.Any;
import javax.enterprise.inject.Instance;
import javax.enterprise.util.AnnotationLiteral;
import javax.inject.Inject;
import javax.inject.Named;
import javax.security.auth.message.AuthException;
import java.lang.annotation.Annotation;
import java.security.GeneralSecurityException;
import java.util.Iterator;

/**
 * @author mmoquillon
 */
public class Authentication {

  @Inject
  private PasswordEncryption encryption;
  @Inject @Any
  private Instance<SocialNetworkConnection> socialNetworkConnections;

  public void authenticate(BasicCredentials credentials) throws AuthException {
    try {
      String encryptedPassword = encryption.encrypt(credentials.getChallenge());
    } catch (GeneralSecurityException e) {
      e.printStackTrace();
      throw new AuthException("Invalid authentication for user " + credentials.getIdentifier());
    }
  }

  public void authenticate(OAuthCredentials credentials) throws AuthException {
    Iterator<SocialNetworkConnection> iterator = socialNetworkConnections.iterator();
    OAuthToken token = null;
    while (iterator.hasNext()) {
      SocialNetworkConnection connection = iterator.next();
      SocialNetwork socialNetwork = connection.getClass().getAnnotation(SocialNetwork.class);
      if (socialNetwork.value() == credentials.getService()) {
        token = connection.connect(credentials);
        if (token != null) {
          break;
        }
      }
    }
    if (token == null) {
      throw new AuthException("Invalid authentication for user");
    }
  }
}
