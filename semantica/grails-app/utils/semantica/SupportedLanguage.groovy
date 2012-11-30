package semantica

/**
 * A language that is supported in Semantica.
 */
class SupportedLanguage {

  /**
   * ISO 639-1 code of the language.
   */
  String code

  /**
   * label of the language (for example, French for the code fr).
   */
  String label

  static List<SupportedLanguage> languages() {
    return [ new SupportedLanguage(code: "en", label: "English"),
        new SupportedLanguage(code: "fr", label: "French") ]
  }


}
