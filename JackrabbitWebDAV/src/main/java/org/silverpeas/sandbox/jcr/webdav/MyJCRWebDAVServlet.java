package org.silverpeas.sandbox.jcr.webdav;

import org.apache.jackrabbit.server.CredentialsProvider;
import org.apache.jackrabbit.webdav.simple.SimpleWebdavServlet;
import org.silverpeas.sandbox.jcr.WebDavCredentialsProvider;

import javax.inject.Inject;
import javax.jcr.Repository;

/**
 * @author mmoquillon
 */
public class MyJCRWebDAVServlet extends SimpleWebdavServlet {

  @Inject
  private Repository repository;

  @Inject
  private WebDavCredentialsProvider credentialsProvider;

  @Override
  public Repository getRepository() {
    return repository;
  }

  @Override
  protected CredentialsProvider getCredentialsProvider() {
    return credentialsProvider;
  }
}
