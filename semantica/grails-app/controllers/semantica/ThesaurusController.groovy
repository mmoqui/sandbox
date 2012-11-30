package semantica

import grails.converters.JSON

class ThesaurusController {

  def thesaurusService

  /**
   * Goes to the thesaurus page.
   */
  def index() {
    render view: 'thesaurus'
  }

  /**
   * Uploads a thesaurus from a CSV file into Semantica and sends back to the thesaurus page.
   * The CSV file must be comma-separated.
   */
  def upload() {
    def multipartFile = request.getFile('fileToUpload')
    def language = params.language // ISO 639-1 code of the language
    String fileName = multipartFile.originalFilename
    if (multipartFile.empty) {
      flash.message = 'File cannot be empty'
    } else if (!fileName.toLowerCase().endsWith('.csv')) {
      flash.message = 'The multipartFile must be a CSV one'
    } else {
      int suffixIndex = fileName.lastIndexOf(".")
      def csvFile = File.createTempFile(fileName.substring(0, suffixIndex), fileName.substring(suffixIndex))
      multipartFile.transferTo(csvFile)
      if (thesaurusService.loadFrom(csvFile, language)) {
        csvFile.delete()
        flash.message = "File \'${fileName}\' uploaded!"
      } else {
        flash.error = "Failed to upload the thesaurus from \'${fileName}\'"
      }
    }
    redirect action: 'index'
  }

  /**
   * Gets the terms in the thesaurus that construct the vocabulary in use.
   * Such terms are the roots one in the thesaurus, that is to say those defining the more generic
   * concept in use in Semantica; they each define a taxonomy tree.
   * @return a JSON representation of the terms that compound the vocabulary.
   */
  def vocabulary() {
    //def terms = ThesaurusTerm.findAll("from ThesaurusTerm where generalTerm is null")
    def terms = Thesaurus.get(1)?.vocabulary
    if (!terms) {
      terms = []
    }
    render terms as JSON
  }

  /**
   * Gets either all the thesaurus terms defined in the Semantica thesaurus or the one identified by
   * the specified unique identifier.
   * @param id the unique identifier of the term to get or null for getting all the thesaurus terms.
   * @return a JSON representation of the asked term or of the array containing all the thesaurus terms.
   */
  def terms(int id) {
    if (id) {
      ThesaurusTerm term = ThesaurusTerm.get(id)
      if (term == null) {
        response.sendError(404)
      }
      render term as JSON
    } else {
      def allTerms = ThesaurusTerm.findAll()
      render allTerms as JSON
    }
  }
}
