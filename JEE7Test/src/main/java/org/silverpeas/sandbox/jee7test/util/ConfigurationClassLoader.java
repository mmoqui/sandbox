package org.silverpeas.sandbox.jee7test.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.io.File.separator;
import static java.io.File.separatorChar;

/**
 * @author mmoquillon
 */
public class ConfigurationClassLoader extends ClassLoader {

  private static String SILVERPEAS_HOME;

  static {
    SILVERPEAS_HOME = System.getenv("SILVERPEAS_HOME");
    if (SILVERPEAS_HOME == null || SILVERPEAS_HOME.trim().isEmpty()) {
      SILVERPEAS_HOME = System.getProperty("SILVERPEAS_HOME");
    }
  }

  private String baseDir = SILVERPEAS_HOME + separatorChar;

  @Override
  public synchronized void clearAssertionStatus() {
    super.clearAssertionStatus();
  }

  @Override
  protected Package definePackage(String name, String specTitle, String specVersion,
      String specVendor, String implTitle, String implVersion, String implVendor, URL sealBase)
      throws IllegalArgumentException {
    return super.definePackage(name, specTitle, specVersion, specVendor, implTitle, implVersion,
        implVendor, sealBase);
  }

  @Override
  protected Class<?> findClass(String name) throws ClassNotFoundException {
    return super.findClass(name);
  }

  @Override
  protected String findLibrary(String libname) {
    return super.findLibrary(libname);
  }

  @Override
  protected URL findResource(String name) {
    return super.findResource(name);
  }

  @Override
  protected Enumeration<URL> findResources(String name) throws IOException {
    return super.findResources(name);
  }

  @Override
  protected Package getPackage(String name) {
    return super.getPackage(name);
  }

  @Override
  protected Package[] getPackages() {
    return super.getPackages();
  }

  @Override
  public URL getResource(String name) {
    URL resource = super.getResource(name);
    if (resource == null && name != null) {
      String fileName = baseDir + name;
      File file = new File(fileName);
      if (file.exists()) {
        try {
          resource = file.toURI().toURL();
        } catch (MalformedURLException ex) {
          Logger.getLogger(ConfigurationClassLoader.class.getName()).log(Level.SEVERE, null, ex);
          resource = super.getResource(name);
        }
      }
    }
    return resource;
  }

  @Override
  public InputStream getResourceAsStream(String name) {
    InputStream inputStream = super.getResourceAsStream(name);
    if (inputStream == null && name != null) {
      String fileName = baseDir + name;
      File file = new File(fileName);
      if (file.exists()) {
        try {
          inputStream = new FileInputStream(file);
        } catch (FileNotFoundException ex) {
          Logger.getLogger(ConfigurationClassLoader.class.getName()).log(Level.SEVERE, null, ex);
          return null;
        }
      }
    }
    return inputStream;
  }

  @Override
  public Enumeration<URL> getResources(String name) throws IOException {
    return super.getResources(name);
  }

  @Override
  public Class<?> loadClass(String name) throws ClassNotFoundException {
    return super.loadClass(name);
  }

  @Override
  protected synchronized Class<?> loadClass(String name, boolean resolve)
      throws ClassNotFoundException {
    return super.loadClass(name, resolve);
  }

  @Override
  public synchronized void setClassAssertionStatus(String className, boolean enabled) {
    super.setClassAssertionStatus(className, enabled);
  }

  @Override
  public synchronized void setDefaultAssertionStatus(boolean enabled) {
    super.setDefaultAssertionStatus(enabled);
  }

  @Override
  public synchronized void setPackageAssertionStatus(String packageName, boolean enabled) {
    super.setPackageAssertionStatus(packageName, enabled);
  }

  public ConfigurationClassLoader(ClassLoader parent) {
    this(parent, SILVERPEAS_HOME + separatorChar + "properties");
  }

  public ConfigurationClassLoader(ClassLoader parent, String directory) {
    super(parent);
    assert directory != null;
    if (directory.endsWith(separator)) {
      this.baseDir = directory;
    } else {
      this.baseDir = directory + separatorChar;
    }
  }
}

