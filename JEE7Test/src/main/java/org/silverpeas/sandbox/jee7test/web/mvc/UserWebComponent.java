package org.silverpeas.sandbox.jee7test.web.mvc;

import org.silverpeas.sandbox.jee7test.model.User;
import org.silverpeas.sandbox.jee7test.web.mvc.annotation.View;

import javax.inject.Named;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import java.util.List;

/**
 * @author mmoquillon
 */
@Named("userWebComponent")
public class UserWebComponent implements WebComponent {

  @GET
  @Path("all")
  @View("/users.jsp")
  public Parameters getAllUsers(Parameters parameters) {
    Parameters result = new Parameters();
    List<User> users = User.getAll();
    result.put("users", users);
    return  result;
  }

  @POST
  @Path("newUser")
  @View("/users.jsp")
  public Parameters createUser(Parameters parameters) {
    String firstName = parameters.get("firstName");
    String lastName = parameters.get("lastName");
    User user = new User(firstName, lastName);
    user.save();
    return getAllUsers(parameters);
  }
}
