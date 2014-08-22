package org.silverpeas.sandbox.jee7test.service;

import javax.inject.Singleton;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * @author mmoquillon
 */
@Singleton
public class MessageBucket {

  private Set<String> messages = Collections.synchronizedSet(new HashSet<String>());

  public void pour(String message) {
    System.out.println("HEY, I JUST RECEIVE THIS MESSAGE ====> " + message);
    messages.add(message);
  }

  public Set<String> getContent() {
    return Collections.unmodifiableSet(messages);
  }

  public void empty() {
    messages.clear();
  }
}
