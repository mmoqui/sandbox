import semantica.DocumentIndexation
import semantica.DocumentCategorization

// Place your Spring DSL code here
beans = {
  indexation(DocumentIndexation) { bean ->
    grailsApplication = ref('grailsApplication')
    bean.singleton = false
  }

  categorization(DocumentCategorization) { bean ->
      bean.singleton = false
    }
}
