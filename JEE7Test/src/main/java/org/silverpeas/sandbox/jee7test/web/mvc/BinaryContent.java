package org.silverpeas.sandbox.jee7test.web.mvc;

import java.io.InputStream;

/**
 * @author mmoquillon
 */
public class BinaryContent extends Parameters {

  private static final String INPUT_STREAM = "__binary__";
  private static final String CONTENT_TYPE = "__contenttype__";
  private static final String CONTENT_SIZE = "__contentsize__";
  private static final String CONTENT_NAME = "__contentname__";

  public void setInputStream(final InputStream stream, final long size) {
    put(INPUT_STREAM, stream);
    put(CONTENT_SIZE, size);
  }

  public void setContentType(String contentType) {
    put(CONTENT_TYPE, contentType);
  }

  public void setContentName(String contentName) {
    this.put(CONTENT_NAME, contentName);
  }

  public InputStream getInputStream() {
    return get(INPUT_STREAM);
  }

  public String getContentType() {
    return get(CONTENT_TYPE);
  }

  public long getContentSize() {
    return get(CONTENT_SIZE);
  }

  public String getContentName() {
    return get(CONTENT_NAME);
  }

  public boolean hasContentType() {
    return contains(CONTENT_TYPE);
  }

  public boolean hasContentName() {
    return contains(CONTENT_NAME);
  }
}
