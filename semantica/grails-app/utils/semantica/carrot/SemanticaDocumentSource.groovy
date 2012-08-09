package semantica.carrot

import com.sun.jersey.api.client.WebResource
import grails.converters.JSON
import org.carrot2.core.attribute.AttributeNames
import org.carrot2.core.attribute.CommonAttributes
import org.carrot2.core.attribute.Internal
import org.carrot2.core.attribute.Processing

import javax.ws.rs.core.MediaType

import org.carrot2.core.*
import org.carrot2.util.attribute.*
import com.sun.jersey.api.client.Client

/**
 * A document source particular to Semantica and that abstracts the more concrete mechanism used
 * in the content fetching (by database, by index engine, ...)
 */
@Bindable(prefix = "SemanticaDocumentSource", inherit = CommonAttributes.class)
class SemanticaDocumentSource extends ProcessingComponentBase implements IDocumentSource {

  @Output
  @Processing
  @Attribute(key = SemanticaAttributeNames.DOCUMENTS, inherit = true)
  public List<Document> documents;

  @Input
  @Output
  @Processing
  @Attribute(key = SemanticaAttributeNames.QUERY, inherit = true)
  public String query;

  @Input
  @Output
  @Processing
  @Attribute(key = SemanticaAttributeNames.SOURCE_URL, inherit = true)
  public String sourceUrl;

  @Override
  void process() {
    if (this.query == null || this.query.trim().empty) {
      throw new ProcessingException("The query must be given!")
    }

    Client client = new Client()
    WebResource solr = client.resource(sourceUrl +
        "select/?q=${URLEncoder.encode(this.query, 'UTF-8')}&fl=*,score&indent=on&wt=json")
    def results = JSON.parse(
        solr.accept(MediaType.APPLICATION_JSON).
            get(String.class))
    if (results.responseHeader.status != 0) {
      throw new ProcessingException("The query ${this.query} has failed (status = ${results.responseHeader.status})")
    }

    this.documents = results.response.docs.grep { it.stored }.collect { doc ->
      String summary = doc.description
      if (!summary && doc.subject) {
        summary = doc.subject
      }
      new Document(doc.name, summary, "file:${doc.location}", LanguageCode.FRENCH).setField("id", doc.id)
    }
  }
}
