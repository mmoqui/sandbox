package org.silverpeas.sandbox.jsptest;

import javax.servlet.http.HttpSession;

/**
 * @author miguel
 */
public class LookHelperImpl implements LookHelper {

  private HttpSession session;

  public LookHelperImpl(final HttpSession session) {
    this.session = session;
  }
}
