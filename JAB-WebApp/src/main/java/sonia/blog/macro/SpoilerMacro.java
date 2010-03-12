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



package sonia.blog.macro;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogContext;
import sonia.blog.api.macro.WebMacro;
import sonia.blog.api.macro.WebResource;
import sonia.blog.api.macro.browse.CheckboxWidget;
import sonia.blog.api.macro.browse.StringInputWidget;
import sonia.blog.api.macro.browse.StringTextAreaWidget;

import sonia.macro.Macro;
import sonia.macro.MacroResult;
import sonia.macro.browse.MacroInfo;
import sonia.macro.browse.MacroInfoParameter;

import sonia.util.Util;

//~--- JDK imports ------------------------------------------------------------

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Sebastian Sdorra
 */
@MacroInfo(
  name = "spoiler",
  displayName = "macro.spoiler.displayName",
  description = "macro.spoiler.description",
  resourceBundle = "sonia.blog.resources.label",
  bodyWidget = StringTextAreaWidget.class,
  widgetParam = "cols=110;rows=25"
)
public class SpoilerMacro implements WebMacro
{

  /**
   * Method description
   *
   *
   * @param environment
   * @param body
   *
   * @return
   */
  public String doBody(Map<String, Object> environment, String body)
  {
    if (title == null)
    {
      title = "spoiler";
    }

    resources = new ArrayList<WebResource>();

    long time = System.nanoTime();
    StringBuffer result = new StringBuffer();

    result.append("<input type=\"button\" value=\"").append(title);
    result.append("\" onclick=\"");
    result.append("$('#spoiler_").append(time).append("').toggle();");
    result.append("\" />\n");
    result.append("<div");

    if (style != null)
    {
      result.append(" style=\"").append(style).append("\"");
    }

    if (styleClass != null)
    {
      result.append(" class=\"").append(styleClass).append("\"");
    }

    result.append(">");
    result.append("<div id=\"spoiler_").append(time);
    result.append("\" style=\"display: none;\">\n");

    if (macros)
    {
      MacroResult childResult =
        BlogContext.getInstance().getMacroParser().parseText(environment, body);

      if (childResult != null)
      {
        List<Macro> children = childResult.getMacros();

        if (Util.hasContent(children))
        {
          for (Macro child : children)
          {
            if (child instanceof WebMacro)
            {
              List<WebResource> childResources =
                ((WebMacro) child).getResources();

              if (childResources != null)
              {
                resources.addAll(childResources);
              }
            }
          }
        }

        body = childResult.getText();
      }
    }

    result.append(body).append("\n");
    result.append("</div>\n");
    result.append("</div>\n");

    return result.toString();
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  public List<WebResource> getResources()
  {
    return resources;
  }

  //~--- set methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param macros
   */
  @MacroInfoParameter(
    displayName = "macro.spoiler.macros.displayName",
    description = "macro.spoiler.macros.description",
    widget = CheckboxWidget.class,
    widgetParam = "checked=true",
    order = 0
  )
  public void setMacros(Boolean macros)
  {
    this.macros = macros;
  }

  /**
   * Method description
   *
   *
   * @param style
   */
  @MacroInfoParameter(
    displayName = "macro.spoiler.style.displayName",
    description = "macro.spoiler.style.description",
    widget = StringInputWidget.class,
    order = 2
  )
  public void setStyle(String style)
  {
    this.style = style;
  }

  /**
   * Method description
   *
   *
   * @param styleClass
   */
  @MacroInfoParameter(
    displayName = "macro.spoiler.styleClass.displayName",
    description = "macro.spoiler.styleClass.description",
    widget = StringInputWidget.class,
    order = 3
  )
  public void setStyleClass(String styleClass)
  {
    this.styleClass = styleClass;
  }

  /**
   * Method description
   *
   *
   * @param title
   */
  @MacroInfoParameter(
    displayName = "macro.spoiler.title.displayName",
    description = "macro.spoiler.title.description",
    widget = StringInputWidget.class,
    widgetParam = "value=Spoiler",
    order = 1
  )
  public void setTitle(String title)
  {
    this.title = title;
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private Boolean macros = Boolean.TRUE;

  /** Field description */
  private List<WebResource> resources;

  /** Field description */
  private String style;

  /** Field description */
  private String styleClass;

  /** Field description */
  private String title;
}
