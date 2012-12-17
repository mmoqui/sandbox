package semantica

/**
 * A plan of classification is a set taxonomies, each of them representing a distinct rubric in an
 * organization activity.
 * A taxonomy is an hierarchical organization of controlled vocabulary terms, from the broader to
 * the narrower one. In the plan of classification, the relationships between the terms of a
 * given taxonomy are mono-parental specializations.
 */
class PlanOfClassification {

  static constraints = {
    //taxonomies minSize: 1
  }

  static mapping = {
    taxonomies cascade: 'all'
  }

  static hasMany = [taxonomies: TaxonomyTerm]

  static synchronized get() {
    PlanOfClassification single
    def all = PlanOfClassification.getAll()
    if (all.empty) {
      single = new PlanOfClassification()
    } else {
      single = all.get(0)
    }
    return single
  }
}
