package org.silverpeas.sandbox.jee7test.util.providers;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.CDI;
import java.lang.reflect.Type;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * An example of an interface with default implementation for some methods in Java 8
 * @author mmoquillon
 */
public interface BeanProvider<T> {

  Class<T> getProvidedBeanType();

  public default T getBean() {
    BeanManager beanManager = CDI.current().getBeanManager();
    Bean<T> bean = (Bean<T>) beanManager.getBeans(getProvidedBeanType()).stream()
        .findFirst()
        .orElseThrow(() -> new IllegalStateException("Cannot find an instance of type " +
            getProvidedBeanType().getName()));

    CreationalContext<T> ctx = beanManager.createCreationalContext(bean);
    T ref = (T) beanManager.getReference(bean, getProvidedBeanType(), ctx);

    return ref;
  }

  public default T getBean(String name) {
    BeanManager beanManager = CDI.current().getBeanManager();
    Bean<T> bean = (Bean<T>) beanManager.getBeans(name).stream()
        .findFirst()
        .orElseThrow(() -> new IllegalStateException("Cannot find an instance of name " + name));

    CreationalContext<T> ctx = beanManager.createCreationalContext(bean);
    Type type = bean.getTypes().stream().findFirst().get();
    T ref = (T) beanManager.getReference(bean, type, ctx);

    return ref;
  }

  public default Set<T> getAllBeans() {
    BeanManager beanManager = CDI.current().getBeanManager();
    Set<T> refs = beanManager.getBeans(getProvidedBeanType()).stream()
        .map(bean -> {
      CreationalContext ctx = beanManager.createCreationalContext(bean);
      return (T) beanManager.getReference(bean, getProvidedBeanType(), ctx);
    })
        .collect(Collectors.toSet());

    return refs;
  }

}
