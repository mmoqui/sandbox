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

import java.util.Set;
import java.io.File;
import java.net.URL;
import java.util.Map.Entry;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;
import static com.silverpeas.sandbox.converter.ODTConverter.*;

/**
 * Test the conversion of documents with an OpenOffice server.
 */
public class ODTConverterTest {

  public static final String DOCUMENT_NAME = "API_REST_Silverpeas.odt";

  private File document;

  public ODTConverterTest() {
  }

  @BeforeClass
  public static void setUpClass() throws Exception {
  }

  @AfterClass
  public static void tearDownClass() throws Exception {
  }

  @Before
  public void setUp() throws Exception {
    URL documentLocation = getClass().getResource("/" + DOCUMENT_NAME);
    document = new File(documentLocation.toURI());
    assertThat(document.exists(), is(true));
  }

  @After
  public void tearDown() {
  }

  @Test
  public void convertAnODTDocumentToPDF() throws Exception {
    ODTConverter converter = odtConverterTo(ConversionFormat.pdf);
    File convertedDocument = converter.convert(document);
    assertThat(convertedDocument.exists(), is(true));
  }

  @Test
  public void convertAnODTDocumentToDoc() throws Exception {
    ODTConverter converter = odtConverterTo(ConversionFormat.doc);
    File convertedDocument = converter.convert(document);
    assertThat(convertedDocument.exists(), is(true));
  }

  @Test
  public void convertAnODTDocumentToRTF() throws Exception {
    ODTConverter converter = odtConverterTo(ConversionFormat.rtf);
    File convertedDocument = converter.convert(document);
    assertThat(convertedDocument.exists(), is(true));
  }

  @Test
  public void convertAnHTMLDocumentToODT() throws Exception {
    URL documentLocation = getClass().getResource("/wysiwyg2.html");
    File odt = new File(documentLocation.toURI());
    ODTConverter converter = odtConverterTo(ConversionFormat.odt);
    File convertedDocument = converter.convert(odt);
    assertThat(convertedDocument.exists(), is(true));
  }

}
