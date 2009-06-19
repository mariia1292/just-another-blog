/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.api.macro.browse;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogRequest;
import sonia.blog.entity.ContentObject;

//~--- JDK imports ------------------------------------------------------------

import java.util.Map;

/**
 *
 * @author sdorra
 */
public class StringTextAreaWidget extends StringInputWidget
{

  /**
   * Method description
   *
   *
   * @param request
   * @param object
   * @param name
   * @param param
   *
   * @return
   */
  @Override
  public String getFormElement(BlogRequest request, ContentObject object,
                               String name, String param)
  {
    Map<String, String> params = buildParameterMap(param);
    String cols = null;
    String rows = null;
    String style = null;
    String styleClass = null;

    if ((params != null) &&!params.isEmpty())
    {
      cols = params.get("cols");
      rows = params.get("rows");
      style = params.get("style");
      styleClass = params.get("class");
    }

    StringBuffer formElement = new StringBuffer();

    formElement.append("<textarea ");

    if (cols != null)
    {
      formElement.append("cols=\"").append(cols).append("\" ");
    }

    if (rows != null)
    {
      formElement.append("rows=\"").append(rows).append("\" ");
    }

    if (style != null)
    {
      formElement.append("style=\"").append(style).append("\" ");
    }

    if (styleClass != null)
    {
      formElement.append("class=\"").append(styleClass).append("\" ");
    }

    formElement.append("name=\"").append(name).append("\"></textarea>");

    return formElement.toString();
  }
}
