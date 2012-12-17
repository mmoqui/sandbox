package semantica

import org.apache.tika.Tika
import org.apache.tika.metadata.Metadata

/**
 * Service dedicated to the classification of documents according to some taxonomies (they are
 * gathered into a plan of classification in Semantica).
 */
class DocumentClassificationService {

  /**
   * Bonus to add to the weight of a term when it comes from the content attributes.
   * Any information from metadata need particular attention as they weight explicitly the meaning
   * of the related content.
   */
  private static final int weightBonus = 100

  /**
   * The weightThreshold value above which feature terms with higher weight enrich the Term Space
   * Model.
   */
  private static final double weightThreshold = 1 + Math.log1p(weightBonus)

  /**
   * Classify the specified document according to the plan of classification in Semantica.
   * The document must be an object defined by two classLabel:
   * <ul>
   *   <li>file: the File storing the document,</li>
   *   <li>attributes: the metadata qualifying the document.</li>
   * </ul>
   * @param document the document to classify, defined as indicated above.
   */
  def classify(document) {
    def vector = extractFeatures(document)
    vector.each { println "Term ${it.term} -> ${it.weight}" }
    TermSpaceModel termSpace = TermSpaceModel.get()
    def selectedClassLabels = lookupClassLabelsFor(vector, termSpace)
    /*plan.taxonomies.each { term ->
      selectedClassLabels.addAll(lookupForMatching(vector, term))
    }*/
    createClassification(document, selectedClassLabels)
    //enrichTermSpaceModel(termSpace, selectedClassLabels, vector)
  }

  def getContentsClassifiedIn(term) {
    def classification = ContentCategory.findByTerm(term)
    if (classification)
      return classification.contents.collect() { it.content }
    return []
  }

  /**
   * Extracts from the specified document the text content features. A text content feature is
   * defined by a term that occurs frequently in the text content and by its frequency (tf).
   * @param document an object defined by two classLabel: the file in which is stored the content
   * to categorize and the attributes (metadata) of the content.
   * @return a feature vector. Each vector is defined by a term and by its tf (term frequency) in
   * the content. The terms in the vector are ordered by their frequency in the document from the
   * higher frequent term to the lower one.
   */
  private def extractFeatures(document) {
    Reader reader = null
    try {
      TextAnalyzer analyzer = new TextAnalyzer(document.attributes.language)
      def termCounter = analyzer.analyze(new StringReader(document.attributes.title), [:])
      if (document.attributes.keywords)
        termCounter = analyzer.analyze(new StringReader(document.attributes.keywords),
            termCounter.each { it.value += weightBonus })
      termCounter = analyzer.analyze(new StringReader(document.attributes.description),
          termCounter.each { it.value += weightBonus })
      termCounter = analyzer.analyze(new Tika().parse(new FileInputStream(document.file), new Metadata()),
          termCounter.each { it.value += weightBonus })

      return termCounter.collect {
        new ClassificationFeature(term: it.key, weight: Math.log1p(it.value))
      }.sort { -it.weight }
    } catch (Exception ex) {
      log.error("${document.name} content parsing failed!", ex)
    } finally {
      try {
        reader?.close()
      } catch (IOException ex) {
        log.warn(ex.getMessage())
      }
    }
    return []
  }

  /*private static def lookupForMatching(vector, parentTerm) {
    def classLabel = defineAClassificationLabelFor parentTerm
    println("Features of term ${parentTerm.label} -> ${classLabel.features().collect { it.term } }")
    classLabel.features().each {
      def i = vector.indexOf(it)
      classLabel.weight += (i >= 0 ? vector[i].weight : 0)
    }
    if (classLabel.weight > 0) {
      def matchedLabels = parentTerm.specificTerms.collect { lookupForMatching(vector, it) }
      matchedLabels = matchedLabels.flatten()
      if (!matchedLabels.isEmpty()) {
        def firstMorePertinentCategory = matchedLabels.max { it.weight }
        return matchedLabels.findAll { it.weight == firstMorePertinentCategory.weight }
      }

      return [classLabel]
    }
    return []
  }*/

  private static def lookupClassLabelsFor(vector, termSpace) {
    println("Lookup for categories...")
    def classLabelsById = [:]
    termSpace.terms.each { term ->
      ClassificationFeature feature = vector.find { it.term == term.term }
      if (feature) {
        term.taxonomyTerms.each {
          println("-> Taxonomy term ${it.label} matching!")
          def classLabel = classLabelsById[it.id]
          if (!classLabel)
            classLabelsById[it.id] = [term: it, weight: feature.weight]
          else
            classLabel.weight += feature.weight
        }
      }
    }
    def classLabels = classLabelsById.values()
    classLabels.each { println("Category ${it.term.label} detected with weight ${it.weight}") }
    def firstMorePertinentClassLabel = classLabels.max { it.weight }
    return classLabels.findAll { it.weight == firstMorePertinentClassLabel.weight }
  }

  /*private static def defineAClassificationLabelFor(term) {
    return [term: term,
        weight: 0,
        features: {
          TextAnalyzer analyzer = new TextAnalyzer(term.language)
          def terms = analyzer.analyze(new StringReader(term.subject), [:])
          return terms.collect { new ClassificationFeature(term: it.key, weight: 1) }
        }]
  }*/

  private def contentCategoryMatching(classLabel) {
    ContentCategory category = ContentCategory.findByTerm(classLabel.term)
    if (!category) {
      category = new ContentCategory(term: classLabel.term)
    }
    return category
  }

  private def enrichTermSpaceModel(termSpace, classLabels, vector) {
    if (!classLabels.empty) {
      vector.each { feature ->
        if (feature.weight > weightThreshold) {
          def term = termSpace.terms.find { it.term == feature.term }
          if (term) {
            classLabels.each {
              if (!term.taxonomyTerms.contains(it.term)) {
                println("Link the term ${term.term} in the Term Space Model with the taxonomy term ${it.term.label}")
                term.addToTaxonomyTerms(it.term)
              }
            }
          } else {
            println("Enrich the Term Space Model with the term ${feature.term}")
            term = new TermSpaceModelItem(term: feature.term)
            classLabels.each { term.addToTaxonomyTerms(it.term) }
            termSpace.addToTerms(term)
          }
        }
      }
      if (!termSpace.save()) {
        termSpace.errors.each { error ->
          log.error error
        }
      }
    }
  }

  private createClassification(document, classLabels) {
    classLabels.each {
      println("Class selected: ${it.term.label}")
      def category = contentCategoryMatching(it)
      category.addToContents(document.attributes)
      if (!category.save()) {
        category.errors.each { error ->
          log.error error
        }
      }
    }
  }
}
