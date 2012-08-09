package semantica

import com.sun.jersey.api.client.Client
import com.sun.jersey.api.client.WebResource
import com.sun.jersey.multipart.FormDataMultiPart
import com.sun.jersey.multipart.file.FileDataBodyPart
import grails.converters.JSON
import org.carrot2.clustering.lingo.LingoClusteringAlgorithm
import org.carrot2.core.Controller
import org.carrot2.core.ControllerFactory
import org.carrot2.core.Document
import org.carrot2.core.ProcessingResult
import org.carrot2.core.attribute.CommonAttributesDescriptor
import semantica.carrot.SemanticaDocumentSource

import javax.persistence.PersistenceException
import javax.ws.rs.core.MediaType
import semantica.carrot.SemanticaDocumentSourceDescriptor

class ContentClassificationService {

  def grailsApplication

  Client client = new Client()

  /**
   * Classifies the specified document into the classification subsystem with the specified
   * classification descriptor.
   * It throws an exception with a message if the classification fails.
   * @param document the File instance in which is stored the content to classify.
   * @param descriptor a descriptor with classification properties.
   */
  def classify(document, descriptor) {
    String query = "literal.id=${descriptor.id}&literal.stored=true"
    descriptor.properties.grep { !['class', 'metaClass', 'active', 'id'].contains(it.key) }.each {
      if (it.key == 'location') {
        query += "&literal.url=file:${URLEncoder.encode(it.value, 'UTF-8')}"
      } else if (it.value instanceof String)
        query += "&literal.${it.key}=${URLEncoder.encode(it.value, 'UTF-8')}"
      else
        query += "&literal.${it.key}=${it.value}"
    }
    WebResource solr = client.resource(grailsApplication.config.solr.url +
        "update/extract?${query}&wt=json&commit=true");
    FormDataMultiPart multipart = new FormDataMultiPart().bodyPart(new FileDataBodyPart("file-attachement", document));
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
   * The contents are clustered into one or more categories according to their common hidden
   * properties.
   * @param query the text of the query.
   * @return a list of search results, each of them referring a particular classified
   * content. The descriptor has at least as properties the unique identifier and the name of the
   * classified content.
   */
  def search(String query) {
    Controller controller = ControllerFactory.createSimple()
    controller.init()
    Map<String, Object> attributes = [:]
    SemanticaDocumentSourceDescriptor.attributeBuilder(attributes).
        query(query).
        sourceUrl(grailsApplication.config.solr.url)
    ProcessingResult results = controller.process(attributes, SemanticaDocumentSource.class, LingoClusteringAlgorithm.class)
    return searchResultWith(results.documents, results.clusters)
  }

  private def searchResultWith(contents, clusters) {
    def searchResultContents = contents.collect { doc ->
      ContentStorageDescriptor descriptor = ContentStorageDescriptor.findByName(doc.title)
      if (descriptor == null)
        throw new PersistenceException("Descriptor of content ${doc.title} not found!")
      descriptor
    }
    def searchResultClusters
    if (!clusters.empty && contents.size() >= 10) {
      searchResultClusters = clusters.collect {
        new SearchResultCluster(label: it.label, contents:
            it.allDocuments.collect { Document doc ->
              ContentStorageDescriptor descriptor = ContentStorageDescriptor.findByName(doc.title)
              if (descriptor == null)
                throw new PersistenceException("Descriptor of content ${doc.title} not found!")
              descriptor
            })
      }
    } else {
      searchResultClusters = []
    }
    return new SearchResult(contents: searchResultContents, clusters: searchResultClusters)
  }
}
