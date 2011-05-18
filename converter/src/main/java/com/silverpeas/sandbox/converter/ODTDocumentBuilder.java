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

import java.util.Iterator;
import org.odftoolkit.odfdom.incubator.doc.style.OdfStyle;
import org.odftoolkit.simple.Document;
import org.odftoolkit.simple.text.list.ListItem;
import org.odftoolkit.simple.table.Column;
import java.io.File;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.Date;
import javax.swing.text.StyledEditorKit.ItalicAction;
import org.odftoolkit.odfdom.dom.element.number.NumberTextStyleElement;
import org.odftoolkit.odfdom.dom.element.style.StyleMasterPageElement;
import org.odftoolkit.odfdom.dom.element.text.TextAElement;
import org.odftoolkit.odfdom.dom.element.text.TextLineBreakElement;
import org.odftoolkit.odfdom.dom.style.OdfStyleFamily;
import org.odftoolkit.odfdom.incubator.doc.office.OdfOfficeAutomaticStyles;
import org.odftoolkit.odfdom.incubator.doc.office.OdfOfficeMasterStyles;
import org.odftoolkit.odfdom.incubator.doc.office.OdfOfficeStyles;
import org.odftoolkit.simple.TextDocument;
import org.odftoolkit.simple.meta.Meta;
import org.odftoolkit.simple.table.Row;
import org.odftoolkit.simple.table.Table;
import org.odftoolkit.simple.text.Paragraph;
import org.odftoolkit.simple.text.Section;
import org.odftoolkit.simple.text.list.BulletDecorator;
import org.odftoolkit.simple.text.list.List;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import static com.silverpeas.sandbox.converter.ODTConverter.*;

/**
 * A builder of document in the ODT format.
 */
public class ODTDocumentBuilder {

  private static ODTDocumentBuilder instance = new ODTDocumentBuilder();
  private static final String DOCUMENT_TEMPLATE = "template-export.odt";
  private static final String WYSIWYG_CONTENT = "wysiwyg1.html";
  private static final String WYSIWYG_STYLES_PREFIX = "WYSIWYG";

  public static ODTDocumentBuilder getBuilder() {
    return instance;
  }

  public File buildFromPublication(final Publication publication) {
    try {
      String documentName = URLEncoder.encode(publication.getName().replaceAll(" ", "_"), "UTF-8")
          + ".odt";
      TextDocument odtDocument = TextDocument.loadDocument(getClass().getResourceAsStream(
          "/" + DOCUMENT_TEMPLATE));
      fill(odtDocument, with(publication));
      File odtFile = new File(documentName);
      odtDocument.save(odtFile);
      return odtFile;
    } catch (Exception ex) {
      throw new DocumentBuildException(ex.getMessage(), ex);
    }
  }

  private ODTDocumentBuilder() {
  }

  private void fill(final TextDocument odtDocument, final Publication publication) throws Exception {
    buildInfoSection(in(odtDocument), with(publication));
    //buildContentSection(in(odtDocument), with(publication));
    buildCommentSection(in(odtDocument), with(publication));
    buildWysiwygContentSection(in(odtDocument), new File(getClass().getResource(
        "/" + WYSIWYG_CONTENT).toURI()));
//    buildContentSection(in(odtDocument), with(publication));
    buildAttachmentSection(in(odtDocument), with(publication));
    buildClassificationSection(in(odtDocument), with(publication));
  }

  private static Publication with(final Publication publication) {
    return publication;
  }

  private static TextDocument in(final TextDocument document) {
    return document;
  }

  private void buildInfoSection(final TextDocument odtDocument, final Publication publication) {
    Meta metadata = odtDocument.getOfficeMetadata();
    metadata.setCreationDate(Calendar.getInstance());
    metadata.setCreator(publication.getAuthor());
    metadata.setSubject(publication.getDescription());
    metadata.setDcdate(Calendar.getInstance());
    metadata.setTitle(publication.getName());
    metadata.setUserDefinedDataValue("Modification", (new Date()).toString());
    metadata.setUserDefinedDataValue("Author", publication.getAuthor());
  }

