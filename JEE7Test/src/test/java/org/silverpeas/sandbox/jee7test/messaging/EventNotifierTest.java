package org.silverpeas.sandbox.jee7test.messaging;

import org.junit.Before;
import org.junit.Test;
import org.silverpeas.sandbox.jee7test.test.util.Reflection;

import javax.jms.JMSContext;
import javax.jms.JMSProducer;
import javax.jms.Queue;

import java.lang.reflect.Field;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author mmoquillon
 */
public class EventNotifierTest {

  private EventNotifier eventNotifier = new EventNotifier();
  private JMSProducer producer;
  private Queue queue;

  @Before
  public void prepareCommonBehaviors() throws Exception {
    JMSContext jms = mock(JMSContext.class);
    queue = mock(Queue.class);
    producer = mock(JMSProducer.class);

    when(jms.createProducer()).thenReturn(producer);

    Field dependency = EventNotifier.class.getDeclaredField("jms");
    Reflection.setField(eventNotifier, dependency, jms);

    dependency = EventNotifier.class.getDeclaredField("queue");
    Reflection.setField(eventNotifier, dependency, queue);
  }

  @Test
  public void notifyAnEventShouldSentAMessageIntoTheJMSQueue() {
    final String event = "This is my event";
    eventNotifier.notify(event);

    verify(producer).send(queue, event);
  }
}
