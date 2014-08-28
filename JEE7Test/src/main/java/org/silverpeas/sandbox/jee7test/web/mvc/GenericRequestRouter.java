package org.silverpeas.sandbox.jee7test.web.mvc;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUpload;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.RequestContext;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.servlet.ServletRequestContext;
import org.apache.commons.io.FileUtils;
import org.silverpeas.sandbox.jee7test.util.ServiceProvider;
import org.silverpeas.sandbox.jee7test.web.mvc.annotation.Binary;
import org.silverpeas.sandbox.jee7test.web.mvc.annotation.View;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.WebApplicationException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.apache.commons.fileupload.disk.DiskFileItemFactory.DEFAULT_SIZE_THRESHOLD;

/**
 * @author mmoquillon
 */
public class GenericRequestRouter extends HttpServlet {

  /**
   * The name of a default parameter set when a file is uploaded.
   */
  public static final String ERROR_MESSAGE = "ErrorMessage";

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
    try {
      RoutingResponse response = invoke(req, GET.class.getSimpleName());
      response.routes(req, resp);
    } catch(WebApplicationException ex) {
      ex.printStackTrace();
      resp.sendError(ex.getResponse().getStatus(), ex.getMessage());
    }
  }

  @Override
  protected void doPost(final HttpServletRequest req, final HttpServletResponse resp)
      throws ServletException, IOException {
    try {
      RoutingResponse response = invoke(req, POST.class.getSimpleName());
      response.routes(req, resp);
    } catch(WebApplicationException ex) {
      ex.printStackTrace();
      resp.sendError(ex.getResponse().getStatus(), ex.getMessage());
    }
  }

  private WebComponent getWebComponent() {
    return webComponent;
  }

  private RoutingResponse invoke(HttpServletRequest request, String type) {
    String requestURI = request.getRequestURI();
    int index = requestURI.indexOf(request.getServletPath()) + request.getServletPath().length();
    final String path = requestURI.substring(index);
    RoutingResponse response = null;
    String nextView = "/error.jsp";
    for (Method method : invokables.get(type)) {
      Path supportedPath = method.getAnnotation(Path.class);
      if (path.equalsIgnoreCase(supportedPath.value()) ||
          path.equalsIgnoreCase("/" + supportedPath.value())) {
        try {
          Parameters parameters;
          RequestContext ctx = new ServletRequestContext(request);
          if (FileUpload.isMultipartContent(ctx)) {
            parameters = fetchMultipartParameters(ctx);
          } else {
            parameters = fetcPlainParameters(request);
          }
          Parameters result = (Parameters) method.invoke(getWebComponent(), parameters);
          if (result instanceof BinaryContent) {
            response = new ContentOutput((BinaryContent)result);
          } else {
            nextView = method.getAnnotation(View.class).value();
            response = new ViewForward(nextView, result);
          }
        } catch (WebApplicationException ex) {
          throw ex;
        } catch (Exception e) {
          e.printStackTrace();
          Parameters result = new Parameters();
          result.put(ERROR_MESSAGE, e.getMessage());
          response = new ViewForward(nextView, result);
        }
        break;
      }
    }
    if (response == null) {
      response = new ViewForward(nextView, new Parameters());
    }

    return response;
  }

  private BinaryContent fetchMultipartParameters(RequestContext ctx)
      throws FileUploadException, IOException {
    BinaryContent parameters = new BinaryContent();
    File uploadDir = FileUtils.getTempDirectory();
    DiskFileItemFactory fileItemFactory = new DiskFileItemFactory(DEFAULT_SIZE_THRESHOLD,
        uploadDir);
    ServletFileUpload upload = new ServletFileUpload(fileItemFactory);
    List<FileItem> items = upload.parseRequest(ctx);
    for(FileItem item: items) {
      if (item.isFormField()) {
        parameters.put(item.getFieldName(), item.getString());
      } else {
        parameters.setContentType(item.getContentType());
        parameters.setInputStream(item.getInputStream(), item.getSize());
        parameters.setContentName(item.getName());
        parameters.put(item.getFieldName(), item.getName());
      }
    }

    return parameters;
  }

  private Parameters fetcPlainParameters(HttpServletRequest request) {
    Parameters parameters = new Parameters();
    Enumeration<String> requestParams = request.getParameterNames();
    while (requestParams.hasMoreElements()) {
      String paramName = requestParams.nextElement();
      parameters.put(paramName, request.getParameter(paramName));
    }
    return parameters;
  }
}
