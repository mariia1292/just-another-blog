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
public class StringInputWidget extends AbstractBlogMacroWidget
{

  /** Field description */
  public static final String PARAMETER_REGEX = "regex";

  //~--- get methods ----------------------------------------------------------

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
    StringBuffer formElement = new StringBuffer();

    formElement.append("<input type=\"text\" name=\"").append(name);
    formElement.append("\" />");

    return formElement.toString();
  }

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
  public String getResult(BlogRequest request, ContentObject object,
                          String name, String param)
  {
    String value = request.getParameter(name);

    if (Util.hasContent(value))
    {
      Map<String, String> params = buildParameterMap(param);
      String regex = params.get(PARAMETER_REGEX);

      if (Util.hasContent(regex))
      {
        if (!value.matches(regex))
        {
          throw new ValidationException();
        }
      }
    }

    return value;
  }
}
