package org.silverpeas.sandbox.jee7test.jcr.model;

import org.apache.commons.io.FileUtils;
import org.silverpeas.sandbox.jee7test.test.util.Reflection;

import java.io.File;
import java.io.IOException;

/**
 * @author mmoquillon
 */
public class DocumentBuilder {

  public static Document anExistingDocument(String id, String title) throws Exception {
    File content = new File(FileUtils.getTempDirectory(), "papoos.txt");
    FileUtils.write(content, "Coucou les papoos");

    Document document = new Document(title, content.getAbsolutePath(), "plain/text");
    Reflection.setField(document, Document.class.getDeclaredField("id"), id);

    return document;
  }

  public static Document aNewDocument(String title) throws Exception {
    File content = new File(FileUtils.getTempDirectory(), "papoos.txt");
    FileUtils.write(content, "Coucou les papoos");

    Document document = new Document(title, content.getAbsolutePath(), "plain/text");
    return document;
  }
}
