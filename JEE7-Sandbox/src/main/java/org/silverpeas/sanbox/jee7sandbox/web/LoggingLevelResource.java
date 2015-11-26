package org.silverpeas.sanbox.jee7sandbox.web;

import org.silverpeas.sanbox.jee7sandbox.util.MyLogger;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.logging.Level;

/**
 * @author miguel
 */
@Path("/logging/{module}")
@RequestScoped
public class LoggingLevelResource {

  @PathParam("module")
  private String module;

  @GET
  @Consumes(MediaType.APPLICATION_JSON)
  public LoggingModule getLoggingModule() {
    MyLogger logger = MyLogger.getLogger(module);
    LoggingModule loggingModule = new LoggingModule(module);
    String level = LoggingLevel.fromJavaLoggingLevel(logger.getLevel()).name();
    loggingModule.setLevel(level);
    return loggingModule;
  }

  @PUT
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public LoggingModule updateLoggingModule(LoggingModule updatedLoggingModule) {
    if (module != null && module.equals(updatedLoggingModule.getModule())) {
      MyLogger logger = MyLogger.getLogger(module);
      Level level = LoggingLevel.valueOf(updatedLoggingModule.getLevel()).toJavaLoggingLevel();
      logger.setLevel(level);
      return updatedLoggingModule;
    } else {
      throw new WebApplicationException(Response.Status.BAD_REQUEST);
    }
  }
}
