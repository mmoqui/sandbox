package org.silverpeas.sandbox.jee7test.web.mvc;

import org.apache.commons.io.FileUtils;
import org.silverpeas.sandbox.jee7test.jcr.model.Document;
import org.silverpeas.sandbox.jee7test.jcr.repository.JcrRepositoryException;
import org.silverpeas.sandbox.jee7test.web.mvc.annotation.Binary;
import org.silverpeas.sandbox.jee7test.web.mvc.annotation.View;

import javax.inject.Named;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.io.File;
import java.io.IOException;
import java.util.Set;

/**
 * @author mmoquillon
 */
@Named("documentWebComponent")
public class DocumentWebComponent implements WebComponent {

  public static final String ALL_DOCUMENTS = "documents";
  public static final String TITLE = "title";
  public static final String ID = "id";

  @GET
  @Path("all")
  @View("/docs.jsp")
  public Parameters getAllDocuments(Parameters parameters) {
    Parameters result = new Parameters();
    Set<Document> documents = Document.getAll();
    result.put(ALL_DOCUMENTS, documents);
    return  result;
  }

  @POST
  @Path("import")
  @View("/docs.jsp")
  public Parameters importDocument(Parameters parameters) {
    BinaryContent content = (BinaryContent) parameters;
    File uploadDir = FileUtils.getTempDirectory();
    try {
      String title = content.get(TITLE);
      String fileName = content.getContentName();
      File file = new File(uploadDir.getAbsolutePath(), fileName);
      FileUtils.copyInputStreamToFile(content.getInputStream(), file);
      Document document = new Document(title, file.getAbsolutePath(), content.getContentType());
      document.save();
      //file.delete();
    } catch (IOException|JcrRepositoryException e) {
      throw new WebApplicationException(e.getMessage(), Response.Status.CONFLICT);
    }
    return getAllDocuments(parameters);
  }

  @GET
  @Path("document")
  @Binary
  public Parameters getDocument(Parameters parameters) {
    BinaryContent result = new BinaryContent();
    String id = parameters.get(ID);
    try {
      Document document = Document.getById(id);
      result.setInputStream(document.getInputStream(), document.size());
      result.setContentType(document.getMimeType());
      result.setContentName(document.getFileName());

    } catch (NullPointerException|IOException|JcrRepositoryException e) {
      throw new WebApplicationException(e.getMessage(), Response.Status.NOT_FOUND);
    }
    return result;
  }
}
