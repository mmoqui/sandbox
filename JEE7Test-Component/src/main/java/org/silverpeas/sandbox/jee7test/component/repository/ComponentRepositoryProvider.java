package org.silverpeas.sandbox.jee7test.component.repository;

import org.silverpeas.sandbox.jee7test.component.util.BeanProvider;

/**
 * @author mmoquillon
 */
public class ComponentRepositoryProvider implements BeanProvider<ComponentRepository> {
  @Override
  public Class<ComponentRepository> getProvidedBeanType() {
    return ComponentRepository.class;
  }
}
