package semantica.carrot

import org.carrot2.core.Document
import org.carrot2.util.attribute.IBindableDescriptor
import org.carrot2.util.attribute.AttributeInfo
import org.carrot2.core.attribute.CommonAttributesDescriptor

/**
 *  Metadata and attributes of the SemanticaDocumentSource component. You can use this descriptor to
 *  obtain metadata, such as human readable name and description, about the component as a whole as
 *  well as about its attributes. Using the attributeBuilder(Map) you can obtain a builder for
 *  type-safe generation of the attribute maps.
 */
final class SemanticaDocumentSourceDescriptor {

  public static AttributeBuilder attributeBuilder(Map<String, Object> attributes) {
    return new AttributeBuilder(attributes)
  }

  public static class AttributeBuilder {

    private Map<String, Object> attributes

    protected AttributeBuilder(final Map<String, Object> attributes) {
      this.attributes = attributes
    }

    public List<Document> documents() {
      return this.attributes.get(SemanticaAttributeNames.DOCUMENTS)
    }

    public AttributeBuilder documents(List<Document> documents) {
      this.attributes.put(SemanticaAttributeNames.DOCUMENTS, documents)
      return this
    }

    public AttributeBuilder sourceUrl(String sourceUrl) {
      this.attributes.put(SemanticaAttributeNames.SOURCE_URL, sourceUrl)
      return this
    }

    public AttributeBuilder query(String query) {
      this.attributes.put(SemanticaAttributeNames.QUERY, query)
      return this
    }
  }
}
