package org.silverpeas.sandbox.jee7test.web.rest;

import org.silverpeas.sandbox.jee7test.web.rest.annotation.Authenticated;
import org.silverpeas.sandbox.jee7test.model.User;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.List;

/**
 * @author mmoquillon
 */
@Path("/users")
@Authenticated
public class UserResource extends RESTWebResource {

  @Context
  private UriInfo uriInfo;

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public List<User> getUsers() {
    return User.getAll();
  }

  @Path("{userId}")
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public User getUser(@PathParam("userId") String userId) {
    User user = User.getById(userId);
    if (user == null) {
      throw new WebApplicationException(Response.Status.NOT_FOUND);
    }
    return user;
  }

  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response newUser(User user) {
    user.save();
    List<User> users = getUsers();
    return Response.created(uriInfo.getAbsolutePathBuilder().build(user.getId())).entity(users)
        .build();
  }
}
