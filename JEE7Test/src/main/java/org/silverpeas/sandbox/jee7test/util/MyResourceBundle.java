package org.silverpeas.sandbox.jee7test.util;

import java.io.Writer;
import java.util.Enumeration;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.Properties;
import java.util.ResourceBundle;

/**
 * @author mmoquillon
 */
public class MyResourceBundle {

  private static final ClassLoader loader = java.security.AccessController
      .doPrivileged(new java.security.PrivilegedAction<ConfigurationClassLoader>() {
        @Override
        public ConfigurationClassLoader run() {
          return new ConfigurationClassLoader(MyResourceBundle.class.getClassLoader());
        }
      });

  private ResourceBundle resourceBundle;

  public MyResourceBundle(String bundlePath) {
    resourceBundle = loadResourceBundle(bundlePath, null);
  }

  public MyResourceBundle(String bundlePath, String language) {
    Locale locale;
    if (language == null || language.trim().isEmpty()) {
      locale = Locale.getDefault();
    } else {
      locale = new Locale(language);
    }
    resourceBundle = loadResourceBundle(bundlePath, locale);
  }

  public String getLanguage() {
    return resourceBundle.getLocale().getLanguage();
  }

  private static ResourceBundle loadResourceBundle(String bundlePath, Locale locale) {
    System.out.println("Load resource bundle " + bundlePath);
    Locale actualLocale = locale;
    if (actualLocale == null) {
      actualLocale = Locale.ROOT;
    }
    return ResourceBundle.getBundle(bundlePath, actualLocale, loader, new ConfigurationControl());
  }

  public String getString(String key) {
    return resourceBundle.getString(key);
  }

  public Properties asProperties() {
    Properties props = new Properties();
    Enumeration<String> keys = resourceBundle.getKeys();
    while (keys.hasMoreElements()) {
      String key = keys.nextElement();
      props.setProperty(key, resourceBundle.getString(key));
    }
    return props;
  }

  private static class ConfigurationControl extends ResourceBundle.Control {

    public static final long DEFAULT_RELOAD = 14400000L; // 4 Hours
    private static long RELOAD = DEFAULT_RELOAD;

    @Override
    public long getTimeToLive(String baseName, Locale locale) {
      return RELOAD;
    }

  }
}
