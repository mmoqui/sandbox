package semantica

import org.simpleframework.xml.core.PersistenceException

class ContentService {

  static final String CONTENT_REPOSITORY = System.getenv("HOME") + "/tmp/semantica/"

  static {
    File repository = new File(CONTENT_REPOSITORY)
    if (!repository.exists()) {
      repository.mkdirs()
    }
  }

  def contentClassificationService

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
      def descriptor = descriptorForContentIn it
      contentClassificationService.classify(descriptor)
    }
  }

  /**
   * Finds the content that match the specified query.
   * Throws an exception if an unexpected error raises.
   * @param query a well formatted query.
   * @return a SearchResult instance embodying the descriptors on the contents that match the query.
   */
  def findByQuery(String query) {
    log.info "Find contents matching the query: ${query}"
    return contentClassificationService.search(query)
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

  private def descriptorForContentIn(File file) {
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
      if (!attributes.save()) {
        attributes.errors.each { error ->
          log.error error
        }
      }
    }
    return descriptor
  }

}
