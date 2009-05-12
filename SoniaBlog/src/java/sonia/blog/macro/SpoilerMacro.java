/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
 * @author sdorra
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
