/**
 * Copyright (c) 2009, Sebastian Sdorra
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 3. Neither the name of JAB; nor the names of its
 *    contributors may be used to endorse or promote products derived from this
 *    software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE REGENTS OR CONTRIBUTORS BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * 
 * http://kenai.com/projects/jab
 * 
 */


package sonia.macro.browse;

//~--- non-JDK imports --------------------------------------------------------

import sonia.util.Util;

//~--- JDK imports ------------------------------------------------------------

import java.util.List;

/**
 *
 * @author Sebastian Sdorra
 */
public class MacroInformation
{

  /**
   * Constructs ...
   *
   *
   * @param name
   */
  public MacroInformation(String name)
  {
    this.name = name;
  }

  /**
   * Constructs ...
   *
   *
   * @param name
   * @param displayName
   */
  public MacroInformation(String name, String displayName)
  {
    this.name = name;
    this.displayName = displayName;
  }

  /**
   * Constructs ...
   *
   *
   * @param name
   * @param displayName
   * @param description
   */
  public MacroInformation(String name, String displayName, String description)
  {
    this.name = name;
    this.displayName = displayName;
    this.description = description;
  }

  /**
   * Constructs ...
   *
   *
   * @param name
   * @param displayName
   * @param description
   * @param icon
   */
  public MacroInformation(String name, String displayName, String description,
                          String icon)
  {
    this.name = name;
    this.displayName = displayName;
    this.description = description;
    this.icon = icon;
  }

  /**
   * Constructs ...
   *
   *
   * @param name
   * @param displayName
   * @param description
   * @param icon
   * @param parameter
   */
  public MacroInformation(String name, String displayName, String description,
                          String icon,
                          List<MacroInformationParameter> parameter)
  {
    this.name = name;
    this.displayName = displayName;
    this.description = description;
    this.icon = icon;
    this.parameter = parameter;
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  public Class<? extends MacroWidget> getBodyWidget()
  {
    return bodyWidget;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String getDescription()
  {
    return description;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String getDisplayName()
  {
    return Util.hasContent(displayName)
           ? displayName
           : name;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String getIcon()
  {
    return icon;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String getName()
  {
    return name;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public List<MacroInformationParameter> getParameter()
  {
    return parameter;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String getWidgetParam()
  {
    return widgetParam;
  }

  //~--- set methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param bodyWidget
   */
  public void setBodyWidget(Class<? extends MacroWidget> bodyWidget)
  {
    this.bodyWidget = bodyWidget;
  }

  /**
   * Method description
   *
   *
   * @param description
   */
  public void setDescription(String description)
  {
    this.description = description;
  }

  /**
   * Method description
   *
   *
   * @param displayName
   */
  public void setDisplayName(String displayName)
  {
    this.displayName = displayName;
  }

  /**
   * Method description
   *
   *
   * @param icon
   */
  public void setIcon(String icon)
  {
    this.icon = icon;
  }

  /**
   * Method description
   *
   *
   * @param name
   */
  public void setName(String name)
  {
    this.name = name;
  }

  /**
   * Method description
   *
   *
   * @param parameter
   */
  public void setParameter(List<MacroInformationParameter> parameter)
  {
    this.parameter = parameter;
  }

  /**
   * Method description
   *
   *
   * @param widgetParam
   */
  public void setWidgetParam(String widgetParam)
  {
    this.widgetParam = widgetParam;
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private Class<? extends MacroWidget> bodyWidget;

  /** Field description */
  private String description;

  /** Field description */
  private String displayName;

  /** Field description */
  private String icon;

  /** Field description */
  private String name;

  /** Field description */

  /** Field description */
  private List<MacroInformationParameter> parameter;

  /** Field description */
  private String widgetParam;
}