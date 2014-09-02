package org.silverpeas.sandbox.jee7test.jcr.model;

import org.junit.Before;
import org.junit.Test;
import org.silverpeas.sandbox.jee7test.jcr.repository.DocumentRepository;
import org.silverpeas.sandbox.jee7test.util.BeanContainer;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.silverpeas.sandbox.jee7test.jcr.model.DocumentBuilder.aNewDocument;
import static org.silverpeas.sandbox.jee7test.jcr.model.DocumentBuilder.anExistingDocument;
import static org.silverpeas.sandbox.jee7test.test.util.TestBeanContainer.getMockedBeanContainer;

/**
 * @author mmoquillon
 */
public class DocumentTest {

  public static final String DOCUMENT_ID = "document_toto";

  private DocumentRepository repository;

  @Before
  public void prepareCommonBehaviour() throws Exception {
    BeanContainer beanContainer = getMockedBeanContainer();
    repository = mock(DocumentRepository.class);
    when(beanContainer.getBeanByType(DocumentRepository.class)).thenReturn(repository);
  }

  @Test
  public void askForAnExistingDocumentShouldReturnIt() throws Exception {
    Document expectedDocument = anExistingDocument(DOCUMENT_ID, "Chez les Papoos");
    when(repository.getDocumentById(expectedDocument.getId())).thenReturn(expectedDocument);

    Document actualDocument = Document.getById(DOCUMENT_ID);
    assertThat(actualDocument, notNullValue());
    assertThat(actualDocument, is(expectedDocument));
  }

  @Test
  public void askForANonExistingDocumentShouldReturnNull() throws Exception {
    Document actualDocument = Document.getById(DOCUMENT_ID);
    assertThat(actualDocument, nullValue());
  }

  @Test
  public void withSomeDocumentsAskForAllDocumentsShouldReturnThem() throws Exception {
    when(repository.getAllDocuments()).thenReturn(new HashSet<Document>(Arrays
        .asList(anExistingDocument("document_0", "Roman 1"),
            anExistingDocument("document_1", "Roman 2"))));

    Set<Document> documents = Document.getAll();
    assertThat(documents, notNullValue());
    assertThat(documents.size(), is(2));
    assertThat(documents.contains(anExistingDocument("document_0", "Roman 1")), is(true));
    assertThat(documents.contains(anExistingDocument("document_1", "Roman 2")), is(true));
  }

  @Test
  public void savingANewUserPutItIntoTheRepository() throws Exception {
    Document document = aNewDocument("Toto et moi");

    document.save();
    verify(repository).putDocument(document);
  }

}
