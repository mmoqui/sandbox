package org.silverpeas.sandbox.jee7test.test.util;

import org.mockito.Mockito;
import org.silverpeas.sandbox.jee7test.util.BeanContainer;

import java.util.Set;

import static org.mockito.Mockito.mock;

/**
 * @author mmoquillon
 */
public class TestBeanContainer implements BeanContainer {

  private static BeanContainer mock = mock(BeanContainer.class);

  public static BeanContainer getMockedBeanContainer() {
    return mock;
  }

  @Override
  public <T> T getBeanByName(final String name) {
    return mock.getBeanByName(name);
  }

  @Override
  public <T> T getBeanByType(final Class<T> type) {
    return mock.getBeanByType(type);
  }

  @Override
  public <T> Set<T> getAllBeansByType(final Class<T> type) {
    return mock.getAllBeansByType(type);
  }
}
