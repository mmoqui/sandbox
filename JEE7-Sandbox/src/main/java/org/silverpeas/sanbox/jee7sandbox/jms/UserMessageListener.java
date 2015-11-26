package org.silverpeas.sanbox.jee7sandbox.jms;

import org.silverpeas.sanbox.jee7sandbox.bean.UserMessage;
import org.silverpeas.sanbox.jee7sandbox.util.MyLogger;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;

/**
 * A listener of messages coming from users. It aims to persist all incoming messages.
 * @author miguel
 */
@MessageDriven(name = "UserMessageListener", activationConfig = {
    @ActivationConfigProperty(propertyName = "destinationLookup", propertyValue = "topic/messages"),
    @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Topic"),
    @ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge") })
public class UserMessageListener implements MessageListener {

  private static final MyLogger logger = MyLogger.getLogger("UserMessage");

  @Override
  public void onMessage(Message message) {
    try {
      UserMessage userMessage = message.getBody(UserMessage.class);
      logger.debug("Receive new user message: " + userMessage.getText());
      logger.info("Receive new user message: " + userMessage.getText());
      logger.warn("Receive new user message: " + userMessage.getText());
      logger.error("Receive new user message: " + userMessage.getText());
      userMessage.save();
    } catch (JMSException e) {
      MyLogger.getLogger(getClass().getSimpleName()).error(e.getMessage(), e);
    }
  }
}
