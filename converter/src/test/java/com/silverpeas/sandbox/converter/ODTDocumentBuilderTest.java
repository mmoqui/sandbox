/*
 *  Copyright (C) 2000 - 2011 Silverpeas
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Affero General Public License as
 *  published by the Free Software Foundation, either version 3 of the
 *  License, or (at your option) any later version.
 *
 *  As a special exception to the terms and conditions of version 3.0 of
 *  the GPL, you may redistribute this Program in connection with Free/Libre
 *  Open Source Software ("FLOSS") applications as described in Silverpeas's
 *  FLOSS exception.  You should have recieved a copy of the text describing
 *  the FLOSS exception, and it is also available here:
 *  "http://www.silverpeas.org/legal/licensing"
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Affero General Public License for more details.
 *
 *  You should have received a copy of the GNU Affero General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.silverpeas.sandbox.converter;

import java.io.File;
import java.io.InputStream;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

/**
 * Tests on the build of an ODT document.
 */
public class ODTDocumentBuilderTest {

  public static final String PUBLICATION = "publication.xml";

  public ODTDocumentBuilderTest() {
  }

  @BeforeClass
  public static void setUpClass() throws Exception {
  }

  @AfterClass
  public static void tearDownClass() throws Exception {
  }

  @Before
  public void setUp() {
  }

  @After
  public void tearDown() {
  }

  @Test
  public void buildAnODTDocumentFromAPublication() throws Exception {
    Publication publication = aPublication();
    ODTDocumentBuilder builder = ODTDocumentBuilder.getBuilder();
    File document = builder.buildFromPublication(publication);
    assertThat(document.exists(), is(true));
    
    ODTConverter converter = ODTConverter.odtConverterTo(ConversionFormat.pdf);
    File pdf = converter.convert(document);
    assertThat(pdf.exists(), is(true));
    
    converter = ODTConverter.odtConverterTo(ConversionFormat.doc);
    File doc = converter.convert(document);
    assertThat(doc.exists(), is(true));
  }

  private static Publication aPublication() {
    Publication publication = null;
    try {
      JAXBContext jaxbContext = JAXBContext.newInstance(Publication.class);
      InputStream input = ODTDocumentBuilderTest.class.getResourceAsStream("/" + PUBLICATION);
      Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
      publication = (Publication) unmarshaller.unmarshal(input);
      assertThat(publication, notNullValue());
    } catch (JAXBException ex) {
      fail(ex.getMessage());
    }
    return publication;
  }
}
