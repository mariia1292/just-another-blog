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



package sonia.blog.api.macro.browse;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogRequest;
import sonia.blog.entity.ContentObject;

import sonia.util.Util;

//~--- JDK imports ------------------------------------------------------------

import java.util.Map;

/**
 *
 * @author Sebastian Sdorra
 */
public class StringInputWidget extends AbstractBlogMacroWidget
{

  /** Field description */
  public static final String PARAMETER_REGEX = "regex";

  /** Field description */
  public static final String PARAMETER_VALUE = "value";

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
    Map<String, String> params = buildParameterMap(param);

    formElement.append("<input type=\"text\" name=\"").append(name);
    formElement.append("\"");

    if ((params != null) &&!params.isEmpty())
    {
      String value = params.get(PARAMETER_VALUE);

      if (Util.hasContent(value))
      {
        formElement.append("value=\"").append(value).append("\"");
      }
    }

    formElement.append(" />");

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
