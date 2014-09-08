package org.silverpeas.sandbox.jee7test.web.mvc;

import org.silverpeas.sandbox.jee7test.model.User;
import org.silverpeas.sandbox.jee7test.model.UserGroup;
import org.silverpeas.sandbox.jee7test.repository.UserGroupDAO;
import org.silverpeas.sandbox.jee7test.web.mvc.annotation.View;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import java.util.List;

/**
 * @author mmoquillon
 */
@Named("userGroupWebComponent")
public class UserGroupWebComponent implements WebComponent {

  public static final String ALL_GROUPS = "groups";
  public static final String NAME = "name";

  @Inject
  public UserGroupDAO dao;

  @GET
  @Path("all")
  @View("/groups.jsp")
  public Parameters getAllGroups(Parameters parameters) {
    Parameters result = new Parameters();
    List<UserGroup> groups = dao.getAllUserGroups();
    result.put(ALL_GROUPS, groups);
    return  result;
  }

  @POST
  @Path("newGroup")
  @View("/groups.jsp")
  public Parameters createGroup(Parameters parameters) {
    String name = parameters.get(NAME);
    UserGroup group = new UserGroup(name);
    dao.createUserGroup(group);
    return getAllGroups(parameters);
  }
}
