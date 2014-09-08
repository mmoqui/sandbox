package org.silverpeas.sandbox.jee7test.repository;

import org.silverpeas.sandbox.jee7test.component.model.Component;
import org.silverpeas.sandbox.jee7test.model.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author mmoquillon
 */
public class UserRepository {

  @PersistenceContext(unitName = "jee7test")
  private EntityManager entityManager;

  public List<User> getAllUsers() {
    TypedQuery<User> query = entityManager.createNamedQuery("all", User.class);
    return query.getResultList();
  }

  public User getUserById(String id) {
    return entityManager.find(User.class, Long.valueOf(id));
  }

  public List<User> getUserByLastName(String name) {
    TypedQuery<User> query = entityManager.createNamedQuery("byLastName", User.class);
    query.setParameter("name", name);
    return query.getResultList();
  }

  public List<User> getAllUsersById(List<String> ids) {
    TypedQuery<User> query = entityManager.createNamedQuery("allById", User.class);
    List<Long> userIds = ids.stream().map(id -> Long.valueOf(id)).collect(Collectors.toList());
    query.setParameter("ids", userIds);
    return query.getResultList();
  }

  public List<User> getAllUsersByGroupId(String groupId) {
    TypedQuery<User> query = entityManager.createNamedQuery("byGroupId", User.class);
    query.setParameter("groupId", Long.valueOf(groupId));
    return query.getResultList();
  }

  @Transactional
  public void putUser(final  User user) {
    entityManager.persist(user);
  }
}
