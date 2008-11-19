/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.macro;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.entity.Entry;

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
   * @param parameters
   *
   * @return
   */
  public String excecute(Map<String, ?> environment, String body,
                         Map<String, String> parameters)
  {
    String title = parameters.get("title");

    if (title == null)
    {
      title = "spoiler";
    }

    String style = parameters.get("style");
    String clazz = parameters.get("class");
    Entry entry = (Entry) environment.get("object");
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

    if (clazz != null)
    {
      result += " class=\"" + clazz + "\"";
    }

    result += ">";
    result += "<div id=\"spoiler_" + entry.getId()
              + "\" style=\"display: none;\">\n";
    result += body + "\n";
    result += "</div>\n";
    result += "</div>\n";
    result += "</div>\n";

    return result;
  }
}
