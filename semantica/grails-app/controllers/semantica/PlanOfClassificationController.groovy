package semantica

import grails.converters.JSON

class PlanOfClassificationController {

  def planOfClassificationService

  /**
   * Goes to the main plan of classification page.
   */
  def index() {
    render view: 'planOfClassification'
  }

  /**
   * Uploads a taxonomy from a CSV file and sends back to the main page.
   * The uploaded taxonomy will be added into the plan of classification in Semantica.
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
      if (planOfClassificationService.loadFrom(csvFile, language)) {
        csvFile.delete()
        flash.message = "File \'${fileName}\' uploaded!"
      } else {
        flash.error = "Failed to upload the thesaurus from \'${fileName}\'"
      }
    }
    redirect action: 'index'
  }

  /**
   * Gets the taxonomies belonged to the plan of classification, each of them representing a distinct
   * subject in the plan.
   *
   * The taxonomies are identified uniquely by their root term that name the subject they represent.
   * @return a JSON representation of the root terms of each taxonomy..
   */
  def taxonomies() {
    //def terms = ThesaurusTerm.findAll("from ThesaurusTerm where generalTerm is null")
    def terms = PlanOfClassification.get().taxonomies
    if (!terms) {
      terms = []
    }
    render terms as JSON
  }

  /**
   * Gets either all the taxonomy terms that compound the plan of classification, whatever the taxonomy
   * to which they belong or the one identified by the specified unique identifier.
   * @param id the unique identifier of the term to get or null for getting all the terms in the
   * plan of classification.
   * @return a JSON representation of the asked term or of the array containing all the terms.
   */
  def terms(int id) {
    if (id) {
      TaxonomyTerm term = TaxonomyTerm.get(id)
      if (term == null) {
        response.sendError(404)
      }
      render term as JSON
    } else {
      def allTerms = TaxonomyTerm.findAll()
      render allTerms as JSON
    }
  }
}
