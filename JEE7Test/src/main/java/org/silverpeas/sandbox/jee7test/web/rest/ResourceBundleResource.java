package org.silverpeas.sandbox.jee7test.web.rest;

import org.silverpeas.sandbox.jee7test.util.MyResourceBundle;
import org.silverpeas.sandbox.jee7test.web.mvc.BundleWebComponent;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.StringWriter;
import java.util.MissingResourceException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author mmoquillon
 */
@Path("/bundle")
public class ResourceBundleResource extends RESTWebResource {

  private static Logger logger = Logger.getLogger(ResourceBundleResource.class.getSimpleName());

  @Inject
  private BundleWebComponent bundleComponent;

  @GET
  @Path("settings/{bundle: (com|org)/[a-zA-Z0-9/._$]+}")
  @Produces(MediaType.TEXT_PLAIN)
  public Response getSettingsBundle(@PathParam("bundle") final String bundle) {
    logger.log(Level.INFO, "Looking for the settings " + bundle);
    try {
      String settings = bundleComponent.loadSettingsBundle(bundle);
      return Response.ok(settings).build();
    } catch (IOException|IllegalArgumentException e) {
      return Response.status(Response.Status.BAD_REQUEST).entity(
          e.getMessage()).build();
    }
  }

  @GET
  @Path("{bundle: (com|org)/[a-zA-Z0-9/._$]+}")
  @Produces(MediaType.TEXT_PLAIN)
  public Response getLocalizedBundle(@PathParam("bundle") final String bundle) throws IOException {
    logger.log(Level.INFO, "Looking for the localized bundle " + bundle);
    try {
      String settings = bundleComponent.loadResourceBundle(bundle);
      return Response.ok(settings).build();
    } catch (IOException|IllegalArgumentException e) {
      return Response.status(Response.Status.BAD_REQUEST).entity(
          e.getMessage()).build();
    }
  }
}
