package org.silverpeas.sandbox.jee7test.model;

import org.silverpeas.sandbox.jee7test.messaging.EventNotifier;
import org.silverpeas.sandbox.jee7test.repository.UserRepository;
import org.silverpeas.sandbox.jee7test.util.ServiceProvider;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.validation.constraints.Null;
import java.util.List;

/**
 * @author mmoquillon
 */
@NamedQueries({
    @NamedQuery(name = "byLastName", query = "select u from User u where u.lastName = :name"),
    @NamedQuery(name = "all", query = "select u from User u"),
    @NamedQuery(name = "allById", query = "select u from User u where u.id in :ids"),
    @NamedQuery(name = "byGroupId", query = "select u from User u where u.groupId = :groupId")
})
@Entity
public class User {

  protected User() {

  }

  public static User getById(String userId) {
    UserRepository userRepository = ServiceProvider.getService(UserRepository.class);
    return userRepository.getUserById(userId);
  }

  public static List<User> getAll() {
    UserRepository userRepository = ServiceProvider.getService(UserRepository.class);
    return userRepository.getAllUsers();
  }

  public static List<User> getAllInGroup(UserGroup group) {
    UserRepository userRepository = ServiceProvider.getService(UserRepository.class);
    return userRepository.getAllUsersByGroupId(group.getId());
  }

  public static List<User> getByLastName(String lastName) {
    UserRepository userRepository = ServiceProvider.getService(UserRepository.class);
    return userRepository.getUserByLastName(lastName);
  }

  public void save() {
    UserRepository userRepository = ServiceProvider.getService(UserRepository.class);
    userRepository.putUser(this);
    EventNotifier notifier = ServiceProvider.getService(EventNotifier.class);
    notifier.notify("User " + getFirstName() + " " + getLastName() + " created");
  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String firstName;
  private String lastName;
  private Long groupId;

  protected User(long id, String firstName, String lastName) {
    this.id = id;
    this.firstName = firstName;
    this.lastName = lastName;
  }

  public User(final String firstName, final String lastName) {
    this.firstName = firstName;
    this.lastName = lastName;
  }

  public String getId() {
    return id == null ? null:String.valueOf(id);
  }

  public String getFirstName() {
    return firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(final String lastName) {
    this.lastName = lastName;
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    final User user = (User) o;
    if (id != null && user.id != null) {
      return id == user.id;
    } else {
      return this == user;
    }
  }

  @Override
  public int hashCode() {
    int result = (int) (id == null ? 0:(id ^ (id >>> 32)));
    result = 31 * result + firstName.hashCode();
    result = 31 * result + lastName.hashCode();
    return result;
  }
}