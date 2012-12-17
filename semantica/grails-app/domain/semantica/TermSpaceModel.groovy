package semantica

/**
 * Data model for representing the space of all the terms conveyed by the taxonomies in Semantica.
 *
 * The space represented here gathers all the terms from which the vector describing a document
 * content is built. Only the terms defined in the space are taken into account when analysing the
 * content; all the terms extracted from a content and not defined in the space is set aside.
 *
 * This model here is structured as an inverted table of terms: each terms in the space refers the
 * taxonomy nodes where it appears either as a label or in the subject.
 */
class TermSpaceModel {

  static constraints = {
  }

  static mapping = {
    terms cascade: 'all'
  }

  /**
   * A collection of space terms.
   */
  static hasMany = [terms: TermSpaceModelItem]

  static synchronized get() {
    TermSpaceModel single
    def all = TermSpaceModel.getAll()
    if (all.empty) {
      single = new TermSpaceModel()
    } else {
      single = all.get(0)
    }
    return  single
  }
}
