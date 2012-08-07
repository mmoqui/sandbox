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
   * The content should know how to be saved itself into a file by implementing a #transferTo method
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
      def descriptor = ContentStorageDescriptor.findByName(it.name)
      if (!descriptor) {
        log.info "The content is new, references it in the system"
        descriptor = new ContentStorageDescriptor(name: it.name, location: it.absolutePath)
        if (!descriptor.save(flush: true)) {
          descriptor.errors.each {
            log.error it
          }
          File savedContent = new File(CONTENT_REPOSITORY + fileName)
          if (savedContent.exists()) {
            savedContent.delete()
          }
          throw new PersistenceException("${it.name} persistence failure!")
        }
      }
      contentClassificationService.classify(it, descriptor)
    }
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
    contentClassificationService.search(query).contents.each {
      def contentDescriptor = ContentStorageDescriptor.get(it.id)
      if (contentDescriptor) {
        descriptors << contentDescriptor
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


}
