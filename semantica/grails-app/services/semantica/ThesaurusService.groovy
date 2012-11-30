package semantica

class ThesaurusService {

  /**
   * Loads a thesaurus from the specified CSV file.
   * The any already existing thesaurus is overwritten by the new one.
   * @param csvFile the CSV file containing the thesaurus to load.
   * @param the ISO 639-1 code of the language in which is written the thesaurus in the CSV file.
   */
  def loadFrom(File csvFile, String language) {
    Thesaurus thesaurus;
    def thesaurusList = Thesaurus.findAll();
    if (thesaurusList.empty) {
      thesaurus = new Thesaurus()
    } else {
      thesaurus = thesaurusList.get(0)
    }
    def stackOfCurrentTerms = []
    csvFile.eachCsvLine { tokens ->
      int i = -1
      while(! tokens[++i] && i < tokens.length) { continue }
      if (i < tokens.length) {
        stackOfCurrentTerms[i] = new ThesaurusTerm(label: tokens[i],
            keywords: keywordsFromToken(tokens[i+1]), language: language)
        if (i > 0) {
          stackOfCurrentTerms[i].generalTerm = stackOfCurrentTerms[i - 1]
          stackOfCurrentTerms[i - 1].addToSpecificTerms(stackOfCurrentTerms[i])
          //stackOfCurrentTerms[i - 1].save()
        } else {
          //stackOfCurrentTerms[0].save()
          thesaurus.addToVocabulary(stackOfCurrentTerms[0])
        }
      }
    }
    if (!thesaurus.save()) {
      thesaurus.errors.each { println it }
      return false
    }
    return true
  }

  private def keywordsFromToken(String token) {
    if (token.startsWith("clues:")) {
      return token.split(":")[1]
    }
    return token
  }
}
