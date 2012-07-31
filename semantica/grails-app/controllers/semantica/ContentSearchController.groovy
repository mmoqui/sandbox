package semantica

class ContentSearchController {

  /**
   * Goes to the main content search page.
   */
  def index() {
    render view:'search'
  }
}
