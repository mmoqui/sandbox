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
 * Exception thrown when the build of a document fail.
 */
public class DocumentBuildException extends RuntimeException {
  private static final long serialVersionUID = -3989726479565411559L;

  /**
   * Creates a new instance of <code>DocumentBuildException</code> without detail message.
   */
  public DocumentBuildException() {
  }

  /**
   * Constructs an instance of <code>DocumentBuildException</code> with the specified detail message.
   * @param msg the detail message.
   */
  public DocumentBuildException(String msg) {
    super(msg);
  }

  public DocumentBuildException(Throwable thrwbl) {
    super(thrwbl);
  }

  public DocumentBuildException(String string, Throwable thrwbl) {
    super(string, thrwbl);
  }

}
