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
import sonia.blog.api.macro.ScriptResource;
import sonia.blog.api.macro.WebMacro;
import sonia.blog.api.macro.WebResource;

import sonia.macro.Macro;
import sonia.macro.MacroResult;

import sonia.util.Util;

//~--- JDK imports ------------------------------------------------------------

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Sebastian Sdorra
 */
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
  public String doBody(Map<String, ?> environment, String body)
  {
    if (title == null)
    {
      title = "spoiler";
    }

    resources = new ArrayList<WebResource>();

    String linkBase = (String) environment.get("linkBase");
    ScriptResource jquery = new ScriptResource(20,
                              linkBase + "resources/jquery/jquery.min.js");

    resources.add(jquery);

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
      }

      body = childResult.getText();
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