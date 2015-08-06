package org.silverpeas.sanbox.jee7sandbox;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.jms.JMSContext;
import javax.jms.JMSDestinationDefinition;
import javax.jms.JMSDestinationDefinitions;
import javax.jms.JMSProducer;
import javax.jms.Topic;

/**
 * A sender of user message within the application.
 * @author miguel
 */
@JMSDestinationDefinitions(
    value =  {
        @JMSDestinationDefinition(
            name = "java:/topic/HELLOWORLDMDBTopic",
            interfaceName = "javax.jms.Topic",
            destinationName = "HelloWorldMDBTopic"
        )
    }
)
public class UserMessageSender {

  @Resource(lookup = "java:/topic/HELLOWORLDMDBTopic")
  private Topic topic;

  @Inject
  private JMSContext context;

  /**
   * Sends the specified message within the application to all users.
   * @param message the message to send.
   */
  public void send(UserMessage message) {
    JMSProducer producer = context.createProducer();
    message.generateId();
    producer.send(topic, message);
  }
}
