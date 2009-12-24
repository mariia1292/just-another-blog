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

/**
 *
 * @author Sebastian Sdorra
 */
public class MacroInformationParameter
{

  /**
   * Constructs ...
   *
   *
   * @param name
   */
  public MacroInformationParameter(String name)
  {
    this.name = name;
  }

  /**
   * Constructs ...
   *
   *
   * @param name
   * @param label
   */
  public MacroInformationParameter(String name, String label)
  {
    this.name = name;
    this.label = label;
  }

  /**
   * Constructs ...
   *
   *
   * @param name
   * @param label
   * @param description
   */
  public MacroInformationParameter(String name, String label,
                                   String description)
  {
    this.name = name;
    this.label = label;
    this.description = description;
  }

  //~--- get methods ----------------------------------------------------------

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
  public String getLabel()
  {
    return label;
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
  public Class<? extends MacroWidget> getWidget()
  {
    return widget;
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
   * @param label
   */
  public void setLabel(String label)
  {
    this.label = label;
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
   * @param widget
   */
  public void setWidget(Class<? extends MacroWidget> widget)
  {
    this.widget = widget;
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
  private String description;

  /** Field description */
  private String label;

  /** Field description */
  private String name;

  /** Field description */
  private Class<? extends MacroWidget> widget;

  /** Field description */
  private String widgetParam;
}
