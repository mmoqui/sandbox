package org.silverpeas.sandbox.jcr;

import org.apache.jackrabbit.server.CredentialsProvider;

import javax.jcr.Credentials;
import javax.jcr.LoginException;
import javax.jcr.SimpleCredentials;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

/**
 * @author mmoquillon
 */
public class WebDavCredentialsProvider implements CredentialsProvider {

  private static final String ID = "jcr-system@domain0";

  @Override
  public Credentials getCredentials(final HttpServletRequest request)
      throws LoginException, ServletException {
    return new SimpleCredentials(ID, new char[0]);
  }
}
