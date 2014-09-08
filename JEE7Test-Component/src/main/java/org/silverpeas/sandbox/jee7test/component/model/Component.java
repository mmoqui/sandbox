package org.silverpeas.sandbox.jee7test.component.model;

import org.silverpeas.sandbox.jee7test.component.repository.ComponentRepository;
import org.silverpeas.sandbox.jee7test.component.repository.ComponentRepositoryProvider;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import java.util.List;

/**
 * @author mmoquillon
 */
@NamedQueries({
    @NamedQuery(name = "byComponentType", query = "select c from Component c where c.type = :type"),
    @NamedQuery(name = "byComponentName", query = "select c from Component c where c.name = :type"),
    @NamedQuery(name = "allComponents", query = "select c from Component c")
})
@Entity
public class Component {

  protected static ComponentRepositoryProvider
      componentRepositoryProvider = new ComponentRepositoryProvider();

  protected Component() {

  }

  public static Component getById(String userId) {
    ComponentRepository componentRepository = componentRepositoryProvider.getBean();
    return componentRepository.getComponentById(userId);
  }

  public static List<Component> getAll() {
    ComponentRepository componentRepository = componentRepositoryProvider.getBean();
    return componentRepository.getAllComponents();
  }

  public static List<Component> getByType(String type) {
    ComponentRepository componentRepository = componentRepositoryProvider.getBean();
    return componentRepository.getComponentsByType(type);
  }

  public static List<Component> getByName(String name) {
    ComponentRepository componentRepository = componentRepositoryProvider.getBean();
    return componentRepository.getComponentsByName(name);
  }

  public void save() {
    ComponentRepository componentRepository = componentRepositoryProvider.getBean();
    componentRepository.putComponent(this);
  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String name;
  private String type;

  protected Component(long id, String name, String type) {
    this.id = id;
    this.name = name;
    this.type = type;
  }

  public Component(final String type, final String name) {
    this.type = type;
    this.name = name;
  }

  public String getId() {
    return id == null ? null:String.valueOf(id);
  }

  public String getName() {
    return name;
  }

  public String getType() {
    return type;
  }

  public void setName(final String name) {
    this.name = name;
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    final Component component = (Component) o;
    if (id != null && component.id != null) {
      return id == component.id;
    } else {
      return this == component;
    }
  }

  @Override
  public int hashCode() {
    int result = (int) (id == null ? 0:(id ^ (id >>> 32)));
    result = 31 * result + type.hashCode();
    result = 31 * result + name.hashCode();
    return result;
  }

}
