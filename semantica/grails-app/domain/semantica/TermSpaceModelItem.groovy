package semantica

/**
 * An item in the model of the space of terms. It represents a term in the space modelled by the
 * TermSpaceModel class.
 *
 * The item represented here defines, with the term itself, the links with the taxonomy nodes
 * where it appears either as a label or in the subject.
 */
class TermSpaceModelItem {

  static constraints = {
    term nullable: false, blank: false
    taxonomyTerms minSize: 1
  }

  static mapping = {
    taxonomyTerms cascade: "merge,refresh"
  }

  /**
   * A term in the space.
   */
  String term

  /**
   * The different taxonomy terms in which this term appears either as the label or in the subject.
   */
  static hasMany = [taxonomyTerms: TaxonomyTerm]

  static belongsTo = [TermSpaceModel]
}
