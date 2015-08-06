package org.silverpeas.sanbox.jee7sandbox;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;

/**
 * A message from a user.
 * @author miguel
 */
@Entity
@NamedQueries({@NamedQuery(name = "listAll", query = "select m from UserMessage m")})
public class UserMessage implements Serializable {

  @Id
  private String id;

  private String text = "";

  protected UserMessage() {

  }

  /**
   * Sets the unique identifier of this message. This method is to be used only in the case the
   * message is sent, whatever it is correctly received.
   */
  protected void generateId() {
    this.id = UUID.randomUUID().toString();
  }

  /**
   * Gets the unique identifier of this message.
   * @return the unique identifier of the message in the system or null if the message
   * wasn't yet sent.
   */
  public String getId() {
    return this.id;
  }

  /**
   * Constructs a new user message with the specified text.
   * @param text the text of the new message to construct.
   */
  public UserMessage(String text) {
    this.text = text;
  }

  /**
   * Gets the text of this message.
   * @return the message text.
   */
  public String getText() {
    return this.text;
  }

  /**
   * Saves this user message into the system.
   */
  public void save() {
    UserMessageRepository repository = BeanProvider.getBeanByType(UserMessageRepository.class);
    repository.put(this);
  }

  /**
   * Gets all the user messages that were persisted up to now.
   * @return a list of saved user messages.
   */
  public static List<UserMessage> getAll() {
    UserMessageRepository repository = BeanProvider.getBeanByType(UserMessageRepository.class);
    return repository.getAll();
  }
}
