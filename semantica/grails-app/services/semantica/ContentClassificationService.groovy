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

import javax.ws.rs.core.MediaType
import javax.persistence.PersistenceException
import org.carrot2.core.LanguageCode

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
      if (it.value instanceof String)
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
   * @param query the text of the query.
   * @return a list of search results, each of them referring a particular classified
   * content. The descriptor has at least as properties the unique identifier and the name of the
   * classified content.
   */
  def search(String query) {
    // requests the search engine for the documents matching the query
    WebResource solr = client.resource(grailsApplication.config.solr.url +
        "select/?q=${URLEncoder.encode(query, 'UTF-8')}&fl=*,score&indent=on&wt=json")
    def results = JSON.parse(
        solr.accept(MediaType.APPLICATION_JSON).
            get(String.class))
    if (results.responseHeader.status != 0) {
      throw new RuntimeException("The query ${query} has failed (status = ${results.responseHeader.status})")
    }

    // clusters the returned documents with Carrot²
    def docs = results.response.docs.grep { it.stored }
    def clusters = clustersDocuments(docs.collect { doc ->
      String summary = doc.description
      if (!summary && doc.subject) {
        summary = doc.subject
      }
      new Document(doc.name, summary, "file:${doc.location}", LanguageCode.FRENCH).setField("id", doc.id)
    }, query)
    return searchResultWith(docs, clusters)
  }

  /**
   * Applies a clustering algorithm on the specified documents sent back by the specified query.
   * The clustering algorithm is only applied if there is more than 10 documents to categorize.
   * @param documents the documents to categorize into one or more clusters.
   * @param query the search query at the origin of the documents fetching.
   * @return the resulted clusters or an empty list if either no clusters were found or there is
   * less than or equal to 10 documents.
   */
  private def clustersDocuments(documents, query) {
    if (documents.size >= 10) {
      Controller controller = ControllerFactory.createSimple()
      ProcessingResult results = controller.process(documents, query, LingoClusteringAlgorithm.class)
      return (results.clusters ? results.clusters : [])
    }
    return []
  }

  private def searchResultWith(contents, clusters) {
    if (!clusters.empty) {
      def searchResultClusters = clusters.collect {
        new SearchResultCluster(label: it.label, contents:
          it.allDocuments.collect { Document doc ->
            ContentStorageDescriptor descriptor = ContentStorageDescriptor.get(doc.getField("id"))
            if (descriptor == null)
              throw new PersistenceException("Descriptor of content ${doc.title} (id=${doc.getField("id")}) not found!")
            descriptor
          })
      }
      return new SearchResult(contents: contents, clusters: searchResultClusters)
    } else {
      return new SearchResult(contents: contents, clusters: [])
    }
  }
}
