package org.silverpeas.sandbox.jee7test.web.rest;

import org.silverpeas.sandbox.jee7test.web.rest.annotation.Authenticated;
import org.silverpeas.sandbox.jee7test.model.UserGroup;
import org.silverpeas.sandbox.jee7test.repository.UserGroupDAO;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.List;

/**
 * @author mmoquillon
 */
@Path("/groups")
@Authenticated
public class UserGroupResource extends RESTWebResource {

  @Context
  private UriInfo uriInfo;

  @Inject
  private UserGroupDAO userGroupDAO;

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public List<UserGroup> getAllUserGroups() {
    return userGroupDAO.getAllUserGroups();
  }

  @POST
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response newUserGroup(UserGroup userGroup) {
    userGroupDAO.createUserGroup(userGroup);
    List<UserGroup> userGroups = userGroupDAO.getAllUserGroups();
    return Response.created(uriInfo.getAbsolutePathBuilder().build(userGroup.getId()))
        .entity(userGroups).build();
  }
}
