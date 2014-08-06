package org.silverpeas.sandbox.jee7test.security;

import org.springframework.social.connect.UserProfile;
import org.springframework.social.oauth1.OAuthToken;

/**
 * @author mmoquillon
 */
public interface SocialNetworkConnection {

  public OAuthToken connect(OAuthCredentials credentials);

  public UserProfile getUserProfile(OAuthToken token);
}
