package org.silverpeas.sandbox.jee7test.web.rest;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

/**
 * @author mmoquillon
 */
public abstract class RESTWebResource {

  @Context
  private HttpServletRequest httpRequest;

  public void validateUserAuthentication(final UserPrivilegeValidation validation) {
    validation.validateAuthentication(httpRequest);
  }
}
