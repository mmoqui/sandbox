package semantica

class PlanOfClassificationService {

  /**
   * Loads a thesaurus from the specified CSV file.
   * The any already existing thesaurus is overwritten by the new one.
   * @param csvFile the CSV file containing the thesaurus to load.
   * @param the ISO 639-1 code of the language in which is written the thesaurus in the CSV file.
   */
  def loadFrom(File csvFile, String language) {
    PlanOfClassification planOfClassification = PlanOfClassification.get()
    TermSpaceModel termSpace = TermSpaceModel.get()
    def stackOfCurrentTerms = []
    csvFile.eachCsvLine { tokens ->
      int i = -1
      while(! tokens[++i] && i < tokens.length) { continue }
      if (i < tokens.length) {
        stackOfCurrentTerms[i] = new TaxonomyTerm(label: tokens[i],
            subject: keywordsFromToken(tokens[i+1]), language: language)
        termSpaceModelItemFrom(stackOfCurrentTerms[i]).each { termSpace.addToTerms(it) }
        if (i > 0) {
          stackOfCurrentTerms[i].generalTerm = stackOfCurrentTerms[i - 1]
          stackOfCurrentTerms[i - 1].addToSpecificTerms(stackOfCurrentTerms[i])
          //stackOfCurrentTerms[i - 1].save()
        } else {
          //stackOfCurrentTerms[0].save()
          planOfClassification.addToTaxonomies(stackOfCurrentTerms[0])
        }
      }
    }
    if (!planOfClassification.save()) {
      planOfClassification.errors.each { println it }
      return false
    }
    if (!termSpace.save()) {
      termSpace.errors.each { println it }
      return false
    }
    return true
  }

  private static def keywordsFromToken(String token) {
    if (token.startsWith("clues:")) {
      return token.split(":")[1]
    }
    return token
  }

  private static def termSpaceModelItemFrom(TaxonomyTerm taxonomyTerm) {
    TextAnalyzer analyzer = new TextAnalyzer(taxonomyTerm.language)
    def terms = analyzer.analyze(
        new StringReader(taxonomyTerm.subject),
        analyzer.analyze(new StringReader(taxonomyTerm.label), [:]))
    return terms.collect {
      TermSpaceModelItem item = TermSpaceModelItem.findByTerm(it.key)
      if (!item) {
        item = new TermSpaceModelItem(term: it.key)
      }
      item.addToTaxonomyTerms(taxonomyTerm)
      item
    }
  }
}
