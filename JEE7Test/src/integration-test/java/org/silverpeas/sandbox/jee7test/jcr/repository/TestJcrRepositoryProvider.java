package org.silverpeas.sandbox.jee7test.jcr.repository;

import org.apache.commons.io.FileUtils;
import org.apache.jackrabbit.commons.JcrUtils;
import org.apache.jackrabbit.core.RepositoryFactoryImpl;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Default;
import javax.enterprise.inject.Produces;
import javax.inject.Singleton;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author mmoquillon
 */
@Singleton
public class TestJcrRepositoryProvider implements JcrRepositoryProvider {

  private static final Logger logger =
      Logger.getLogger(JcrRepositoryProvider.class.getSimpleName());
  private static final String JCR_HOME = "jcr.home.dir";
  private static final String JCR_CONFIG = "jcr.config";

  private Repository repository;

  @Override
  @Produces
  public Repository getRepository() {
    return repository;
  }

  @PostConstruct
  protected void initJcrAccess() {
    try {
      logger.log(Level.INFO, "BOOTSTRAP THE JCR REPOSITORY FOR TESTS...");
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

      Map<String, String> parameters = new HashMap<>(2);
      parameters.put(RepositoryFactoryImpl.REPOSITORY_CONF, jcrConfPath);
      parameters.put(RepositoryFactoryImpl.REPOSITORY_HOME, jcrHomePath);
      repository = JcrUtils.getRepository(parameters);
    } catch (IOException | RepositoryException ex) {
      throw new JcrRepositoryException(ex.getMessage(), ex);
    }
  }

}
