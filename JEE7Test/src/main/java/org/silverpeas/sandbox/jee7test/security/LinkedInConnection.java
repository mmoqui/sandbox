package org.silverpeas.sandbox.jee7test.security;

import org.springframework.social.connect.UserProfile;
import org.springframework.social.linkedin.connect.LinkedInConnectionFactory;
import org.springframework.social.oauth1.AuthorizedRequestToken;
import org.springframework.social.oauth1.OAuth1Operations;
import org.springframework.social.oauth1.OAuthToken;
import org.springframework.social.oauth2.AccessGrant;
import org.springframework.social.oauth2.OAuth2Operations;

import javax.annotation.PostConstruct;
import javax.inject.Named;
import java.util.ResourceBundle;

/**
 * @author mmoquillon
 */
@Named("linkedInConnection")
public class LinkedInConnection implements SocialNetworkConnection {

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
    System.out.println("CONNECTION WITH LINKEDIN...");
    OAuth2Operations oauthOperations = connectionFactory. getOAuthOperations();
    return new OAuthToken(consumerKey, secretKey);
  }

  @Override
  public UserProfile getUserProfile(final OAuthToken token) {
    return null;
  }
}
