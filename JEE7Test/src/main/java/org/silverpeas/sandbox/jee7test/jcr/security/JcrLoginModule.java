package org.silverpeas.sandbox.jee7test.jcr.security;

import com.sun.security.auth.UserPrincipal;
import org.apache.jackrabbit.core.security.authentication.CredentialsCallback;
import org.silverpeas.sandbox.jee7test.security.Authentication;
import org.silverpeas.sandbox.jee7test.security.BasicCredentials;
import org.silverpeas.sandbox.jee7test.util.ServiceProvider;

import javax.jcr.Credentials;
import javax.jcr.SimpleCredentials;
import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.auth.login.LoginException;
import javax.security.auth.spi.LoginModule;
import java.io.IOException;
import java.security.Principal;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author mmoquillon
 */
public class JcrLoginModule implements LoginModule {
  private CallbackHandler callbackHandler;
  private Subject subject;
  private Set<Principal> principals = new HashSet<>();

  @Override
  public void initialize(final Subject subject, final CallbackHandler callbackHandler,
      final Map<String, ?> sharedState, final Map<String, ?> options) {
    this.subject = subject;
    this.callbackHandler = callbackHandler;
  }

  @Override
  public boolean login() throws LoginException {
    if (callbackHandler == null) {
      throw new LoginException("no CallbackHandler available");
    }
    principals.clear();

    boolean authenticated = false;
    try {
      CredentialsCallback credentialsCallback = new CredentialsCallback();
      callbackHandler.handle(new Callback[]{credentialsCallback});
      Credentials credentials = credentialsCallback.getCredentials();
      if (credentials != null) {
        if (credentials instanceof SimpleCredentials) {
          SimpleCredentials simpleCredentials = (SimpleCredentials) credentials;
          Authentication authentication = ServiceProvider.getService(Authentication.class);
          BasicCredentials basicCredentials = new BasicCredentials(simpleCredentials.getUserID(),
              String.valueOf(simpleCredentials.getPassword()));
          UserPrincipal principal = new UserPrincipal(simpleCredentials.getUserID());
          principals.add(principal);
          authenticated = true;
        }
      }
    } catch (IOException | UnsupportedCallbackException e) {
      e.printStackTrace();
      authenticated = false;
    }
    return authenticated;
  }

  @Override
  public boolean commit() throws LoginException {
    if (principals.isEmpty()) {
      return false;
    } else {
      subject.getPrincipals().addAll(principals);
      return true;
    }
  }

  @Override
  public boolean abort() throws LoginException {
    if (principals.isEmpty()) {
      return false;
    } else {
      logout();
    }
    return true;
  }

  @Override
  public boolean logout() throws LoginException {
    subject.getPrincipals().removeAll(principals);
    principals.clear();
    return true;
  }
}
