package semantica

/**
 * Descriptor of a content storage, whatever the content is (an office document, an image, ...).
 * The descriptor, as its name implies, describes the storage of a content: its name, its location
 * in the filesystem, and so on.
 */
class ContentStorageDescriptor {

  static constraints = {
    name unique: true
    location blank: false
  }
  
  String name
  String location
}
