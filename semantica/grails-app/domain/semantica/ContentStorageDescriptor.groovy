package semantica

/**
 * Descriptor of a content storage, whatever the content is (an office document, an image, ...).
 * The descriptor, as its name implies, describes the storage of a content: its name, its location
 * in the filesystem..
 */
class ContentStorageDescriptor {

  static constraints = {
    name unique: true
    location blank: false
  }

  /**
   * The name of the file in which is stored the document. This name identifies uniquely the content.
   */
  String name
  /**
   * The location of the file in the filesystem (the absolute path of the file).
   */
  String location
}
