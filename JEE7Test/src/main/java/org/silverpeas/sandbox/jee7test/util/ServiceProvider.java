package org.silverpeas.sandbox.jee7test.util;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.CDI;
import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author mmoquillon
 */
public final class ServiceProvider {

  // by default, in Silverpeas, we use a CDI container
  private static BeanContainer container = new CDIContainer();

  private ServiceProvider() {

  }

  public static <T> T getService(Class<T> type) {
    return container.getBeanByType(type);
  }

  public static <T> T getService(String name) {
    return container.getBeanByName(name);
  }

  public static <T> Set<T> getAllServices(Class<T> type) {
    return container.getAllBeansByType(type);
  }

  /**
   * Change the current bean container by the specified one.
   * @param beanContainer the bean container to use.
   */
  public static void setBeanContainer(final BeanContainer beanContainer) {
    container = beanContainer;
  }

}
