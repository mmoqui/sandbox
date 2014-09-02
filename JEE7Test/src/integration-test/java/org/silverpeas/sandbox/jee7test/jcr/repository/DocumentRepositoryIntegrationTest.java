package org.silverpeas.sandbox.jee7test.jcr.repository;

import org.apache.commons.lang.ArrayUtils;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.silverpeas.sandbox.jee7test.jcr.model.Document;

import javax.inject.Inject;
import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.nodetype.NodeType;
import java.io.File;
import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.silverpeas.sandbox.jee7test.jcr.model.DocumentBuilder.aNewDocument;

/**
 * @author mmoquillon
 */
@RunWith(Arquillian.class)
public class DocumentRepositoryIntegrationTest {

  @Inject
  private DocumentRepository repository;

  @Deployment
  public static Archive<?> createTestArchive() {
    File[] coreLibs = Maven.resolver().loadPomFromFile("pom.xml")
        .resolve("org.mockito:mockito-all",
            "org.springframework.social:spring-social-linkedin",
            "org.springframework.social:spring-social-facebook",
            "org.apache.tika:tika-core",
            "org.apache.tika:tika-parsers",
            "org.apache.lucene:lucene-core",
            "commons-io:commons-io",
            "org.slf4j:slf4j-api",
            "concurrent:concurrent",
            "commons-dbcp:commons-dbcp",
            "commons-collections:commons-collections")
        .withTransitivity()
        .asFile();
    File[] jackrabbitLibs = Maven.resolver().loadPomFromFile("pom.xml")
        .resolve("javax.jcr:jcr",
            "org.apache.jackrabbit:jackrabbit-core",
            "org.apache.jackrabbit:jackrabbit-data",
            "org.apache.jackrabbit:jackrabbit-api",
            "org.apache.jackrabbit:jackrabbit-spi",
            "org.apache.jackrabbit:jackrabbit-spi-commons",
            "org.apache.jackrabbit:jackrabbit-jcr-commons")
        .withoutTransitivity()
        .asFile();
    File[] libs = (File[]) ArrayUtils.addAll(coreLibs, jackrabbitLibs);

    return ShrinkWrap.create(WebArchive.class, "test.war")
        .addPackage("org.silverpeas.sandbox.jee7test.security")
        .addPackage("org.silverpeas.sandbox.jee7test.jcr.model")
        .addPackage("org.silverpeas.sandbox.jee7test.jcr.repository")
        .addPackage("org.silverpeas.sandbox.jee7test.jcr.security")
        .addPackages(true, "org.silverpeas.sandbox.jee7test.util")
        .addAsLibraries(libs)
        .addAsResource("META-INF/services/test-org.silverpeas.sandbox.jee7test.util.BeanContainer",
            "META-INF/services/org.silverpeas.sandbox.jee7test.util.BeanContainer")
        .addAsResource("test-jcr.properties", "jcr.properties")
        .addAsResource("test-repository.xml", "repository.xml")
        .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
        .addAsWebInfResource("test-jcr-ds.xml", "test-jcr-ds.xml");
  }

  @After
  public void cleanUpJcrRepository() throws RepositoryException {
    Session session = repository.openSession();
    NodeIterator iterator = session.getRootNode().getNodes();
    while(iterator.hasNext()) {
      Node documentNode = iterator.nextNode();
      if (documentNode.getPrimaryNodeType().isNodeType(NodeType.NT_FOLDER)) {
        documentNode.remove();
      }
    }
    session.save();
    repository.closeSession(session);
  }

  @Test
  public void emptyTest() {

  }

  @Test
  public void noDocumentsShouldBeReturnFromAnEmptyRepository() {
    Set<Document> documents = repository.getAllDocuments();
    assertThat(documents.isEmpty(), is(true));
  }

  @Test
  public void aSavedDocumentCanBeRetrieved() throws Exception {
    Document expectedDocument = aNewDocument("Toto chez les Papoos");

    repository.putDocument(expectedDocument);

    Document actualDocument = repository.getDocumentById(expectedDocument.getId());
    assertThat(actualDocument, is(expectedDocument));
  }

  @Test
  public void aNonEmptyRepositoryShouldReturnAllOfItsDocuments() throws Exception {
    Set<Document> expectedDocuments = new HashSet<>();
    for (int i = 0; i < 3; i++) {
      Document document = aNewDocument("Toto chez les Papoos - Livre " + i);
      repository.putDocument(document);
      expectedDocuments.add(document);
    }

    Set<Document> actualDocuments = repository.getAllDocuments();
    assertThat(actualDocuments.size(), is(expectedDocuments.size()));
    assertThat(actualDocuments.containsAll(expectedDocuments), is(true));
  }

}
