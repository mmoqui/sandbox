package org.silverpeas.sanbox.jee7sandbox;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

/**
 * Web resource representing user messages. It is the entry point of the
 * application functionalities about user message sending.
 * @author miguel
 */
@Path("/messages")
@RequestScoped
public class UserMessageResource {

  @Context
  private UriInfo uriInfo;

  @Inject
  private UserMessageSender messageSender;

  /**
   * Posts a new user message into the application. Once the message sent, a unique identifier will
   * be set to the message (the sending identifier) and the message with its identifier will be
   * sent back in the body of the HTTP response.
   * @param message the message to send.
   * @return the message itself with its unique identifier set.
   */
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response postNewUserMessage(UserMessage message) {
    messageSender.send(message);
    return Response.created(uriInfo.getAbsolutePathBuilder().build(message.getId()))
        .entity(message)
        .build();
  }
}
