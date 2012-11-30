package semantica

/**
 * A thesaurus is a list of semantically orthogonal topical terms.
 * In this thesaurus, the vocabulary defines the terms that are linked to others by hierarchical
 * relationships (semantic refinement) and that can be enriched by some equivalency relationships
 * (synonymy).
 */
class Thesaurus {

  static constraints = {
    vocabulary minSize: 1
  }

  static mapping = {
    vocabulary cascade: 'all'
  }

  static hasMany = [vocabulary: ThesaurusTerm]
}
