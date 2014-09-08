package org.silverpeas.sandbox.jee7test.web.mvc;

import org.silverpeas.sandbox.jee7test.component.model.Component;
import org.silverpeas.sandbox.jee7test.model.User;
import org.silverpeas.sandbox.jee7test.model.UserGroup;
import org.silverpeas.sandbox.jee7test.repository.UserGroupDAO;
import org.silverpeas.sandbox.jee7test.web.mvc.annotation.View;

import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.Transactional;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import java.util.List;

/**
 * @author mmoquillon
 */
@Named("userWebComponent")
public class UserWebComponent implements WebComponent {

  public static final String ALL_USERS = "users";
  public static final String FIRST_NAME = "firstName";
  public static final String LAST_NAME = "lastName";

  @Inject
  public UserGroupDAO dao;

  @GET
  @Path("all")
  @View("/users.jsp")
  public Parameters getAllUsers(Parameters parameters) {
    Parameters result = new Parameters();
    List<User> users = User.getAll();
    result.put(ALL_USERS, users);
    return result;
  }

  @POST
  @Path("newUser")
  @View("/users.jsp")
  @Transactional
  public Parameters createUser(Parameters parameters) {
    String firstName = parameters.get(FIRST_NAME);
    String lastName = parameters.get(LAST_NAME);
    User user = new User(firstName, lastName);
    user.save();
    UserGroup userGroup = new UserGroup(user.getFirstName() + " " + user.getLastName());
    dao.createUserGroup(userGroup);
    Component userAgenda =
        new Component("Agenda", "Agenda." + user.getFirstName() + "." + user.getLastName());
    userAgenda.save();
    return getAllUsers(parameters);
  }
}
