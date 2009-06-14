/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.api.macro.browse;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogRequest;
import sonia.blog.entity.ContentObject;

import sonia.util.Util;

//~--- JDK imports ------------------------------------------------------------

import java.util.Map;

/**
 *
 * @author sdorra
 */
public class SelectWidget extends AbstractBlogMacroWidget
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
    StringBuffer out = new StringBuffer();
    Map<String, String> params = buildParameterMap(param);

    if ((params != null) &&!params.isEmpty())
    {
      String optionParam = params.get("options");

      if (Util.hasContent(optionParam))
      {
        String[] options = optionParam.split("\\|");

        out.append("<select name=\"").append(name).append("\">");

        for (String option : options)
        {
          out.append("<option>").append(option).append("</option>");
        }

        out.append("</select>");
      }
      else
      {
        out.append("-- option param is required --");
      }
    }
    else
    {
      out.append("-- option param is required --");
    }

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
    return request.getParameter(name);
  }
}
