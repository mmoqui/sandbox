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

/**
 * Exception thrown when an error occurs while converting a document to another format.
 */
public class DocumentConvertionException extends RuntimeException {
  private static final long serialVersionUID = 5458173513843306754L;

  /**
   * Creates a new instance of <code>FormatConvertionException</code> without detail message.
   */
  public DocumentConvertionException() {
  }

  /**
   * Constructs an instance of <code>FormatConvertionException</code> with the specified detail message.
   * @param msg the detail message.
   */
  public DocumentConvertionException(String msg) {
    super(msg);
  }

  public DocumentConvertionException(Throwable thrwbl) {
    super(thrwbl);
  }

  public DocumentConvertionException(String string, Throwable thrwbl) {
    super(string, thrwbl);
  }

}
