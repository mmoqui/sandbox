package org.silverpeas.sandbox.jee7test.init;

import org.silverpeas.sandbox.jee7test.init.annotation.Initialization;
import org.silverpeas.sandbox.jee7test.init.annotation.Initializer;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AnnotatedType;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessAnnotatedType;
import javax.enterprise.inject.spi.WithAnnotations;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author mmoquillon
 */
public class ApplicationInitializationExtension implements Extension {

  private Queue<AnnotatedType> schedulers = new ConcurrentLinkedQueue<>();

  public <T> void initialize(
      final @Observes @WithAnnotations(Initializer.class) ProcessAnnotatedType<T> type) {
    AnnotatedType<T> annotatedType = type.getAnnotatedType();
    System.out.println("INITIALIZER FOUND: " + annotatedType.getJavaClass().getSimpleName());
    annotatedType.getMethods().stream()
        .filter(m -> m.isAnnotationPresent(Initialization.class))
        .forEach(m -> {
          try {
            T initialization = annotatedType.getJavaClass().newInstance();
            m.getJavaMember().invoke(initialization);
          } catch (Exception e) {
            e.printStackTrace();
          }
        });
  }
}
