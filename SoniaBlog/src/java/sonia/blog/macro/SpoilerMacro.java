/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.macro;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.entity.ContentObject;

import sonia.macro.Macro;

//~--- JDK imports ------------------------------------------------------------

import java.util.Map;

/**
 *
 * @author sdorra
 */
public class SpoilerMacro implements Macro
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

    ContentObject object = (ContentObject) environment.get("object");
    String js = "var div = this.parentNode.getElementsByTagName('div')[0]";

    js += ".getElementsByTagName('div')[0];";
    js += "if ( div.style.display != '' ){ div.style.display = '' } ";
    js += "else { div.style.display = 'none' }";

    String result = "<div>\n";

    result += "<input type=\"button\" value=\"" + title + "\" onclick=\"" + js
              + "\" />\n";
    result += "<div";

    if (style != null)
    {
      result += " style=\"" + style + "\"";
    }

    if (styleClass != null)
    {
      result += " class=\"" + styleClass + "\"";
    }

    result += ">";
    result += "<div id=\"spoiler_" + object.getId()
              + "\" style=\"display: none;\">\n";
    result += body + "\n";
    result += "</div>\n";
    result += "</div>\n";
    result += "</div>\n";

    return result;
  }

  //~--- set methods ----------------------------------------------------------

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
  private String style;

  /** Field description */
  private String styleClass;

  /** Field description */
  private String title;
}
