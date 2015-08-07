package org.silverpeas.sanbox.jee7sandbox.util;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.CDI;
import java.lang.annotation.Annotation;
import java.util.Set;

/**
 * A provider of beans that were registered into an underlying DI container. It abstracts the
 * concrete implementation of the DI container; it can be a CDI implementation (like Weld), or
 * the Springframework DI container, and so on.
 *
 * This class is useful to access the beans managed by a DI container from an unmanaged object.
 * @author miguel
 */
public class BeanProvider {

  /**
   * Gets one bean whose type is the specified one and optionally qualified by the given
   * annotations. If no bean of the specified type and annotations is found, an exception is then
   * thrown.
   * @param type the class to which the bean belongs.
   * @param qualifiers the annotations that qualifies the bean. According to the underlying DI
   * container, the qualifiers concept shouldn't be supported.
   * @param <T> the concrete type of the bean.
   * @return one bean matching both the type and the qualifiers.
   * @throws IllegalStateException if no bean of the specified type and qualifiers is found.
   */
  public static <T> T getBeanByType(final Class<T> type, Annotation... qualifiers)
      throws IllegalStateException {
    BeanManager beanManager = CDI.current().getBeanManager();
    Bean<T> bean = beanManager.resolve((Set) beanManager.getBeans(type, qualifiers));
    if (bean == null) {
      throw new IllegalStateException("Cannot find an instance of type " + type.getName());
    }
    CreationalContext<T> ctx = beanManager.createCreationalContext(bean);

    return (T) beanManager.getReference(bean, type, ctx);
  }
}
