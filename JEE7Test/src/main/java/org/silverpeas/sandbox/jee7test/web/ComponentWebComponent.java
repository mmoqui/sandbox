package org.silverpeas.sandbox.jee7test.web;

import org.silverpeas.sandbox.jee7test.component.model.Component;
import org.silverpeas.sandbox.jee7test.model.UserGroup;
import org.silverpeas.sandbox.jee7test.repository.UserGroupDAO;
import org.silverpeas.sandbox.jee7test.web.mvc.Parameters;
import org.silverpeas.sandbox.jee7test.web.mvc.WebComponent;
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
@Named("componentWebComponent")
public class ComponentWebComponent implements WebComponent {

  public static final String ALL_COMPONENTS = "components";
  public static final String NAME = "name";
  public static final String TYPE = "type";

  @GET
  @Path("all")
  @View("/components.jsp")
  public Parameters getAllComponents(Parameters parameters) {
    Parameters result = new Parameters();
    List<Component> components = Component.getAll();
    result.put(ALL_COMPONENTS, components);
    return  result;
  }

  @POST
  @Path("newComponent")
  @View("/components.jsp")
  public Parameters createComponent(Parameters parameters) {
    String name = parameters.get(NAME);
    String type = parameters.get(TYPE);
    Component component = new Component(type, name);
    component.save();
    return getAllComponents(parameters);
  }
}
