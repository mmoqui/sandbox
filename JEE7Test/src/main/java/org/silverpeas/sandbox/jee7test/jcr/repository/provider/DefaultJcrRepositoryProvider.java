package org.silverpeas.sandbox.jee7test.jcr.repository.provider;

import org.apache.commons.io.FileUtils;
import org.apache.jackrabbit.core.RepositoryImpl;
import org.apache.jackrabbit.core.config.RepositoryConfig;
import org.silverpeas.sandbox.jee7test.jcr.repository.JcrRepositoryException;
import org.silverpeas.sandbox.jee7test.jcr.repository.JcrRepositoryProvider;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.inject.Produces;
import javax.inject.Singleton;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import java.io.File;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author mmoquillon
 */
@Singleton
public class DefaultJcrRepositoryProvider implements JcrRepositoryProvider {

  private static final Logger logger =
      Logger.getLogger(JcrRepositoryProvider.class.getSimpleName());
  private static final String JCR_HOME = "jcr.home.dir";
  private static final String JCR_CONFIG = "jcr.config";

  private RepositoryImpl repository;

  @Override
  @Produces
  public Repository getRepository() {
    return repository;
  }

  @PostConstruct
  protected void initJcrAccess() {
    try {
      logger.log(Level.INFO, "BOOTSTRAP THE JCR REPOSITORY...");
      Properties jcrProperties = new Properties();
      jcrProperties.load(getClass().getResourceAsStream("/jcr.properties"));
      String jcrHomePath = jcrProperties.getProperty(JCR_HOME);
      String jcrConfPath = jcrProperties.getProperty(JCR_CONFIG);

      // the JCR is recreated each time the application is launched.
      File jcrHome = new File(jcrHomePath);
      if (jcrHome.exists()) {
        FileUtils.deleteDirectory(jcrHome);
      }
      FileUtils.forceMkdir(jcrHome);
      FileUtils.copyInputStreamToFile(getClass().getResourceAsStream("/repository.xml"),
          new File(jcrConfPath));

      RepositoryConfig config = RepositoryConfig.create(jcrConfPath, jcrHomePath);
      repository = RepositoryImpl.create(config);
    } catch (IOException | RepositoryException ex) {
      throw new JcrRepositoryException(ex.getMessage(), ex);
    }
  }

  @PreDestroy
  protected void releaseJcrAccess() {
    logger.log(Level.INFO, "SHUTDOWN THE JCR REPOSITORY...");
    repository.shutdown();
  }
}
