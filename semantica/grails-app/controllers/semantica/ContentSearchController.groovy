package semantica

import grails.converters.JSON

class ContentSearchController {

  def contentService

  /**
   * Goes to the main content search page.
   */
  def index() {
    render view: 'search'
  }

  /**
   * Searches the contents that match the query specified in the incoming request.
   * @param query the text of the query.
   */
  def search(String query) {
    def contents
    if (!query.trim().isEmpty()) {
      contents = contentService.findByQuery(query)
    } else {
      contents = new SearchResult()
    }
    render contents as JSON
  }

  /**
   * Gets the contents classified in the specified category of the thesaurus.
   * @param id the unique identifier of the category in the thesaurus.
   */
  def category(long id) {
    def contents
    def term = TaxonomyTerm.get(id)
    if (term)
      contents = contentService.findByClassification(term)
    else
      contents = []
    render contents as JSON
  }

  /**
   * Gets the content identified by the specified unique identifier.
   * @param id the content unique identifier
   */
  def content(long id) {
    def content = contentService.findById(id)
    if (content) {
      def file = new File(content.location)
      response.setContentType("application/octet-stream")
      response.setHeader("Content-disposition", "filename=${URLEncoder.encode(file.name, 'UTF-8')}")
      response.outputStream << file.bytes
    } else {
      response.sendError(404)
    }
  }
}
