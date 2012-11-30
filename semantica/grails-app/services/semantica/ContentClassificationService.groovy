package semantica

import com.sun.jersey.api.client.Client
import org.apache.commons.io.FilenameUtils
import org.apache.tika.Tika
import org.apache.tika.language.LanguageIdentifier
import org.apache.tika.metadata.Metadata
import org.apache.tika.metadata.OfficeOpenXMLCore
import org.apache.tika.metadata.TikaCoreProperties
import org.carrot2.core.Document

import javax.persistence.PersistenceException

class ContentClassificationService {

  def indexation
  def categorization

  /**
   * Classifies the document referred by the specified description into the classification subsystem.
   * The classification is done in two ways: by indexation with the indexation engine and by
   * text categorization according to known taxonomies with the categorization engine.
   * It throws an exception with a message if the classification fails.
   * @param document the File instance in which is stored the content to classify.
   * @param descriptor a descriptor with classification properties.
   */
  def classify(descriptor) {
    ContentAttributes attributes = ContentAttributes.findByContent(descriptor)
    File document = new File(descriptor.location)
    setContentAttributes(attributes, parse(document))

    indexation.index([file: document, attributes: attributes])
    categorization.categorize([file: document, attributes: attributes])
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
    def results = indexation.search(query)
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

  private def setContentAttributes(ContentAttributes attributes, metadata) {
    if (metadata) {
      attributes.title = metadata.get(TikaCoreProperties.TITLE)
      attributes.description = metadata.get(TikaCoreProperties.DESCRIPTION)
      attributes.language = metadata.get(TikaCoreProperties.LANGUAGE)
      attributes.keywords = metadata.get(TikaCoreProperties.KEYWORDS)
      attributes.save()
    }
  }

  /**
   * Parses the specified file and returns both a Reader on the content file and the document
   * metadata.
   * @param file the file containing the document to parse.
   * @return a Reader on the content and the content Metadata.
   */
  private def parse(File file) {
    final short LinesMaxNumber = 5
    Tika parser = new Tika()
    Metadata metadata = new Metadata()
    Reader content
    try {
      content = parser.parse(new FileInputStream(file), metadata)
      String title = metadata.get(TikaCoreProperties.TITLE)
      String description = metadata.get(OfficeOpenXMLCore.SUBJECT)
      if (!description)
        description = metadata.get(TikaCoreProperties.DESCRIPTION)
      String language = metadata.get(TikaCoreProperties.LANGUAGE)

      if (title == null || title.trim().length() == 0) {
        metadata.set(TikaCoreProperties.TITLE,
            FilenameUtils.getBaseName(file.name).replaceAll("[,-_\\.]|(%20)", " "))
      }

      if (description == null || description.trim().length() == 0 || language == null) {
        description = ""
        BufferedReader buffer = new BufferedReader(content)
        String contentLine
        for (short lineCount = 0; lineCount < LinesMaxNumber && (contentLine = buffer.readLine())
            != null; lineCount++) {
          if (contentLine)
            description += contentLine + "\n"
          else
            lineCount--
        }
        if (description)
          metadata.set(TikaCoreProperties.DESCRIPTION, description)
        if (!language) {
          LanguageIdentifier identifier = new LanguageIdentifier(description)
          metadata.set(TikaCoreProperties.LANGUAGE, identifier.language)
        }
      }
    } catch (Exception ex) {
      log.error("${file.name} content parsing failed!", ex)
      metadata.set(TikaCoreProperties.TITLE,
          FilenameUtils.getBaseName(file.name).replaceAll("[,-_\\.]|(%20)", " "))
    } finally {
      try {
        content?.close()
      } catch (IOException ex) {
        log.warn(ex.getMessage())
      }
    }
    return metadata
  }
}
