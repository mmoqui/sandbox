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
package org.silverpeas.sanbox.jee7sandbox.util;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * My custom logger.
 * @author miguel
 */
public class MyLogger {

  private static final String ROOT_NAME = "JEE7Sandbox";
  private final Logger logger;

  public static final MyLogger getLogger(String module) {
    return new MyLogger(Logger.getLogger(ROOT_NAME + "." + module));
  }

  private MyLogger(Logger wrappedLogger) {
    this.logger = wrappedLogger;
  }

  public void info(String msg) {
    this.logger.log(Level.INFO, msg);
  }

  public void debug(String msg) {
    this.logger.log(Level.FINE, msg);
  }

  public void debug(String msg, Throwable t) {
    this.logger.log(Level.FINE, msg, t);
  }

  public void warn(String msg) {
    this.logger.log(Level.WARNING, msg);
  }

  public void error(String msg) {
    this.logger.log(Level.SEVERE, msg);
  }

  public void error(String msg, Throwable t) {
    this.logger.log(Level.SEVERE, msg, t);
  }

  public Level getLevel() {
    Level level = this.logger.getLevel();
    Logger parent = this.logger.getParent();
    while(level == null) {
      level = parent.getLevel();
      parent = parent.getParent();
    }
    return level;
  }

  public void setLevel(Level level) {
    this.logger.setLevel(level);
  }
}
