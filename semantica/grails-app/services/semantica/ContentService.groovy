package semantica

import com.sun.jersey.api.client.Client
import com.sun.jersey.api.client.WebResource
import com.sun.jersey.multipart.FormDataMultiPart
import com.sun.jersey.multipart.file.FileDataBodyPart
import javax.ws.rs.core.MediaType
import grails.converters.JSON

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
   * Throws an exception if an unexpected error raises.
   * @param content the content embodied into a file.
   * @param fileName the name of the repository file into which the content will be saved.
   */
  def save(content, String fileName) {
    log.info "Save a content into the file ${fileName}"
    def descriptor = ContentStorageDescriptor.findByName(fileName)
    if (!descriptor) {
      log.info "The content is new, references it in the system"
      descriptor = new ContentStorageDescriptor(name: fileName, location: CONTENT_REPOSITORY + fileName)
      descriptor.save(flush: true)
    }
    File storage = new File(descriptor.location)
    content.transferTo(storage)
    classify(storage, descriptor)
  }

  /**
   * Finds the content that match the specified query.
   * Throws an exception if an unexpected error raises.
   * @param query a well formatted query.
   * @return a list of content descriptors each of them describing a matching content.
   */
  def findByQuery(String query) {
    log.info "Find contents matching the query: ${query}"
    def descriptors = []
    search(query).each {
      if (it.stored) {
        def contentDescriptor = ContentStorageDescriptor.get(it.id)
        if (contentDescriptor) {
          descriptors << contentDescriptor
        }
      }
    }
    return descriptors
  }

  /**
   * Finds the content that is identified by the specified unique identifier.
   * Throws an exception if an unexpected error raises.
   * @param id the unique identifier of the content to find.
   * @return the content to find or null if no such content exists
   */
  def findById(id) {
    log.info "Find the content by its unique identifier ${id}"
    return ContentStorageDescriptor.get(id)
  }

  /**
   * Classifies the specified content into the classification subsystem with the specified
   * classification descriptor.
   * It throws an exception with a message if the classification fails.
   * @param content the File instance with the content to classify.
   * @param descriptor a descriptor with classification properties.
   */
  private def classify(content, descriptor) {
    String query = "literal.id=${descriptor.id}&literal.stored=true"
    descriptor.properties.grep { !['class', 'metaClass', 'active', 'id'].contains(it.key) }.each {
      if (it.value instanceof String)
        query += "&literal.${it.key}=${URLEncoder.encode(it.value, 'UTF-8')}"
      else
        query += "&literal.${it.key}=${it.value}"
    }
    Client client = new Client()
    WebResource solr = client.resource(grailsApplication.config.solr.url +
        "update/extract?${query}&wt=json&commit=true");
    FormDataMultiPart multipart = new FormDataMultiPart().bodyPart(new FileDataBodyPart("file-attachement", content));
    def response = JSON.parse(
        solr.type(MediaType.MULTIPART_FORM_DATA).
            accept(MediaType.APPLICATION_JSON).
            post(String.class, multipart))
    if (response.responseHeader.status != 0) {
      throw new RuntimeException("The classification of ${descriptor.name} has failed (status = ${response.responseHeader.status})")
    }
  }

  /**
   * Searches the contents that match the specified query.
   * @param query the query.
   * @return a list of classification descriptors, each of them referring a particular classified
   * content. The descriptor has at least as properties the unique identifier and the name of the
   * classified content.
   */
  private def search(String query) {
    Client client = new Client()
    WebResource solr = client.resource(grailsApplication.config.solr.url +
        "clustering/?q=${query}&fl=*,score&facet=true&indent=on&wt=json")
    def results = JSON.parse(
        solr.accept(MediaType.APPLICATION_JSON).
            get(String.class))
    if (results.responseHeader.status != 0) {
      throw new RuntimeException("The query ${query} has failed (status = ${results.responseHeader.status})")
    }
    println results
    return results.response.docs
  }
}
