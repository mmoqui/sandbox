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
    List<ClassificationFeature> features = extractFeatures(document)
    def selectedClassLabels = lookupClassLabels(features)
    List<ContentCategory> categories = createClassification(document, selectedClassLabels)
    enrichTermSpaceModel(selectedClassLabels, features)
    return categories
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
  private extractFeatures(document) {
    log.info("Extract terms from document ${document.file.name}...")
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
        double weight = Math.log1p(it.value)
        log.debug(" -> Term ${it.key} found with weight ${weight}")
        new ClassificationFeature(term: it.key, weight: weight)
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

  private lookupClassLabels(vector) {
    log.info("Lookup for categories...")
    def classLabelsById = [:]
    // find the class labels matching the features
    vector.each { ClassificationFeature feature ->
      TermSpaceModelItem item = TermSpaceModelItem.findByTerm(feature.term)
      if (item) {
        // weight each taxonomy term
        item.taxonomyTerms.each {
          // weight it with the feature weight
          def classLabel = classLabelsById[it.id]
          if (!classLabel)
            classLabelsById[it.id] = [term: it, weight: feature.weight]
          else
            classLabel.weight += feature.weight
        }
      }
    }
    // now, for features matching exactly taxonomy terms, they are weighty accordingly
    def classLabels = classLabelsById.values()
    classLabels.each { classLabel ->
      log.debug(" -> Category ${classLabel.term.label} detected with weight ${classLabel.weight}")
      def stemmedLabelTokens = stemmedTaxonomyLabel(classLabel.term).collect { new ClassificationFeature(term: it.key, weight: it.value) }
      int pos = vector.indexOf(stemmedLabelTokens[0])
      boolean match = pos > -1
      for (int i = 1; i < stemmedLabelTokens.size(); i++) {
        if (pos+i > vector.size() || (vector[pos + i] != stemmedLabelTokens[i])) {
          match = false
          break;
        }
      }
      if (match)
        classLabel.weight += 100
    }
    // fetch the more weighted class labels
    def firstMorePertinentClassLabel = classLabels.max { it.weight }
    return classLabels.findAll { it.weight == firstMorePertinentClassLabel.weight }
  }

  private contentCategoryMatching(classLabel) {
    ContentCategory category = ContentCategory.findByTerm(classLabel.term)
    if (!category) {
      category = new ContentCategory(term: classLabel.term)
    }
    return category
  }

  private enrichTermSpaceModel(classLabels, features) {
    TermSpaceModel termSpace = TermSpaceModel.get()
    if (!classLabels.empty) {
      features.each { ClassificationFeature feature ->
        if (feature.weight > weightThreshold) {
          TermSpaceModelItem item = TermSpaceModelItem.findByTerm(feature.term)
          if (!item) {
            log.info("Enrich the Term Space Model with the term ${feature.term}")
            item = new TermSpaceModelItem(term: feature.term)
            classLabels.each { item.addToTaxonomyTerms(it.term) }
            termSpace.addToTerms(item)
          }
        }
      }
      if (!termSpace.save(flush: true)) {
        termSpace.errors.each { error ->
          log.error error
        }
      }
    }
  }

  private createClassification(document, classLabels) {
    return classLabels.collect {
      log.info("Category selected: ${it.term.label}")
      def category = contentCategoryMatching(it)
      category.addToContents(document.attributes)
      if (!category.save()) {
        category.errors.each { error ->
          log.error error
        }
      }
      category
    }
  }

  private stemmedTaxonomyLabel(TaxonomyTerm term) {
    TextAnalyzer analyzer = new TextAnalyzer(term.language)
    return analyzer.analyze(new StringReader(term.label), [:])
  }
}
