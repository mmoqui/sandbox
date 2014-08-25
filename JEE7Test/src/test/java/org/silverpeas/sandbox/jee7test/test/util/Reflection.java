package org.silverpeas.sandbox.jee7test.test.util;

import java.lang.reflect.Field;

/**
 * @author mmoquillon
 */
public class Reflection {

  public static <T, V> void setField(T object, Field field, V value) throws IllegalAccessException {
    field.setAccessible(true);
    field.set(object, value);
  }
}
