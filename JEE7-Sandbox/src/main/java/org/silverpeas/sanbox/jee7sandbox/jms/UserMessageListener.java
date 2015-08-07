package org.silverpeas.sanbox.jee7sandbox.jms;

import org.silverpeas.sanbox.jee7sandbox.bean.UserMessage;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A listener of messages coming from users. It aims to persist all incoming messages.
 * @author miguel
 */
@MessageDriven(name = "UserMessageListener", activationConfig = {
    @ActivationConfigProperty(propertyName = "destinationLookup", propertyValue = "topic/messages"),
    @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Topic"),
    @ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge") })
public class UserMessageListener implements MessageListener {

  @Override
  public void onMessage(Message message) {
    try {
      UserMessage userMessage = message.getBody(UserMessage.class);
      userMessage.save();
    } catch (JMSException e) {
      Logger.getLogger(getClass().getSimpleName()).log(Level.SEVERE, e.getMessage(), e);
    }
  }
}
