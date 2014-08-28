package org.silverpeas.sandbox.jee7test.web.mvc;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

/**
 * @author mmoquillon
 */
public class ViewForward implements RoutingResponse {

  private final Parameters parameters;
  private final String view;

  public ViewForward(final String view, final Parameters parameters) {
    this.view = view;
    this.parameters = parameters;
  }

  @Override
  public void routes(final HttpServletRequest request, final HttpServletResponse response)
      throws ServletException, IOException {
    Set<String> parameterNames = parameters.getParameterNames();
    for (String paramName : parameterNames) {
      request.setAttribute(paramName, parameters.get(paramName));
    }

    request.getRequestDispatcher(view).forward(request, response);
  }
}
