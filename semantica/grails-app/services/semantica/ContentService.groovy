package semantica

import org.simpleframework.xml.core.PersistenceException
import org.apache.commons.io.FilenameUtils
import org.apache.tika.metadata.TikaCoreProperties
import org.apache.tika.language.LanguageIdentifier
import org.apache.tika.metadata.OfficeOpenXMLCore
import org.apache.tika.metadata.Metadata
import org.apache.tika.Tika

class ContentService {

  static final String CONTENT_REPOSITORY = System.getenv("HOME") + "/tmp/semantica/"

  static {
    File repository = new File(CONTENT_REPOSITORY)
    if (!repository.exists()) {
      repository.mkdirs()
    }
  }

  def documentIndexationService
  def documentClassificationService

  /**
   * Saves the specified content (whatever it is) into a file with the specified name.
   * The content should know how to be saved itself into a file by implementing the #transferTo method
   * that accepts two arguments: the destination file and optionally a closure (the closure will
   * receive the actual file object into which the content is saved).
   * While saving the content into a file, the content is also automatically classified into some
   * categories so that it can be found more easily.
   * Throws an exception if an unexpected error raises.
   * @param content the content to save.
   * @param fileName the name of the repository file into which the content will be saved.
   */
  def save(content, String fileName) {
    log.info "Save a content into the file ${fileName}"
    content.transferTo(new File(CONTENT_REPOSITORY + fileName)) {
      def attributes = setContentAttributes it
      documentIndexationService.index([file: it, attributes: attributes])
      documentClassificationService.classify([file: it, attributes: attributes])
    }
  }

  /**
   * Finds the contents that are classified with the specified term.
   * @param term a term from the thesaurus.
   * @return the contents classified with the specified term.
   */
  def findByClassification(term) {
    log.info "Find contents classified in the category '${term.label}'"
    return documentClassificationService.getContentsClassifiedIn(term)
  }

  /**
   * Finds the content that match the specified query.
   * Throws an exception if an unexpected error raises.
   * @param query a well formatted query.
   * @return a SearchResult instance embodying the descriptors on the contents that match the query.
   */
  def findByQuery(String query) {
    log.info "Find contents matching the query: ${query}"
    def response = documentIndexationService.search(query)
    return searchResultWith(response.documents, response.clusters)
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

  private def setContentAttributes(File file) {
    def descriptor = ContentStorageDescriptor.findByName(file.name)
    // create a new descriptor if the content is new
    if (!descriptor) {
      log.info "The content is new, references it in the system"
      descriptor = new ContentStorageDescriptor(name: file.name, location: file.absolutePath)
      if (!descriptor.save()) {
        descriptor.errors.each { error ->
          log.error error
        }
        if (file.exists()) {
          file.delete()
        }
        throw new PersistenceException("${file.name} persistence failure!")
      }
    }

    // create new attributes if the content has not yet such data
    def attributes = ContentAttributes.findByContent(descriptor)
    if (!attributes) {
      attributes = new ContentAttributes(title: descriptor.name, content: descriptor)
    }
    def metadata = getMetadata(file)
    if (metadata) {
      log.info "Set the content attributes (metadata)"
      attributes.title = metadata.title
      attributes.description = metadata.description
      attributes.language = metadata.language
      attributes.keywords = metadata.keywords
      if (!attributes.save()) {
        attributes.errors.each { error ->
          log.error error
        }
      }
    }
    return attributes
  }

  /**
   * Parses the specified document to extract the metadata on the content.
   * @param document the document as a file.
   * @return a map with the document metadata: title, description, subject, and language.
   */
  private def getMetadata(File document) {
    final short LinesMaxNumber = 5
    def metadata = [:]
    Reader content = null
    try {
      Tika parser = new Tika()
      Metadata extractedMetadata = new Metadata()
      content = parser.parse(new FileInputStream(document), extractedMetadata)
      metadata.title = extractedMetadata.get(TikaCoreProperties.TITLE)
      metadata.description = extractedMetadata.get(OfficeOpenXMLCore.SUBJECT)
      if (!metadata.description)
        metadata.description = extractedMetadata.get(TikaCoreProperties.DESCRIPTION)
      metadata.language = extractedMetadata.get(TikaCoreProperties.LANGUAGE)
      metadata.keywords = extractedMetadata.get(TikaCoreProperties.KEYWORDS)

      if (!metadata.title)
        metadata.title = FilenameUtils.getBaseName(document.name).replaceAll("[,-_\\.]|(%20)", " ")

      if (!metadata.description || !metadata.language) {
        String snippets = ""
        BufferedReader buffer = new BufferedReader(content)
        String contentLine
        for (short lineCount = 0; lineCount < LinesMaxNumber && (contentLine = buffer.readLine())
            != null; lineCount++) {
          if (contentLine)
            snippets += contentLine + "\n"
          else
            lineCount--
        }
        if (!metadata.description)
          metadata.description = snippets
        if (!metadata.language) {
          LanguageIdentifier identifier = new LanguageIdentifier(snippets)
          metadata.language = identifier.language
        }
      }
    } catch (Exception ex) {
      log.error("${document.name} content parsing failed!", ex)
      metadata.title = FilenameUtils.getBaseName(document.name).replaceAll("[,-_\\.]|(%20)", " ")
    } finally {
      try {
        content?.close()
      } catch (IOException ex) {
        log.warn(ex.getMessage())
      }
    }
    return metadata
  }

  private static def searchResultWith(contents, clusters) {
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
            it.allDocuments.collect { doc ->
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
