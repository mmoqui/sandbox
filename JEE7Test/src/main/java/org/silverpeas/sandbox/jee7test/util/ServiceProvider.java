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

  private static ServiceProvider instance = new ServiceProvider();

  private ServiceProvider() {

  }

  public static <T> T getService(Class<T> type) {
    BeanManager beanManager = CDI.current().getBeanManager();
    Bean<T> bean = (Bean<T>) beanManager.getBeans(type).stream()
        .findFirst()
        .orElseThrow(
          () -> new IllegalStateException("Cannot find an instance of type " + type.getName()));
    CreationalContext<T> ctx = beanManager.createCreationalContext(bean);
    T service = (T) beanManager.getReference(bean, type, ctx);

    return service;
  }

  public static <T> T getService(String name) {
    BeanManager beanManager = CDI.current().getBeanManager();
    Bean<T> bean = (Bean<T>) beanManager.getBeans(name).stream()
        .findFirst()
        .orElseThrow(() -> new IllegalStateException("Cannot find an instance of name " + name));
    CreationalContext<T> ctx = beanManager.createCreationalContext(bean);
    Type type = bean.getTypes().stream().findFirst().get();
    T service = (T) beanManager.getReference(bean, type, ctx);

    return service;
  }

  public static <T> Set<T> getAllServices(Class<T> type) {
    BeanManager beanManager = CDI.current().getBeanManager();
    Set<T> services = beanManager.getBeans(type).stream()
        .map(bean -> {
          CreationalContext ctx = beanManager.createCreationalContext(bean);
          return (T) beanManager.getReference(bean, type, ctx);
    })
        .collect(Collectors.toSet());
    return services;
  }

}
