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
 *
 * @author miguel
 */
public class LoggingModule {

  private String level;
  private String module;

  protected LoggingModule() {

  }

  protected void setModule(String module) {
    this.module = module;
  }

  public LoggingModule(String module) {
    this.module = module;
  }

  public void setLevel(String level) {
    this.level = level;
  }

  public String getLevel() {
    return this.level;
  }

  public String getModule() {
    return this.module;
  }


}
