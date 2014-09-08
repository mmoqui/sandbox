package org.silverpeas.sandbox.jee7test.component.model;

import org.silverpeas.sandbox.jee7test.component.repository.ComponentRepositoryProvider;

import static org.mockito.Mockito.mock;

/**
 * @author mmoquillon
 */
public class ComponentBuilder {

  public static Component anExistingComponent(long id, String name, String type) {
    return new Component(id, name, type);
  }

  public static Component aNewComponent(String name, String type) {
    return new Component(type, name);
  }

  public static ComponentRepositoryProvider mockComponentRepositoryProvider() {
    Component.componentRepositoryProvider = mock(ComponentRepositoryProvider.class);
    return Component.componentRepositoryProvider;
  }
}
