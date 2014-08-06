package org.silverpeas.sandbox.jee7test.util;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.CDI;
import java.lang.reflect.Type;
import java.util.Iterator;

/**
 * @author mmoquillon
 */
public final class ServiceProvider {

  private static ServiceProvider instance = new ServiceProvider();

  private ServiceProvider() {

  }

  public static <T> T getService(Class<T> type) {
    BeanManager beanManager = CDI.current().getBeanManager();
    Iterator<Bean< ? >> iterator = beanManager.getBeans(type).iterator();
    if (!iterator.hasNext()) {
      throw new IllegalStateException("Cannot find an instance of type " + type.getName());
    }
    Bean<T> bean = (Bean<T>) iterator.next();
    CreationalContext<T> ctx = beanManager.createCreationalContext(bean);
    T service = (T) beanManager.getReference(bean, type, ctx);

    return service;
  }

  public static <T> T getService(String name) {
    BeanManager beanManager = CDI.current().getBeanManager();
    Iterator<Bean< ? >> iterator = beanManager.getBeans(name).iterator();
    if (!iterator.hasNext()) {
      throw new IllegalStateException("Cannot find an instance named " + name);
    }
    Bean<T> bean = (Bean<T>) iterator.next();
    CreationalContext<T> ctx = beanManager.createCreationalContext(bean);
    Type type = (Type) bean.getTypes().iterator().next();
    T service = (T) beanManager.getReference(bean, type, ctx);

    return service;
  }

}
