package org.silverpeas.sandbox.jee7test.component.repository;

import org.silverpeas.sandbox.jee7test.component.model.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;

/**
 * @author mmoquillon
 */
public class ComponentRepository {

  @PersistenceContext(unitName = "jee7test-component")
  private EntityManager entityManager;

  public List<Component> getAllComponents() {
    TypedQuery<Component> query = entityManager.createNamedQuery("allComponents", Component.class);
    return query.getResultList();
  }

  public Component getComponentById(String id) {
    return entityManager.find(Component.class, Long.valueOf(id));
  }

  public List<Component> getComponentsByType(String type) {
    TypedQuery<Component> query = entityManager.createNamedQuery("byComponentType", Component.class);
    query.setParameter("type", type);
    return query.getResultList();
  }

  public List<Component> getComponentsByName(String name) {
    TypedQuery<Component> query = entityManager.createNamedQuery("byComponentName", Component.class);
    query.setParameter("name", name);
    return query.getResultList();
  }

  @Transactional
  public void putComponent(final Component component) {
    entityManager.persist(component);
  }
}
