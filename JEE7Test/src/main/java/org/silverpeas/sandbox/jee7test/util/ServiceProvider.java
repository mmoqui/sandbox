package org.silverpeas.sandbox.jee7test.util;

import java.util.Iterator;
import java.util.ServiceLoader;
import java.util.Set;

/**
 * @author mmoquillon
 */
public final class ServiceProvider {

  private static BeanContainer _currentContainer = null;

  private ServiceProvider() {
  }

  public static <T> T getService(Class<T> type) {
    return beanContainer().getBeanByType(type);
  }

  public static <T> T getService(String name) {
    return beanContainer().getBeanByName(name);
  }

  public static <T> Set<T> getAllServices(Class<T> type) {
    return beanContainer().getAllBeansByType(type);
  }

  private static BeanContainer beanContainer() {
    if (_currentContainer == null) {
      Iterator<BeanContainer> iterator = ServiceLoader.load(BeanContainer.class).iterator();
      if (iterator.hasNext()) {
        _currentContainer = iterator.next();
      } else {
        throw new RuntimeException(
            "No IoD container detected! At least one bean container should be available!");
      }
    }
    return _currentContainer;
  }

}
