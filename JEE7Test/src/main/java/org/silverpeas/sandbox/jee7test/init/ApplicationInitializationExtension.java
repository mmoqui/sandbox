package org.silverpeas.sandbox.jee7test.init;

import org.silverpeas.sandbox.jee7test.init.annotation.Initialization;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AnnotatedType;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessInjectionTarget;

/**
 * @author mmoquillon
 */
public class ApplicationInitializationExtension implements Extension {

  public <T> void initialize(final @Observes ProcessInjectionTarget<T> target) {
    final AnnotatedType<T> type = target.getAnnotatedType();
    if (type.isAnnotationPresent(Initialization.class)) {
      System.out.println("INITIALIZER FOUND: " + type.getJavaClass().getName());
      type.getMethods().stream()
          .filter(m -> m.isAnnotationPresent(Initialization.class))
          .forEach(m -> {
            try {
              T initialization = type.getJavaClass().newInstance();
              m.getJavaMember().invoke(initialization);
            } catch (Exception e) {
              e.printStackTrace();
            }
          });
    }
  }
}
