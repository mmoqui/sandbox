package org.silverpeas.sandbox.jee7test.messaging;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author mmoquillon
 */
@MessageDriven(name = "EventNotification", activationConfig = {
        @ActivationConfigProperty(propertyName = "destinationLookup", propertyValue = "queue/notification"),
        @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
        @ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge") })
public class EventNotificationReporter implements MessageListener {

  private Logger logger = Logger.getLogger(getClass().getSimpleName());

  @Override
  public void onMessage(final Message message) {
    TextMessage notification;
    try {
      if (message instanceof TextMessage) {
        notification = (TextMessage) message;
        logger.log(Level.INFO, notification.getText());
      } else {
        logger.log(Level.WARNING, "Invalid event notification received");
      }
    } catch (JMSException e) {
      throw new RuntimeException(e);
    }
  }
}
