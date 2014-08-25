package org.silverpeas.sandbox.jee7test.util;

import java.util.Set;

/**
 * I represents the strategy used to manage the life-cycle of the objects (aka the beans) in
 * Silverpeas.
 *
 * All implementation of a bean management container must implement this interface.
 *
 * @author mmoquillon
 */
public interface BeanContainer {

  public <T> T getBeanByName(String name);

  public <T> T getBeanByType(Class<T> type);

  public <T> Set<T> getAllBeansByType(Class<T> type);
}