  private void buildContentSection(final TextDocument odtDocument, final Publication publication)
      throws
      Exception {
    Section content = odtDocument.getSectionByName("Content");
    Paragraph p = content.getParagraphByIndex(1, false);
    if (p != null) {
      content.removeParagraph(p);
    }
    content.addParagraph(publication.getContent());
  }

  private void buildClassificationSection(final TextDocument odtDocument,
      final Publication publication) throws Exception {
    Section pdc = odtDocument.getSectionByName("Classing");
    List l = addALink("tartempion", odtDocument);
    pdc.getOdfElement().appendChild(l.getOdfElement().cloneNode(true));
    l.remove();
    pdc.addParagraph("ceci est toto");
    pdc.addParagraph("ceci est titi");
    pdc.addParagraph("");

    l = addALink("pas bô", odtDocument);
    pdc.getOdfElement().appendChild(l.getOdfElement().cloneNode(true));
    l.remove();
    pdc.addParagraph("ceci est tata");
    pdc.addParagraph("ceci est tutu");
  }

  private void buildWysiwygContentSection(final TextDocument odtDocument, final File wysiwyg) throws
      Exception {
    Section content = odtDocument.getSectionByName("Content");
    Paragraph p = content.getParagraphByIndex(1, false);
    if (p != null) {
      content.removeParagraph(p);
    }

    Node contentNode = odtDocument.getContentDom().getFirstChild().getLastChild().getFirstChild();
//    Node contentNode = content.getOdfElement();

    // converts the HTML WYSIWYG content in ODT
    ODTConverter converter = odtConverterTo(ConversionFormat.odt);
    File wysiwygInOdt = converter.convert(wysiwyg);
    TextDocument odt = TextDocument.loadDocument(wysiwygInOdt);

    // imports global styles
    OdfOfficeStyles documentStyles = odt.getDocumentStyles();
    OdfOfficeStyles stylesNode = (OdfOfficeStyles) odtDocument.getStylesDom().importNode(
        documentStyles, true);
    Iterable<OdfStyle> styles = stylesNode.getStylesForFamily(OdfStyleFamily.Paragraph);
    for (OdfStyle odfStyle : styles) {
      odfStyle.setStyleNameAttribute(WYSIWYG_STYLES_PREFIX + "_" + odfStyle.getStyleNameAttribute());
      odfStyle.setStyleDisplayNameAttribute(WYSIWYG_STYLES_PREFIX + " " + odfStyle.
          getStyleDisplayNameAttribute());
      String parentStyleName = odfStyle.getStyleParentStyleNameAttribute();
      if (parentStyleName != null && !parentStyleName.trim().isEmpty()) {
        odfStyle.setStyleParentStyleNameAttribute(WYSIWYG_STYLES_PREFIX + "_" + parentStyleName);
      }
      String nextStyleName = odfStyle.getStyleNextStyleNameAttribute();
      if (nextStyleName != null && !nextStyleName.trim().isEmpty()) {
        odfStyle.setStyleNextStyleNameAttribute(WYSIWYG_STYLES_PREFIX + "_" + nextStyleName);
      }
      odtDocument.getDocumentStyles().appendChild(odfStyle.cloneNode(true));
    }
    styles = stylesNode.getStylesForFamily(OdfStyleFamily.Text);
    for (OdfStyle odfStyle : styles) {
      odfStyle.setStyleNameAttribute(WYSIWYG_STYLES_PREFIX + "_" + odfStyle.getStyleNameAttribute());
      odfStyle.setStyleDisplayNameAttribute(WYSIWYG_STYLES_PREFIX + " " + odfStyle.
          getStyleDisplayNameAttribute());
      String parentStyleName = odfStyle.getStyleParentStyleNameAttribute();
      if (parentStyleName != null && !parentStyleName.trim().isEmpty()) {
        odfStyle.setStyleParentStyleNameAttribute(WYSIWYG_STYLES_PREFIX + "_" + parentStyleName);
      }
      String nextStyleName = odfStyle.getStyleNextStyleNameAttribute();
      if (nextStyleName != null && !nextStyleName.trim().isEmpty()) {
        odfStyle.setStyleNextStyleNameAttribute(WYSIWYG_STYLES_PREFIX + "_" + nextStyleName);
      }
      odtDocument.getDocumentStyles().appendChild(odfStyle.cloneNode(true));
    }

    // import master styles (page layouts)
    OdfOfficeMasterStyles documentMasterStyles = odt.getOfficeMasterStyles();
    OdfOfficeMasterStyles templateMasterStyles = odtDocument.getOfficeMasterStyles();
    Iterator<StyleMasterPageElement> masterPages = documentMasterStyles.getMasterPages();
    while (masterPages.hasNext()) {
      StyleMasterPageElement styleMasterPageElement = masterPages.next();
      StyleMasterPageElement existingMasterPage =
          templateMasterStyles.getMasterPage(styleMasterPageElement.getStyleNameAttribute());
      if (existingMasterPage == null) {
        Node masterPageNode = odtDocument.getStylesDom().importNode(styleMasterPageElement, true);
        odtDocument.getOfficeMasterStyles().appendChild(masterPageNode.cloneNode(true));
      }
    }

    // import styles of the content
    OdfOfficeAutomaticStyles automaticStyles = odt.getContentDom().getAutomaticStyles();
    OdfOfficeAutomaticStyles automaticStylesNode = (OdfOfficeAutomaticStyles) odtDocument.
        getContentDom().importNode(automaticStyles, true);
    styles = automaticStylesNode.getStylesForFamily(OdfStyleFamily.Paragraph);
    for (OdfStyle odfStyle : styles) {
      odfStyle.setStyleNameAttribute(WYSIWYG_STYLES_PREFIX + "_" + odfStyle.getStyleNameAttribute());
      String displayStyleName = odfStyle.getStyleDisplayNameAttribute();
      if (displayStyleName != null && !displayStyleName.trim().isEmpty()) {
        odfStyle.setStyleDisplayNameAttribute(WYSIWYG_STYLES_PREFIX + " " + displayStyleName);
      }
      String parentStyleName = odfStyle.getStyleParentStyleNameAttribute();
      if (parentStyleName != null && !parentStyleName.trim().isEmpty()) {
        odfStyle.setStyleParentStyleNameAttribute(WYSIWYG_STYLES_PREFIX + "_" + parentStyleName);
      }
      String nextStyleName = odfStyle.getStyleNextStyleNameAttribute();
      if (nextStyleName != null && !nextStyleName.trim().isEmpty()) {
        odfStyle.setStyleNextStyleNameAttribute(WYSIWYG_STYLES_PREFIX + "_" + nextStyleName);
      }
      odtDocument.getContentDom().getAutomaticStyles().appendChild(odfStyle.cloneNode(true));
    }
    styles = automaticStylesNode.getStylesForFamily(OdfStyleFamily.Text);
    for (OdfStyle odfStyle : styles) {
      odfStyle.setStyleNameAttribute(WYSIWYG_STYLES_PREFIX + "_" + odfStyle.getStyleNameAttribute());
      odfStyle.setStyleDisplayNameAttribute(WYSIWYG_STYLES_PREFIX + " " + odfStyle.
          getStyleDisplayNameAttribute());
      String parentStyleName = odfStyle.getStyleParentStyleNameAttribute();
      if (parentStyleName != null && !parentStyleName.trim().isEmpty()) {
        odfStyle.setStyleParentStyleNameAttribute(WYSIWYG_STYLES_PREFIX + "_" + parentStyleName);
      }
      String nextStyleName = odfStyle.getStyleNextStyleNameAttribute();
      if (nextStyleName != null && !nextStyleName.trim().isEmpty()) {
        odfStyle.setStyleNextStyleNameAttribute(WYSIWYG_STYLES_PREFIX + "_" + nextStyleName);
      }
      odtDocument.getContentDom().getAutomaticStyles().appendChild(odfStyle.cloneNode(true));
    }

    // import the content itself
    Node textNode = odt.getContentDom().getElementsByTagName("office:text").item(0);
    textNode = odtDocument.getContentDom().importNode(textNode, true);
    NodeList textContentNode = textNode.getChildNodes();
    for (int i = 0; i < textContentNode.getLength(); i++) {
      parseTree(textContentNode.item(i));
      contentNode.appendChild(textContentNode.item(i).cloneNode(true));
    }
//    
//
//    Node wysiwygNode = odtDocument.getContentDom().importNode(odt.getContentDom().getFirstChild(),
//            true);
//    Node odtDocumentStyles = odtDocument.getContentDom().getFirstChild().getChildNodes().item(2);
//    NodeList styleNodes = wysiwygNode.getChildNodes().item(2).getChildNodes();
//    for (int i = 0; i < styleNodes.getLength(); i++) {
//      Node style = styleNodes.item(i);
//      odtDocumentStyles.appendChild(style.cloneNode(true));
//    }
//    Node textNode = wysiwygNode.getLastChild().getFirstChild();
//    NodeList children = textNode.getChildNodes();
//    for (int i = 0; i < children.getLength(); i++) {
//      Node child = children.item(i);
//      contentNode.appendChild(child.cloneNode(true));
//    }
    //content.getOdfElement().appendChild(textNode.cloneNode(true));
    //odtDocument.getContentRoot().appendChild(textNode);
  }
  private static int deep = 0;

