/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.api.macro.browse;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogRequest;
import sonia.blog.entity.ContentObject;

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
    StringBuffer formElement = new StringBuffer();

    formElement.append("<textarea name=\"").append(name);
    formElement.append("\"></textarea>");

    return formElement.toString();
  }
}
