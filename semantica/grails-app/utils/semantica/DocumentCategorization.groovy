package semantica

import org.apache.tika.Tika
import org.apache.tika.metadata.Metadata
import org.apache.commons.logging.LogFactory

/**
 * Service dedicated to the categorization of documents according to some taxonomies (they are
 * represented by a thesaurus in Semantica).
 */
class DocumentCategorization {

  private static final log = LogFactory.getLog(this)

  /**
   * Categorizes the specified document according to the thesaurus.
   * The document must be an object defined by two properties:
   * <ul>
   *   <li>file: the File storing the document,</li>
   *   <li>attributes: the metadata qualifying the document.</li>
   * </ul>
   * @param document the document to categorize, defined as indicated above.
   */
  def categorize(document) {
    def thesauri = Thesaurus.getAll()
    def vector = extractFeatures(document)
    def selectedCategories = []
    vector.each { println "Term ${it.term} -> ${it.weight}"}
    thesauri.each { Thesaurus thesaurus ->
      thesaurus.vocabulary.each { term ->
        selectedCategories.addAll(lookupForMatching(vector, term))
      }
    }
    selectedCategories.each { println("Category selected: ${it.label}") }
  }

  /**
   * Extracts from the specified document the text content features. A text content feature is
   * defined by a term that occurs frequently in the text content and by its frequency (tf).
   * @param document an object defined by two properties: the file in which is stored the content
   * to categorize and the attributes (metadata) of the content.
   * @return a feature vector. Each vector is defined by a term and by its tf (term frequency) in
   * the content. The terms in the vector are ordered by their frequency in the document from the
   * higher frequent term to the lower one.
   */
  private def extractFeatures(document) {
    Reader reader
    try {
      TextAnalyzer analyzer = new TextAnalyzer(document.attributes.language)
      def termCounter = analyzer.analyze(new StringReader(document.attributes.title), [:])
      termCounter = analyzer.analyze(new StringReader(document.attributes.description),
          termCounter.each { it.value += 100 })
      termCounter = analyzer.analyze(new Tika().parse(new FileInputStream(document.file), new Metadata()),
          termCounter.each { it.value += 100 })

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
  }

  private def lookupForMatching(vector, parentTerm) {
    println("Features of term ${parentTerm.label} -> ${parentTerm.features.collect { it.term} }")
    boolean match = parentTerm.features.inject(false) { acc, it -> acc |= vector.contains(it) }
    if (match) {
      def matchingTerms = parentTerm.specificTerms.findAll {
        def matching = lookupForMatching(vector, it)
        !matching.isEmpty()
      }
      if (matchingTerms.isEmpty())
        matchingTerms = [parentTerm]
      return matchingTerms
    }
    return []
  }
}
