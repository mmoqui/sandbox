package semantica

import com.sun.jersey.api.client.Client
import com.sun.jersey.api.client.WebResource
import com.sun.jersey.multipart.FormDataMultiPart
import com.sun.jersey.multipart.file.FileDataBodyPart
import javax.ws.rs.core.MediaType

class ContentService {
  
  static final String CONTENT_REPOSITORY = System.getenv("HOME") + "/tmp/semantica/"
  
  static {
    File repository = new File(CONTENT_REPOSITORY)
    if (!repository.exists()) {
      repository.mkdirs()
    }
  }
  
  def grailsApplication

  /**
   * Saves the specified content into a file with the specified name.
   */
  def save(content, String fileName) {
    log.info "Save a content into the file ${fileName}"
    def descriptor = StorageDescriptor.findByName(fileName)
    if (!descriptor) {
      log.info "The content is new, references it in the system"
      descriptor = new StorageDescriptor(name: fileName, location: CONTENT_REPOSITORY + fileName)
      descriptor.save(flush: true)
    }
    File storage = new File(descriptor.location)
    content.transferTo(storage)
    classify(storage, descriptor)
  }
  
  /**
   * Classifies the specified content into the classification subsystem with the specified
   * classification descriptor.
   */
  private def classify(content, descriptor) {
    Client client = new Client()
    WebResource solr = client.resource(grailsApplication.config.solr.url +
        "update/extract?literal.id=${descriptor.id}&literal.name=${URLEncoder.encode(descriptor.name, 'UTF-8')}&commit=true");
    FormDataMultiPart multipart = new FormDataMultiPart().bodyPart(new FileDataBodyPart("file-attachemnt", content));
    solr.type(MediaType.MULTIPART_FORM_DATA).post(multipart)
  }
}
