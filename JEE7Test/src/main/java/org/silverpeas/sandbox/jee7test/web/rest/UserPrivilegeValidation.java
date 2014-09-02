package org.silverpeas.sandbox.jee7test.web.rest;

import org.silverpeas.sandbox.jee7test.security.Authentication;
import org.silverpeas.sandbox.jee7test.security.BasicCredentials;
import org.silverpeas.sandbox.jee7test.security.OAuthCredentials;
import org.silverpeas.sandbox.jee7test.security.SocialNetworkService;

import javax.inject.Inject;
import javax.security.auth.message.AuthException;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.nio.charset.Charset;
import java.util.Base64;

/**
 * @author mmoquillon
 */
public class UserPrivilegeValidation {

  @Inject
  private Authentication authentication;

  public void validateAuthentication(final HttpServletRequest request)
      throws WebApplicationException {
    String challenge = request.getHeader("Authorization");
    if (challenge == null || challenge.trim().isEmpty()) {
      throw new WebApplicationException(Response.Status.UNAUTHORIZED);
    }
    String inLowerCaseChallenge = challenge.toLowerCase();
    if (inLowerCaseChallenge.startsWith("basic ")) {
      basicAuthentication(challenge.substring("basic ".length()));
    } else if (inLowerCaseChallenge.startsWith("linkedin")) {
      oauthAuthentication(SocialNetworkService.LinkedIn, challenge.substring("linkedin ".length()));
    }  else if (inLowerCaseChallenge.startsWith("facebook")) {
      oauthAuthentication(SocialNetworkService.Facebook, challenge.substring("facebook ".length()));
    }
  }

  private void oauthAuthentication(final SocialNetworkService service, final String challenge) {
    String encodedCredentials =
        new String(Base64.getDecoder().decode(challenge), Charset.forName("UTF-8"));
    int separatorIndex = encodedCredentials.indexOf(":");
    String token = encodedCredentials.substring(0, separatorIndex);
    String verifier = encodedCredentials.substring(separatorIndex + 1);
    OAuthCredentials credentials = new OAuthCredentials(service, token, verifier);
    try {
      authentication.authenticate(credentials);
    } catch (AuthException e) {
      throw new WebApplicationException(Response.Status.UNAUTHORIZED);
    }
  }

  private void basicAuthentication(final String challenge) {
    String encodedCredentials =
        new String(Base64.getDecoder().decode(challenge), Charset.forName("UTF-8"));
    int separatorIndex = encodedCredentials.indexOf(":");
    String login = encodedCredentials.substring(0, separatorIndex);
    String password = encodedCredentials.substring(separatorIndex + 1);
    BasicCredentials credentials = new BasicCredentials(login, password);
    try {
      authentication.authenticate(credentials);
    } catch (AuthException e) {
      throw new WebApplicationException(Response.Status.UNAUTHORIZED);
    }
  }

}
