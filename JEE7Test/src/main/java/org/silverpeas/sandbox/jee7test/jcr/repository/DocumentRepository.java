package org.silverpeas.sandbox.jee7test.jcr.repository;

import org.apache.commons.io.FileUtils;
import org.silverpeas.sandbox.jee7test.jcr.model.Document;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.jcr.Binary;
import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.Property;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;
import javax.jcr.nodetype.NodeType;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author mmoquillon
 */
@Singleton
public class DocumentRepository {

  private static final String ID_PREFIX = "document_";

  @Inject
  private Repository jcr;

  protected Session openSession() throws RepositoryException {
    return jcr.login(new SimpleCredentials("admin", "admin".toCharArray()));
  }

  protected void closeSession(Session session) throws RepositoryException {
    if (session.hasPendingChanges()) {
      session.save();
    }
    session.logout();
  }

  protected String computeNodeId() {
    String[] uuidParts = UUID.randomUUID().toString().split(":");
    return ID_PREFIX + uuidParts[uuidParts.length - 1];
  }

  public void putDocument(final Document document) {
    try {
      Session session = openSession();

      Node root = session.getRootNode();
      String documentId = computeNodeId();
      Binary content = session.getValueFactory().createBinary(document.getInputStream());
      Calendar lastModificationDate = Calendar.getInstance();
      lastModificationDate.setTimeInMillis(document.getUpdateDate().getTime());

      Node documentNode = root.addNode(documentId, NodeType.NT_FOLDER);
      Node fileNode = documentNode.addNode(document.getTitle(), NodeType.NT_FILE);
      fileNode.addMixin(NodeType.MIX_TITLE);
      fileNode.setProperty(Property.JCR_TITLE, document.getFileName());
      Node contentNode = fileNode.addNode(Node.JCR_CONTENT, NodeType.NT_RESOURCE);
      contentNode.setProperty(Property.JCR_MIMETYPE, document.getMimeType());
      contentNode.setProperty(Property.JCR_LAST_MODIFIED, lastModificationDate);
      contentNode.setProperty(Property.JCR_DATA, content);

      session.save();
      closeSession(session);

      Field idField = document.getClass().getDeclaredField("id");
      idField.setAccessible(true);
      idField.set(document, documentId);
    } catch (RepositoryException | IOException | ReflectiveOperationException e) {
      throw new JcrRepositoryException(e.getMessage(), e);
    }
  }

  public Document getDocumentById(String id) {
    try {
      Document document = null;
      Session session = openSession();

      Node root = session.getRootNode();
      Node documentNode = root.getNode(id);
      if (documentNode != null) {
        document = buildDocumentFromNode(documentNode);
      }

      closeSession(session);
      return document;
    } catch (RepositoryException e) {
      throw new JcrRepositoryException(e.getMessage(), e);
    }
  }

  public Set<Document> getAllDocuments() {
    try {
      Set<Document> documents = new HashSet<>();
      Session session = openSession();

      Node root = session.getRootNode();
      NodeIterator iterator = root.getNodes();
      while (iterator.hasNext()) {
        Node documentNode = iterator.nextNode();
        if (documentNode.getPrimaryNodeType().isNodeType(NodeType.NT_FOLDER)) {
          documents.add(buildDocumentFromNode(documentNode));
        } else {
          Logger.getLogger(getClass().getSimpleName())
              .log(Level.WARNING, "NODE : " + documentNode.getName());
        }
      }

      closeSession(session);
      return documents;
    } catch (RepositoryException e) {
      throw new JcrRepositoryException(e.getMessage(), e);
    }
  }

  private Document buildDocumentFromNode(final Node documentNode) throws RepositoryException {
    try {
      Node fileNode = documentNode.getNodes().nextNode();
      Node contentNode = fileNode.getNode(Node.JCR_CONTENT);
      String id = documentNode.getName();
      String title = fileNode.getName();
      String fileName = fileNode.getProperty(Property.JCR_TITLE).getString();
      String mimeType = contentNode.getProperty(Property.JCR_MIMETYPE).getString();
      Date updateDate = contentNode.getProperty(Property.JCR_LAST_MODIFIED).getDate().getTime();
      InputStream content = contentNode.getProperty(Property.JCR_DATA).getBinary().getStream();
      String tmpDir = FileUtils.getTempDirectoryPath();
      File contentFile = new File(tmpDir, fileName);
      FileUtils.copyInputStreamToFile(content, contentFile);

      Constructor<Document> constructor = Document.class.getDeclaredConstructor();
      constructor.setAccessible(true);
      Document document = constructor.newInstance();
      setDocumentField(document, "id", id);
      setDocumentField(document, "title", title);
      setDocumentField(document, "mimeType", mimeType);
      setDocumentField(document, "updateDate", updateDate);
      setDocumentField(document, "path", contentFile.getAbsolutePath());
      setDocumentField(document, "size", contentFile.length());
      return document;
    } catch (ReflectiveOperationException | IOException e) {
      throw new JcrRepositoryException(e.getMessage(), e);
    }
  }

  private void setDocumentField(Document document, String fieldName, Object value)
      throws ReflectiveOperationException {
    Field field = Document.class.getDeclaredField(fieldName);
    field.setAccessible(true);
    field.set(document, value);
  }

}