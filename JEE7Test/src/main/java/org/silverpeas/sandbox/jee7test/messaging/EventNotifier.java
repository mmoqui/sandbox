package org.silverpeas.sandbox.jee7test.messaging;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.jms.JMSContext;
import javax.jms.JMSDestinationDefinition;
import javax.jms.JMSDestinationDefinitions;
import javax.jms.JMSProducer;
import javax.jms.Queue;

/**
 * @author mmoquillon
 */
@JMSDestinationDefinitions(
    value = {@JMSDestinationDefinition(
        name = "java:/queue/notification",
        interfaceName = "javax.jms.Queue",
        destinationName = "EventNotification")})
public class EventNotifier {

  @Inject
  private JMSContext jms;

  @Resource(lookup = "java:/queue/notification")
  private Queue queue;

  public void notify(String event) {
    JMSProducer producer = jms.createProducer();
    producer.send(queue, event);
  }
}
