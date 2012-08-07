package semantica

/**
 * Result of a content search.
 * It is defined by two attributes: the contents that match the search query and optionally the
 * clusters into which are clustered the contents. The contents within a given cluster can be
 * get by calling the Cluster#allDocuments method
 */
class SearchResult {

  /**
   * The descriptors on the contents that match the query. They refer the content itself plus
   * additional information about them like its unique identifier in the system (#id), its name
   * (#name), its description (#description), its relevance score (#score) and so on.
   */
  def contents = []

  /**
   * The clusters into which are categorized the contents above. Each cluster is defined by a label
   * (#label), by relevance score (#score), and by its contents (#allDocuments).
   */
  def clusters = []
}
