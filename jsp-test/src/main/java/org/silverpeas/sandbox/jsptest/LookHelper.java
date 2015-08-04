package org.silverpeas.sandbox.jsptest;

import javax.servlet.http.HttpSession;

/**
 * @author miguel
 */
public interface LookHelper {

  String SESSION_ATT = "Silverpeas_LookHelper";

  static LookHelper getLookHelper(HttpSession session) {
    LookHelper helper = new LookHelperImpl(session);
    session.setAttribute(LookHelper.SESSION_ATT, helper);
    return helper;
  }
}
