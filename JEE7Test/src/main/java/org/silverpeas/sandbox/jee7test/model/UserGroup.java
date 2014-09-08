package org.silverpeas.sandbox.jee7test.model;

import java.util.List;

/**
 * @author mmoquillon
 */
public class UserGroup {

  private long id;
  private String name;

  protected UserGroup() {

  }

  public UserGroup( String name) {
    this.name = name;
  }

  public String getId() {
    return String.valueOf(id);
  }

  public void setName(final String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public List<User> getUsers() {
    return User.getAllInGroup(this);
  }

}
