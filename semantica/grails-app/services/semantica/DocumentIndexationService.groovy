package semantica

import javax.persistence.PersistenceException
import org.carrot2.core.Document
import com.sun.jersey.api.client.Client
import com.sun.jersey.api.client.WebResource
import com.sun.jersey.multipart.MultiPart
import com.sun.jersey.multipart.FormDataMultiPart
import com.sun.jersey.multipart.file.FileDataBodyPart
import javax.ws.rs.core.MediaType
import grails.converters.JSON
import semantica.carrot.SemanticaDocumentSourceDescriptor
import org.carrot2.core.ControllerFactory
import org.carrot2.core.Controller
import semantica.carrot.SemanticaDocumentSource
import org.carrot2.clustering.lingo.LingoClusteringAlgorithm
import org.carrot2.core.ProcessingResult

/**
 * Service dedicated to the indexation of documents.
 */
class DocumentIndexationService {

  def grailsApplication

  private Client client = new Client()

  /**
   * Indexes the specified document. The document must be an object defined by two properties:
   * <ul>
   *   <li>file: the File storing the document,</li>
   *   <li>attributes: the metadata qualifying the document.</li>
   * </ul>
   * @param document the document to index, defined as indicated above.
   */
  def index(document) {
    String query = "literal.id=${document.attributes.id}" +
        "&literal.url=file:${URLEncoder.encode(document.attributes.content.location, 'UTF-8')}" +
        "&literal.name=${URLEncoder.encode(document.attributes.content.name, 'UTF-8')}&literal.stored=true"
    document.attributes.properties.grep { !['class', 'metaClass', 'active', 'id', 'content', 'keywords'].contains(it.key) }.each {
      if (it.value instanceof String)
        query += "&literal.${it.key}=${URLEncoder.encode(it.value, 'UTF-8')}"
      else
        query += "&literal.${it.key}=${it.value}"
    }
    WebResource solr = client.resource(grailsApplication.config.solr.url +
        "update/extract?${query}&wt=json&commit=true");
    MultiPart multipart = new FormDataMultiPart().bodyPart(
        new FileDataBodyPart("file-attachement", document.file));
    def response = JSON.parse(
        solr.type(MediaType.MULTIPART_FORM_DATA).
            accept(MediaType.APPLICATION_JSON).
            post(String.class, multipart))
    if (response.responseHeader.status != 0) {
      throw new ApplicationException("The indexation of ${document.attributes.content.name} has failed " +
          "(status = ${response.responseHeader.status})")
    }
  }

  /**
   * Searches the documents that match the specified query.
   * The documents are clustered into one or more categories according to their common hidden
   * properties.
   * @param query the text of the query.
   * @return an a search result object defined by two properties:
   * <ul>
   *   <li>documents: a list of objects representing a document matching the query; it is defined
   *   by the following properties:
   *   <ul>
   *     <li>title: the document title,</li>
   *     <li>id: the document identifier among the search results,</li>
   *     <li>score: the score of the document in its matching with the query.</li>
   *   </ul></li>
   *   <li>clusters: a list of clusters into which are clustered the documents matching the query.
   *   Each cluster is represented by an object defined by the following properties:
   *   <ul>
   *     <li>label: the cluster label,</li>
   *     <li>score: the score of the cluster according to its pertinence with the query,</li>
   *     <li>allDocuments: the list of documents of this cluster.</li>
   *   </ul></li>
   */
  def search(String query) {
    Controller controller = ControllerFactory.createSimple()
    controller.init()
    Map<String, Object> attributes = [:]
    SemanticaDocumentSourceDescriptor.attributeBuilder(attributes).
        query(query).
        sourceUrl(grailsApplication.config.solr.url)
    ProcessingResult results = controller.process(attributes, SemanticaDocumentSource.class,
        LingoClusteringAlgorithm.class)
    return [documents: results.documents, clusters: results.clusters]
  }
}
