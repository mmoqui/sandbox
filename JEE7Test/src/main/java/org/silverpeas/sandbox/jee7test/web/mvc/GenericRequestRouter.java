package org.silverpeas.sandbox.jee7test.web.mvc;

import org.silverpeas.sandbox.jee7test.util.ServiceProvider;
import org.silverpeas.sandbox.jee7test.web.mvc.annotation.View;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author mmoquillon
 */
public class GenericRequestRouter extends HttpServlet {

  private WebComponent webComponent;
  private Map<String, List<Method>> invokables = new HashMap<>();

  @Override
  public void init(ServletConfig config) throws ServletException {
    super.init(config);
    String webComponentName = config.getInitParameter(WebComponent.class.getSimpleName());
    if (webComponentName == null || webComponentName.trim().isEmpty()) {
      throw new IllegalArgumentException(
          "A web component must be declared to the request router instance");
    }
    webComponent = ServiceProvider.getService(webComponentName);
    invokables.put(GET.class.getSimpleName(), new ArrayList<>());
    invokables.put(POST.class.getSimpleName(), new ArrayList<>());
    Method[] methods = webComponent.getClass().getDeclaredMethods();
    for (Method method : methods) {
      Class<?>[] types = method.getParameterTypes();
      Class<?> returnType = method.getReturnType();
      if (types.length == 1 && Arrays.asList(types).contains(Parameters.class) &&
          returnType == Parameters.class) {
        if (method.getAnnotation(GET.class) != null) {
          invokables.get(GET.class.getSimpleName()).add(method);
        } else if (method.getAnnotation(POST.class) != null) {
          invokables.get(POST.class.getSimpleName()).add(method);
        }
      }
    }
  }

  @Override
  protected void doGet(final HttpServletRequest req, final HttpServletResponse resp)
      throws ServletException, IOException {
    String view = invoke(req, GET.class.getSimpleName());
    req.getRequestDispatcher(view).forward(req, resp);
  }

  @Override
  protected void doPost(final HttpServletRequest req, final HttpServletResponse resp)
      throws ServletException, IOException {
    String view = invoke(req, POST.class.getSimpleName());
    req.getRequestDispatcher(view).forward(req, resp);
  }

  private WebComponent getWebComponent() {
    return webComponent;
  }

  private String invoke(HttpServletRequest request, String type) {
    String requestURI = request.getRequestURI();
    int index = requestURI.indexOf(request.getServletPath()) + request.getServletPath().length();
    final String path = requestURI.substring(index);
    Parameters result = null;
    String nextView = "/error.jsp";
    for (Method method : invokables.get(type)) {
      Path supportedPath = method.getAnnotation(Path.class);
      if (path.equalsIgnoreCase(supportedPath.value()) ||
          path.equalsIgnoreCase("/" + supportedPath.value())) {
        try {
          Parameters parameters = new Parameters();
          Enumeration<String> requestParams = request.getParameterNames();
          while (requestParams.hasMoreElements()) {
            String paramName = requestParams.nextElement();
            parameters.put(paramName, request.getParameter(paramName));
          }
          result = (Parameters) method.invoke(getWebComponent(), parameters);
          nextView = method.getAnnotation(View.class).value();
        } catch (IllegalAccessException e) {
          e.printStackTrace();
        } catch (InvocationTargetException e) {
          e.printStackTrace();
        }
        break;
      }
    }
    if (result == null) {
      result = new Parameters();
    }
    setResultInRequest(result, request);
    return nextView;
  }

  private void setResultInRequest(Parameters result, HttpServletRequest request) {
    Set<String> parameterNames = result.getParameterNames();
    for (String paramName : parameterNames) {
      request.setAttribute(paramName, result.get(paramName));
    }
  }
}