  private void parseTree(Node node) {
    NamedNodeMap attributes = node.getAttributes();
    if (attributes != null) {
      Node attribute = attributes.getNamedItem("text:style-name");
      if (attribute != null) {
        attribute.setNodeValue(WYSIWYG_STYLES_PREFIX + "_" + attribute.getNodeValue());
      }
    }
    if (node.hasChildNodes()) {
      NodeList children = node.getChildNodes();
      for (int i = 0; i < children.getLength(); i++) {
        deep++;
        parseTree(children.item(i));
      }
    }
  }

  private void buildCommentSection(final TextDocument odtDocument, final Publication publication) {
    Table comments = odtDocument.getTableByName("PubliComments");
    int i = 1;
    for (Comment comment : publication.getComments()) {
      Calendar calendar = Calendar.getInstance();
      calendar.setTime(comment.getDate());
      Row row = comments.getRowByIndex(i++);
      row.getCellByIndex(0).setStringValue(comment.getAuthor());
      row.getCellByIndex(1).setStringValue(comment.getText());
      row.getCellByIndex(2).setDateValue(calendar);
      row.getCellByIndex(3).setDateValue(calendar);
    }
  }

  private void buildAttachmentSection(final TextDocument odtDocument, final Publication publication) {
    Table attachments = odtDocument.getTableByName("PubliAttachments");
    String cellStyleName = attachments.getCellByPosition(0, 0).getCellStyleName();
    String paraStyleName = attachments.getCellByPosition(0, 0).getParagraphByIndex(0, true).
        getStyleName();
    Column col = attachments.appendColumn();
    col.getCellByIndex(0).setCellStyleName(cellStyleName);
    Paragraph p = col.getCellByIndex(0).addParagraph("Validateur");
    col.getCellByIndex(0).getOdfElement().setStyleName(cellStyleName);
    p.getOdfElement().setStyleName(paraStyleName);
    attachments.getCellByPosition(3, 0).setStringValue("Version");
  }

  private List addALink(String link, TextDocument odtDocument) throws Exception {
    TextAElement hyperlink = new TextAElement(odtDocument.getContentDom());
    hyperlink.setXlinkHrefAttribute("http://www.silverpeas.org");
    hyperlink.setXlinkTypeAttribute("simple");
    hyperlink.setTextContent(link);
    List l = odtDocument.addList();
    ListItem li = l.addItem("");
    li.getOdfElement().getFirstChild().appendChild(hyperlink);
    return l;
  }

  public class NoListDecorator extends BulletDecorator {

    public NoListDecorator(Document doc) {
      super(doc);
    }

    @Override
    public void decorateList(List list) {
    }

    @Override
    public void decorateListItem(ListItem li) {
    }
  }
}
