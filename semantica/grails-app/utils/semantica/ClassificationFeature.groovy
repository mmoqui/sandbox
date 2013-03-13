package semantica

/**
 * A classification feature is the input in the classification of a text according to a label (aka
 * category). A such feature is defined by a term (that occurs in the content to classify) and a
 * weight measuring the importance of the term in the text or in a class of texts.
 */
class ClassificationFeature {

  /**
   * The term identifying the feature.
   */
  String term

  /**
   * Weight of the term in a text (in this case, the weight is the term frequency, aka TF, of the
   * term in a given text) or in a class of texts.
   */
  float weight

  @Override
  boolean equals(Object o) {
    if (this.is(o)) return true
    if (o.hasProperty("term"))
        this.term == o.term
    if (o.respondsTo("term"))
        return this.term == o.term()
    return false
  }

  @Override
  int hashCode() {
    return term.hashCode()
  }
}
