package org.silverpeas.sandbox.jee7test.jcr.model;

import org.apache.commons.io.FilenameUtils;
import org.apache.tika.Tika;
import org.silverpeas.sandbox.jee7test.jcr.repository.DocumentRepository;
import org.silverpeas.sandbox.jee7test.util.ServiceProvider;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

/**
 * @author mmoquillon
 */
public class Document implements Serializable {

  private String id;
  private String title;
  private String path;
  private Date updateDate;
  private String mimeType;
  private long size;

  public static Set<Document> getAll() {
    DocumentRepository repository = ServiceProvider.getService(DocumentRepository.class);
    return repository.getAllDocuments();
  }

  public static Document getById(String id) {
    DocumentRepository repository = ServiceProvider.getService(DocumentRepository.class);
    return repository.getDocumentById(id);
  }

  protected Document() {
  }

  public Document(final String title, final String path, String mimeType) throws IOException {
    this.title = title;
    this.updateDate = new Date();
    this.path = path;
    this.size = new File(path).length();
    if (mimeType.toLowerCase().contains("stream") || mimeType.toLowerCase().contains("download")) {
      Tika tika = new Tika();
      this.mimeType = tika.detect(getInputStream(), getTitle());
    } else {
      this.mimeType = mimeType;
    }
  }

  public Document(final String title, final String path) throws IOException {
    this.title = title;
    this.path = path;
    this.size = new File(path).length();
    this.updateDate = new Date();
    Tika tika = new Tika();
    this.mimeType = tika.detect(getInputStream(), getTitle());
  }

  public String getId() {
    return id;
  }

  public String getTitle() {
    return title;
  }

  public Date getUpdateDate() {
    return updateDate;
  }

  public String getMimeType() {
    return mimeType;
  }

  public long size() {
    return size;
  }

  public String getFileName() {
    return FilenameUtils.getName(path);
  }

  public InputStream getInputStream() throws IOException {
    if (path != null && !path.trim().isEmpty()) {
      return new BufferedInputStream(new FileInputStream(path));
    }
    throw new FileNotFoundException("The path of the document content isn't defined!");
  }

  public void save() {
    DocumentRepository repository = ServiceProvider.getService(DocumentRepository.class);
    repository.putDocument(this);
  }
}
