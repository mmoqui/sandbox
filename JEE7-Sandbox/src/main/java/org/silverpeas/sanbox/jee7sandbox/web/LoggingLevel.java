/**
 * Copyright (C) 2000 - 2015 Silverpeas
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * <p>
 * As a special exception to the terms and conditions of version 3.0 of
 * the GPL, you may redistribute this Program in connection with Free/Libre
 * Open Source Software ("FLOSS") applications as described in Silverpeas's
 * FLOSS exception.  You should have received a copy of the text describing
 * the FLOSS exception, and it is also available here:
 * "http://www.silverpeas.org/docs/core/legal/floss_exception.html"
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.silverpeas.sanbox.jee7sandbox.web;

import java.util.logging.Level;

/**
 * @author miguel
 */
public enum LoggingLevel {
  INFO,
  DEBUG,
  WARN,
  ERROR;

  public static LoggingLevel fromJavaLoggingLevel(final Level level) {
    if (level.intValue() == Level.FINE.intValue() || level.intValue() == Level.FINEST.intValue()
        || level.intValue() == Level.FINER.intValue()) {
      return DEBUG;
    } else if (level.intValue() == Level.WARNING.intValue()) {
      return WARN;
    } else if (level.intValue() == Level.SEVERE.intValue()) {
      return ERROR;
    } else {
      return INFO;
    }
  }

  public Level toJavaLoggingLevel() {
    switch (this) {
      case INFO:
        return Level.INFO;
      case DEBUG:
        return Level.FINE;
      case WARN:
        return Level.WARNING;
      case ERROR:
        return Level.SEVERE;
    }
    return Level.INFO;
  }

}
