package org.silverpeas.sandbox.jee7test.model;

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
}
