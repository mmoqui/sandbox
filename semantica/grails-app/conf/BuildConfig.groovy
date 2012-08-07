grails.servlet.version = "3.0" // Change depending on target container compliance (2.5 or 3.0)
grails.project.class.dir = "target/classes"
grails.project.test.class.dir = "target/test-classes"
grails.project.test.reports.dir = "target/test-reports"
grails.project.target.level = 1.6
grails.project.source.level = 1.6
//grails.project.war.file = "target/${appName}-${appVersion}.war"

grails.project.dependency.resolution = {
  // inherit Grails' default dependencies
  inherits("global") {
    // specify dependency exclusions here; for example, uncomment this to disable ehcache:
    // excludes 'ehcache'
  }
  log "error" // log level of Ivy resolver, either 'error', 'warn', 'info', 'debug' or 'verbose'
  checksums true // Whether to verify checksums on resolve

  repositories {
    inherits true // Whether to inherit repository definitions from plugins

    grailsPlugins()
    grailsHome()
    grailsCentral()

    mavenLocal()
    mavenCentral()

    // uncomment these (or add new ones) to enable remote dependency resolution from public Maven repositories
    //mavenRepo "http://snapshots.repository.codehaus.org"
    //mavenRepo "http://repository.codehaus.org"
    //mavenRepo "http://download.java.net/maven/2/"
    //mavenRepo "http://repository.jboss.com/maven2/"
  }
  dependencies {
    // Jersey dependency to communicate with the REST-like WEB API
    compile 'com.sun.jersey:jersey-client:1.13'
    compile 'com.sun.jersey:jersey-core:1.13'
    compile 'com.sun.jersey.contribs:jersey-multipart:1.13'
    // Tika dependency to parse text document
    compile 'org.apache.tika:tika-parsers:1.2'
    // Carrot² dependency to cluster search results
    compile 'org.carrot2:carrot2-core:3.6.0'
  }

  plugins {
    runtime ":hibernate:$grailsVersion"
    runtime ":jquery:1.7.2"
    runtime ":resources:1.1.6"

    // Uncomment these (or add new ones) to enable additional resources capabilities
    //runtime ":zipped-resources:1.0"
    //runtime ":cached-resources:1.0"
    //runtime ":yui-minify-resources:0.1.4"

    build ":tomcat:$grailsVersion"

    runtime ":database-migration:1.1"

    compile ':cache:1.0.0'
  }
}
