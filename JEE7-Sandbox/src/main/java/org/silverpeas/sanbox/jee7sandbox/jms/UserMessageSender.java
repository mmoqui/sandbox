package org.silverpeas.sanbox.jee7sandbox.jms;

import org.silverpeas.sanbox.jee7sandbox.bean.UserMessage;
import org.silverpeas.sanbox.jee7sandbox.util.MyLogger;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.jms.JMSContext;
import javax.jms.JMSDestinationDefinition;
import javax.jms.JMSDestinationDefinitions;
import javax.jms.JMSProducer;
import javax.jms.Topic;
import java.util.UUID;

/**
 * A sender of user message within the application.
 * @author miguel
 */
@JMSDestinationDefinitions(
    value =  {
        @JMSDestinationDefinition(
            name = "java:/topic/messages",
            interfaceName = "javax.jms.Topic",
            destinationName = "UserMessageListener"
        )
    }
)
@Stateless
public class UserMessageSender {

  private static final MyLogger logger = MyLogger.getLogger("UserMessage");

  @Resource(lookup = "java:/topic/messages")
  private Topic topic;

  @Inject
  private JMSContext context;

  /**
   * Sends the specified message within the application to all users.
   * @param message the message to send.
   */
  public void send(UserMessage message) {
    logger.info("Send a user message: '" + message.getText() + "'");
    try {
      JMSProducer producer = context.createProducer();
      message.setId(UUID.randomUUID().toString());
      producer.send(topic, message);
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
    }
  }
}
