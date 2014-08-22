package org.silverpeas.sandbox.jee7test.security;

import org.springframework.social.connect.UserProfile;
import org.springframework.social.linkedin.connect.LinkedInConnectionFactory;
import org.springframework.social.oauth1.OAuthToken;
import org.springframework.social.oauth2.OAuth2Operations;

import javax.annotation.PostConstruct;
import javax.inject.Named;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author mmoquillon
 */
@Named("linkedInConnection")
public class LinkedInConnection implements SocialNetworkConnection {

  private static Logger logger = Logger.getLogger(LinkedInConnection.class.getSimpleName());
  private String consumerKey;
  private String secretKey;
  private LinkedInConnectionFactory connectionFactory;

  @PostConstruct
  public void init() {
    ResourceBundle properties = ResourceBundle.getBundle("socialNetwork");
    consumerKey = properties.getString("linkedIn.consumerKey");
    secretKey = properties.getString("linkedIn.secretKey");
    connectionFactory = new LinkedInConnectionFactory(consumerKey, secretKey);
  }

  @Override
  public OAuthToken connect(final OAuthCredentials credentials) {
    logger.log(Level.INFO, "CONNECTION WITH LINKEDIN...");
    OAuth2Operations oauthOperations = connectionFactory.getOAuthOperations();
    return new OAuthToken(consumerKey, secretKey);
  }

  @Override
  public UserProfile getUserProfile(final OAuthToken token) {
    return null;
  }
}
