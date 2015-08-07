package org.silverpeas.sanbox.jee7sandbox.bean;

import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * A repository of user messages. Any messages stored into a repository will be persisted over
 * the application runtime. The repository serves as a persistence container for business entities.
 * @author miguel
 */
@Singleton
public class UserMessageRepository {

  @PersistenceContext(unitName = "jee7-sandbox")
  private EntityManager entityManager;

  protected UserMessageRepository() {

  }

  /**
   * Puts the specified message into the repository of user messages. Once the message is put,
   * a persistence identifier will be assigned to it in order to be personally get from the
   * repository.
   * @param message the message to save into the system.
   * @return itself.
   */
  public UserMessageRepository put(final UserMessage message) {
    entityManager.persist(message);
    return this;
  }

  /**
   * Gets all the user messages that were stored into this repository.
   * @return a list of all available user messages.
   */
  public List<UserMessage> getAll() {
    TypedQuery<UserMessage> query = entityManager.createNamedQuery("listAll", UserMessage.class);
    return query.getResultList();
  }
}
