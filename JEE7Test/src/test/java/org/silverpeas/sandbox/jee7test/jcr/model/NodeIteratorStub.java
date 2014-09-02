package org.silverpeas.sandbox.jee7test.jcr.model;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import java.util.Arrays;
import java.util.Iterator;

/**
* @author mmoquillon
*/
class NodeIteratorStub implements NodeIterator {

  private final Iterator<Node> iterator;

  public NodeIteratorStub(Node... nodes) {
    iterator = Arrays.asList(nodes).iterator();
  }

  @Override
  public Node nextNode() {
    return iterator.next();
  }

  @Override
  public void skip(final long skipNum) {

  }

  @Override
  public long getSize() {
    return 0;
  }

  @Override
  public long getPosition() {
    return 0;
  }

  @Override
  public boolean hasNext() {
    return iterator.hasNext();
  }

  @Override
  public Object next() {
    return iterator.next();
  }
}
