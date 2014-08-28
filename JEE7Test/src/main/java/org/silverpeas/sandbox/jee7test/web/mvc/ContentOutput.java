package org.silverpeas.sandbox.jee7test.web.mvc;

import org.apache.commons.fileupload.FileUpload;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.text.MessageFormat;

/**
 * @author mmoquillon
 */
public class ContentOutput implements RoutingResponse {

  private static final String CONTENT_LENGTH = "Content-Length";
  private static final String CONTENT_DISPOSITION = "Content-Disposition";
  private static final String CONTENT_DISPOSITION_VALUE_TEMPLATE = "attachment; filename=\"{0}\"";

  private final BinaryContent content;

  public ContentOutput(BinaryContent content) {
    this.content = content;
  }

  @Override
  public void routes(final HttpServletRequest request, final HttpServletResponse response)
      throws ServletException, IOException {
    response.setHeader(CONTENT_LENGTH, String.valueOf(content.getContentSize()));
    if (content.hasContentType()) {
      response.setContentType(content.getContentType());
    }
    if (content.hasContentName()) {
      response.setHeader(CONTENT_DISPOSITION,
          MessageFormat.format(CONTENT_DISPOSITION_VALUE_TEMPLATE, content.getContentName()));
    }
    OutputStream output = response.getOutputStream();
    IOUtils.copy(content.getInputStream(), output);
    output.flush();
  }
}
