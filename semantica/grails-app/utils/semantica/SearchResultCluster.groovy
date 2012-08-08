package semantica

/**
 * A cluster gathers the search results that share a common hidden property.
 * A cluster is defined by the property label and by the contents that satisfies that property.
 */
class SearchResultCluster {

  /**
   * The label of a hidden structural property that share several contents sent back by a search
   * request.
   */
  String label

  /**
   * The contents that share the property identified by this cluster.
   */
  def contents = []
}
