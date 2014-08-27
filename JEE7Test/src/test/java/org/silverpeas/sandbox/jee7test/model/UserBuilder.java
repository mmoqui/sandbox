package org.silverpeas.sandbox.jee7test.model;

import org.mockito.Mockito;
import org.silverpeas.sandbox.jee7test.repository.UserRepositoryProvider;

import static org.mockito.Mockito.mock;

/**
 * @author mmoquillon
 */
public class UserBuilder {

  public static User anExistingUser(long id, String firstName, String lastName) {
    return new User(id, firstName, lastName);
  }

  public static User aNewUser(String firstName, String lastName) {
    return new User(firstName, lastName);
  }

  public static UserRepositoryProvider mockUserRepositoryProvider() {
    User.userRepositoryProvider = mock(UserRepositoryProvider.class);
    return User.userRepositoryProvider;
  }
}
