package org.silverpeas.sandbox.jee7test.web.mvc;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author mmoquillon
 */
public class Parameters {

  private Map<String, Object> p = new HashMap<>();

  public <T> T get(String parameterName) {
    return (T) p.get(parameterName);
  }

  public <T> void put(String parameterName, T parameterValue) {
    p.put(parameterName, parameterValue);
  }

  public Set<String> getParameterNames() {
    return p.keySet();
  }

  public boolean contains(String parameterName) {
    return p.containsKey(parameterName);
  }
}
