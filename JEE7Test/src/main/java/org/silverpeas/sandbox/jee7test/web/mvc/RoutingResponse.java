package org.silverpeas.sandbox.jee7test.web.mvc;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author mmoquillon
 */
public interface RoutingResponse {

  public void routes(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException;
}
