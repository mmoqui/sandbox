package net.moquillon.resteasytest.web;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUpload;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.RequestContext;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.jboss.resteasy.annotations.providers.multipart.PartType;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.FormParam;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * @author mmoquillon
 */
public class FileUploadFormData {

  private InputStream file;

  private String name;

  public final FileUploadFormData parse(HttpServletRequest request)
      throws FileUploadException, IOException {
    ServletFileUpload uploader = new ServletFileUpload(new DiskFileItemFactory());
    List<FileItem> items = uploader.parseRequest(request);
    for (FileItem anItem: items) {
      if (anItem.isFormField() && anItem.getFieldName().equals("name")) {
        name = anItem.getString();
      } else {
        file = anItem.getInputStream();
        if (name == null) {
          name = anItem.getName();
        }
      }
    }
    return this;
  }

  public InputStream getFile() {
    return file;
  }

  public String getName() {
    return name;
  }
}
