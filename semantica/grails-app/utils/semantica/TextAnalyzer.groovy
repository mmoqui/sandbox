package semantica

import org.apache.lucene.analysis.Analyzer
import org.apache.lucene.analysis.en.EnglishAnalyzer
import org.apache.lucene.analysis.fr.FrenchAnalyzer
import org.apache.lucene.analysis.de.GermanAnalyzer
import org.apache.lucene.analysis.es.SpanishAnalyzer
import org.apache.lucene.analysis.pt.PortugueseAnalyzer
import org.apache.lucene.analysis.it.ItalianAnalyzer
import org.apache.lucene.analysis.standard.StandardAnalyzer
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute
import org.apache.lucene.analysis.TokenStream
import org.apache.lucene.util.Version

/**
 * The text analyzer parses the content from a reader to extract from it stemmed words with for each
 * their count in the content.
 */
class TextAnalyzer {

  /**
   * The version of Lucene used in the classification..
   */
  private static final LUCENE_VERSION = Version.LUCENE_36

  private def Analyzer analyzer

  /**
   * Constructs a new text analyzer for the specified language.
   * @param language the ISO 639-1 code of the language.
   */
  TextAnalyzer(String language) {
    this.analyzer = newAnalyzerFor(language)
  }

  /**
   * Analyzes the text by using the specified reader and enriches the specified map of terms with
   * the ones extracted from the text. The terms in the map must be stemmed as the extracted ones
   * will be.
   * @param reader a reader with which the text will be accessed.
   * @param terms a map of stemmed terms-count of identified terms.
   * @return the map enriched with all of the analyzed terms and their count updated.
   */
  def analyze(final Reader reader, terms = [:]) {
    TokenStream tokenStream = analyzer.reusableTokenStream("text", reader)
    tokenStream.addAttribute(CharTermAttribute.class)
    tokenStream.reset()
    for (; tokenStream.incrementToken();) {
      String term = tokenStream.getAttribute(CharTermAttribute.class)
      terms[term] = (terms.containsKey(term) ? terms[term] + 1 : 1)
    }
    return terms
  }

  private static Analyzer newAnalyzerFor(iso639LanguageCode) {
    Analyzer analyzer
    if (iso639LanguageCode == 'fr')
      analyzer = new FrenchAnalyzer(LUCENE_VERSION)
    else if (iso639LanguageCode == 'en')
      analyzer = new EnglishAnalyzer(LUCENE_VERSION)
    else if (iso639LanguageCode == 'de')
      analyzer = new GermanAnalyzer(LUCENE_VERSION)
    else if (iso639LanguageCode == 'es')
      analyzer = new SpanishAnalyzer(LUCENE_VERSION)
    else if (iso639LanguageCode == 'pt')
      analyzer = new PortugueseAnalyzer(LUCENE_VERSION)
    else if (iso639LanguageCode == 'it')
      analyzer = new ItalianAnalyzer(LUCENE_VERSION)
    else
      analyzer = new StandardAnalyzer(LUCENE_VERSION)
    return analyzer
  }
}
