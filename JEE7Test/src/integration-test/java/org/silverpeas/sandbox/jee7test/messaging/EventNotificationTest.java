package org.silverpeas.sandbox.jee7test.messaging;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.silverpeas.sandbox.jee7test.service.MessageBucket;

import javax.inject.Inject;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * @author mmoquillon
 */
@RunWith(Arquillian.class)
public class EventNotificationTest {

  @Inject
  private EventNotifier notifier;

  @Inject
  private MessageBucket messageBucket;

  @Deployment
  public static Archive<?> createTestArchive() {
    return ShrinkWrap.create(JavaArchive.class, "test.jar")
        .addPackage("org.silverpeas.sandbox.jee7test.messaging")
        .addPackage("org.silverpeas.sandbox.jee7test.service")
        .addPackage("org.silverpeas.sandbox.jee7test.util")
        .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
  }

  @Test
  @Ignore
  public void emptyTest() {
  }

  @Test
  public void theNotificationReporterShouldBeNotifiedAboutAnyEvent() throws InterruptedException {
    final String expectedEvent = "coucou les papoos";
    notifier.notify(expectedEvent);

    Thread.sleep(1000); // to let the time of the JMS message routing.

    assertThat(messageBucket.getContent().size(), is(1));

    String receivedEvent = messageBucket.getContent().iterator().next();
    assertThat(receivedEvent, is(expectedEvent));
  }
}
