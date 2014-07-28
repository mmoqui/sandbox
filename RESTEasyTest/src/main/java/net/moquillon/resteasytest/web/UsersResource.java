/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.moquillon.resteasytest.web;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Enumeration;
import java.util.List;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
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
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;
import net.moquillon.resteasytest.core.User;
import net.moquillon.resteasytest.core.UserService;
import org.apache.commons.io.IOUtils;
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

/**
 * REST Web Service
 *
 * @author mmoquillon
 */
@Service
@Scope("request")
@Path("/users")
public class UsersResource {

  @Context
  private UriInfo context;

  @Context
  private HttpServletRequest request;

  @Inject
  private UserService service;

  /**
   * Creates a new instance of UsersResource
   */
  public UsersResource() {
  }

  /**
   * Retrieves representation of an instance of net.moquillon.resteasytest.web.UsersResource
   *
   * @return an instance of java.util.List
   */
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public List<User> getAllUsers() {
    return service.getAllUsers();
  }

  /**
   * POST method for creating an instance of UserResource
   *
   * @param user representation for the new resource
   * @return an HTTP response with content of the created resource
   */
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response createUser(User user) {
    logHttpHeaders();
    service.persistUser(user);
    return Response.created(context.getAbsolutePathBuilder().path(user.getId()).build()).
        entity(user).build();
  }

  @GET
  @Path("{id}")
  @Produces(MediaType.APPLICATION_JSON)
  public User getUser(@PathParam("id") String id) {
    logHttpHeaders();
    User user = service.getUser(id);
    if (user.isNotDefined()) {
      throw new WebApplicationException(Status.NOT_FOUND);
    }
    return user;
  }

  @POST
  @Path("files")
  @Consumes(MediaType.MULTIPART_FORM_DATA)
  public Response uploadFile() throws Exception {
    FileUploadFormData form = new FileUploadFormData().parse(request);
    System.out.println("Content of the file " + form.getName() + ": ");
    IOUtils.copy(form.getFile(), System.out);
    return Response.created(context.getAbsolutePathBuilder().build()).build();
  }

  private void logHttpHeaders() {
    Enumeration<String> headerNames = request.getHeaderNames();
    for (; headerNames.hasMoreElements();) {
      String name = headerNames.nextElement();
      System.out.println(name + ": " + request.getHeader(name));
    }
  }
}
