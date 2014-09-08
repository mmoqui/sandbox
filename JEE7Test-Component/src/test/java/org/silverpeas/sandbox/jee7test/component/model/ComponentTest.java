package org.silverpeas.sandbox.jee7test.component.model;

import org.junit.Before;
import org.junit.Test;
import org.silverpeas.sandbox.jee7test.component.repository.ComponentRepository;
import org.silverpeas.sandbox.jee7test.component.repository.ComponentRepositoryProvider;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;
import static org.silverpeas.sandbox.jee7test.component.model.ComponentBuilder.*;

/**
 * @author mmoquillon
 */
public class ComponentTest {

  private ComponentRepository componentRepository;

  @Before
  public void prepareCommonBehaviors() {
    ComponentRepositoryProvider componentRepositoryProvider = mockComponentRepositoryProvider();
    componentRepository = mock(ComponentRepository.class);

    when(componentRepositoryProvider.getBean()).thenReturn(componentRepository);
    when(componentRepository.getAllComponents()).thenReturn(Collections.<Component>emptyList());
  }

  @Test
  public void askForAnExistingComponentShouldReturnNull() {
    Component component = Component.getById("0");
    assertThat(component, nullValue());
  }

  @Test
  public void askForAnExistingComponentShouldReturnIt() {
    Component expectedComponent = anExistingComponent(0, "Toto", "GED");
    when(componentRepository.getComponentById("0")).thenReturn(expectedComponent);

    Component actualComponent = Component.getById(expectedComponent.getId());
    assertThat(actualComponent, notNullValue());
    assertThat(actualComponent, is(expectedComponent));
  }

  @Test
  public void withNoComponentsAskForAllComponentsShouldReturnNothing() {
    List<Component> allComponents = Component.getAll();
    assertThat(allComponents.isEmpty(), is(true));
  }

  @Test
  public void withSomeComponentsAskForAllComponentsShouldReturnThem() {
    when(componentRepository.getAllComponents()).thenReturn(Arrays.asList(
        anExistingComponent(0, "Toto", "GED"),
        anExistingComponent(1, "Bob", "Gallery")));

    List<Component> allComponents = Component.getAll();
    assertThat(allComponents.size(), is(2));
    assertThat(allComponents.contains(anExistingComponent(0, "Toto", "GED")), is(true));
    assertThat(allComponents.contains(anExistingComponent(1, "Bob", "Gallery")), is(true));
  }
}
