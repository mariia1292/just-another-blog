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
public class CheckboxWidget extends AbstractBlogMacroWidget
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
  public String getFormElement(BlogRequest request, ContentObject object,
                               String name, String param)
  {
    Map<String, String> params = buildParameterMap(param);
    String checked = null;

    if (params != null)
    {
      checked = params.get("checked");
    }

    StringBuffer out = new StringBuffer();

    out.append("<input type=\"checkbox\" ");

    if ((checked != null) && checked.equalsIgnoreCase("true"))
    {
      out.append("checked=\"checked\" ");
    }

    out.append("name=\"").append(name);
    out.append("\" />");

    return out.toString();
  }

  /**
   * Method description
   *
   *
   * @param request
   * @param obejct
   * @param name
   * @param param
   *
   * @return
   */
  public String getResult(BlogRequest request, ContentObject obejct,
                          String name, String param)
  {
    return (request.getParameter(name) != null)
           ? "true"
           : "false";
  }
}
