package semantica

/**
 * A term in a taxonomy whose the relationships are mono-parental specializations.
 *
 * The term is then either more general one in the taxonomy (it has then no parent) or it conveys a
 * narrower meaning than its parent..
 * The term can have zero, one or more children; it this case, it conveys a broader meaning about a
 * given subject.
 *
 * In Semantica, a taxonomy term is described by its label (a noun), a subject (a string of subject)
 * carrying its meaning, the language in which it is expressed, and by its hierarchical relationship
 * with the other terms (if any).
 */
class TaxonomyTerm {

  static constraints = {
    label blank: false, nullable: false
    subject blank: true, nullable: false
    language blank: false, nullable: false
  }

  static mapping = {
    specificTerms cascade: 'all'
  }

  String label

  /**
   * The subject of its term. A subject is a set of keywords or key phrases that defines its meaning.
   * Each keyword or keyphrase is separated by a semi-colon.
   */
  String subject

  /**
   * Language in which are written the label and the subject.
   */
  String language

  /**
   * The more general term with which this term is related to.
   */
  TaxonomyTerm generalTerm = null

  /**
   * The direct more specific terms of this one.
   */
  static hasMany = [specificTerms: TaxonomyTerm]

  /**
   * Is this term more general one? It is more general if it doesn't exist any hierarchical
   * relationships in which it a more specific term.
   * @return true if has no parent in any hierarchical relationships, false otherwise.
   */
  boolean isMoreGeneral() {
    return generalTerm == null
  }

}
