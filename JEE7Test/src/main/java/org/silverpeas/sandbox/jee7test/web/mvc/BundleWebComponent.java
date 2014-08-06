package org.silverpeas.sandbox.jee7test.web.mvc;

import org.silverpeas.sandbox.jee7test.util.MyResourceBundle;
import org.silverpeas.sandbox.jee7test.web.mvc.annotation.View;

import javax.inject.Named;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import java.io.IOException;
import java.io.StringWriter;

/**
 * @author mmoquillon
 */
@Named("bundleWebComponent")
public class BundleWebComponent implements WebComponent {

  @GET
  @Path("view")
  @View("/bundle.jsp")
  public Parameters getBundle(Parameters parameters) {
    Parameters result = new Parameters();
    String bundlePath = parameters.get("bundle");
    String content;
    try {
      if (bundlePath.contains("multilang")) {
        content = loadResourceBundle(bundlePath);
      } else {
        content = loadSettingsBundle(bundlePath);
      }
    } catch (IOException | IllegalArgumentException ex) {
      content = ex.getMessage();
    }
    result.put("bundleName", bundlePath);
    result.put("bundleContent", content);
    return result;
  }

  public String loadSettingsBundle(final String bundlePath)
      throws IOException, IllegalArgumentException {
    String settingsBundle = bundlePath;
    if (bundlePath.endsWith(".properties")) {
      settingsBundle = bundlePath.substring(0, bundlePath.indexOf(".properties"));
    }
    if (settingsBundle.lastIndexOf("_") == settingsBundle.length() - 3) {
      settingsBundle = settingsBundle.substring(0, bundlePath.lastIndexOf("_"));
    }
    settingsBundle = settingsBundle.replaceAll("/", ".");
    MyResourceBundle settings = new MyResourceBundle(settingsBundle);
    if (!bundlePath.trim().isEmpty() && !bundlePath.contains("multilang")) {
      StringWriter bundleContent = new StringWriter();
      settings.asProperties().store(bundleContent, settingsBundle);
      return bundleContent.toString();
    } else {
      throw new IllegalArgumentException("The bundle is not a settings bundle");
    }
  }

  public String loadResourceBundle(final String bundlePath)
      throws IOException, IllegalArgumentException {
    String language = "fr";
    String localizedBundle = bundlePath;
    if (bundlePath.endsWith(".properties")) {
      localizedBundle = bundlePath.substring(0, bundlePath.indexOf(".properties"));
    }
    if (localizedBundle.lastIndexOf("_") == localizedBundle.length() - 3) {
      String askedLanguage = localizedBundle.substring(bundlePath.lastIndexOf("_") + 1);
      if (!askedLanguage.equals("$$")) {
        language = askedLanguage;
      }
      localizedBundle = localizedBundle.substring(0, bundlePath.lastIndexOf("_"));
    }
    localizedBundle = localizedBundle.replaceAll("/", ".");
    MyResourceBundle resource = new MyResourceBundle(localizedBundle, language);
    if (!bundlePath.trim().isEmpty() && bundlePath.contains("multilang")) {
      StringWriter bundleContent = new StringWriter();
      resource.asProperties().store(bundleContent, localizedBundle + " - " + language);
      return bundleContent.toString();
    } else {
      throw new IllegalArgumentException("The bundle is not a resource bundle");
    }
  }
}
