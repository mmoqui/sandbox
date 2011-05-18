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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * A publication is a set of texts about a given subject.
 */
@XmlRootElement
public class Publication {

  private String name;
  private String description;
  private String content;
  private String author;
  @XmlElementWrapper
  @XmlElement(name="comment")
  private List<Comment> comments = new ArrayList<Comment>();
  @XmlElementWrapper
  @XmlElement(name="attachment")
  private List<String> attachments = new ArrayList<String>();

  public static Publication aPublicationNamed(String name) {
    return new Publication(name);
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  @XmlElement(required=true)
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void addComment(final Comment comment) {
    comments.add(comment);
  }

  public void addAttachments(String attachmentPath) {
    attachments.add(attachmentPath);
  }

  public List<String> getAttachments() {
    return Collections.unmodifiableList(attachments);
  }

  public List<Comment> getComments() {
    return Collections.unmodifiableList(comments);
  }

  public String getAuthor() {
    return author;
  }

  public void setAuthor(String author) {
    this.author = author;
  }

  protected Publication(String name) {
    this.name = name;
  }

  protected Publication() {}
}
