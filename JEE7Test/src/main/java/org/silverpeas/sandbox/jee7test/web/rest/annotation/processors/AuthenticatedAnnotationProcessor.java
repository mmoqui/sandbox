package org.silverpeas.sandbox.jee7test.web.rest.annotation.processors;

import org.silverpeas.sandbox.jee7test.web.rest.annotation.Authenticated;
import org.silverpeas.sandbox.jee7test.web.rest.RESTWebResource;
import org.silverpeas.sandbox.jee7test.web.rest.UserPrivilegeValidation;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

/**
 * @author mmoquillon
 */
@Interceptor
@Authenticated
@Priority(Interceptor.Priority.APPLICATION)
public class AuthenticatedAnnotationProcessor {

  @Inject
  private UserPrivilegeValidation validation;

  @AroundInvoke
  public Object processAutenthication(InvocationContext context)
      throws Exception {
    Object target = context.getTarget();
    if (target instanceof RESTWebResource) {
      RESTWebResource resource = (RESTWebResource) target;
      resource.validateUserAuthentication(validation);
    }
    return context.proceed();
  }
}
