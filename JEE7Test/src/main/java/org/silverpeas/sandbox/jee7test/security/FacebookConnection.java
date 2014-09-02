package org.silverpeas.sandbox.jee7test.security;

import org.silverpeas.sandbox.jee7test.security.annotation.SocialNetwork;
import org.springframework.social.connect.UserProfile;
import org.springframework.social.facebook.connect.FacebookConnectionFactory;
import org.springframework.social.oauth1.OAuthToken;
import org.springframework.social.oauth2.OAuth2Operations;

import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.inject.Singleton;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author mmoquillon
 */
@SocialNetwork(SocialNetworkService.Facebook)
@Singleton
public class FacebookConnection implements SocialNetworkConnection {

  private static Logger logger = Logger.getLogger(FacebookConnection.class.getSimpleName());
  private String consumerKey;
  private String secretKey;
  private FacebookConnectionFactory connectionFactory;

  @PostConstruct
  public void init() {
    ResourceBundle properties = ResourceBundle.getBundle("socialNetwork");
    consumerKey = properties.getString("facebook.consumerKey");
    secretKey = properties.getString("facebook.secretKey");
    connectionFactory = new FacebookConnectionFactory(consumerKey, secretKey);
  }

  @Override
  public OAuthToken connect(final OAuthCredentials credentials) {
    logger.log(Level.INFO, "CONNECTION WITH FACEBOOK...");
    OAuth2Operations oauthOperations = connectionFactory. getOAuthOperations();
    return new OAuthToken(consumerKey, secretKey);
  }

  @Override
  public UserProfile getUserProfile(final OAuthToken token) {
    return null;
  }
}
