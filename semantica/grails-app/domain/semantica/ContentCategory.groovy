package semantica

/**
 * Represents the class (the category in a classification) of a content according to a
 * plan of classification.
 */
class ContentCategory {

  static constraints = {
    term nullable: false
    contents nullable: false, minSize: 1
  }

  static belongsTo = [term: TaxonomyTerm]

  static hasMany = [contents: ContentAttributes]

}
