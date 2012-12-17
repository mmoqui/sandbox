package semantica

class ContentAttributes {

  static constraints = {
    title blank: false
    description blank: true, nullable: true
    language blank: false, nullable: true
    keywords blank: true, nullable: true
  }

  static mapping = {
    description type: "text"
  }

  /**
   * The title of the content.
   */
  String title
  /**
   * A short description of the content.
   */
  String description

  /**
   * Some keywords qualifying the content.
   */
  String keywords = ""

  /**
   * Language in which is written the content. Must be an ISO 639 code.
   */
  String language

  /**
   * The content to which this metadata belongs. The content is here described by its storage
   * descriptor.
   */
  static belongsTo = [content: ContentStorageDescriptor]

}
