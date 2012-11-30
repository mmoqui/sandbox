package semantica

/**
 * A term in a thesaurus. The terms and their relationships (whatever they are) defines the
 * vocabulary of a thesaurus and they define generally the vocabulary of a given domain.
 *
 * In Semantica, a thesaurus term is described by its label (a noun) and by its hierarchical
 * relationship with the other terms (if any).
 */
class ThesaurusTerm {

  static constraints = {
    label blank: false, nullable: false
    keywords blank: true, nullable: false
    language blank: false, nullable: false
  }

  static mapping = {
    specificTerms cascade: 'all'
  }

  String label

  /**
   * Each keyword is separated by a semi-colon.
   */
  String keywords

  /**
   * Language in which are written the label and the keywords.
   */
  String language

  /**
   * The more general term with which this term is related to.
   */
  ThesaurusTerm generalTerm = null

  /**
   * The direct more specific terms of this one.
   */
  static hasMany = [specificTerms: ThesaurusTerm]

  transient List<ClassificationFeature> _features

  /**
   * Is this term more general one? It is more general if it doesn't exist any hierarchical
   * relationships in which it a more specific term.
   * @return true if has no parent in any hierarchical relationships, false otherwise.
   */
  boolean isMoreGeneral() {
    return generalTerm == null
  }

  /**
   * Gets the features from the keywords. Each feature is defined by the stemmed term of a keyword
   * and by its weight in the contents categorized with this thesaurus term.
   */
  List<ClassificationFeature> getFeatures() {
    if (!_features) {
      TextAnalyzer analyzer = new TextAnalyzer(this.language)
      def terms =  analyzer.analyze(new StringReader(this.keywords), [:])
      _features = terms.collect { new ClassificationFeature(term: it.key, weight: 1 ) }
    }
    return _features
  }
}
