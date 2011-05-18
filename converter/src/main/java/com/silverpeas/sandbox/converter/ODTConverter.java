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

import com.artofsolving.jodconverter.DocumentConverter;
import com.artofsolving.jodconverter.openoffice.connection.OpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.connection.SocketOpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.converter.OpenOfficeDocumentConverter;
import java.io.File;
import org.apache.commons.io.FilenameUtils;

/**
 * A converter of an ODT document to another format.
 */
public class ODTConverter {

  private static final int OPENOFFICE_SERVER_PORT = 8100;
  private static String DESTINATION_PATH = "";

  public static void setDestinationPath(String destinationPath) {
    if (!destinationPath.isEmpty()) {
      DESTINATION_PATH = destinationPath;
      if (!destinationPath.endsWith("/")) {
        DESTINATION_PATH += "/";
      }
    }
  }

  public static ODTConverter odtConverterTo(ConversionFormat format) {
    return new ODTConverter(format);
  }

  public File convert(final File document) {
    String fileName = document.getName();
    String suffix = FilenameUtils.getExtension(fileName);
    if (suffix != null && !suffix.trim().isEmpty()) {
      fileName = FilenameUtils.getBaseName(fileName);
    }
    fileName += "." + format.name();
    File output = new File(DESTINATION_PATH + fileName);
    OpenOfficeConnection connection = new SocketOpenOfficeConnection(OPENOFFICE_SERVER_PORT);
    try {
      connection.connect();
      DocumentConverter converter = new OpenOfficeDocumentConverter(connection);
      converter.convert(document, output);
    } catch (Exception e) {
      throw new DocumentConvertionException(e.getMessage(), e);
    } finally {
      if (connection.isConnected()) {
        connection.disconnect();
      }
    }

    return output;
  }
  private final ConversionFormat format;

  private ODTConverter(ConversionFormat format) {
    this.format = format;
  }
}
