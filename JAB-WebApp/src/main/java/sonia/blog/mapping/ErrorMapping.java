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



package sonia.blog.mapping;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogRequest;
import sonia.blog.api.app.BlogResponse;
import sonia.blog.api.app.Constants;
import sonia.blog.api.mapping.FilterMapping;
import sonia.blog.api.mapping.MappingConfig;
import sonia.blog.api.mapping.MappingNavigation;
import sonia.blog.util.ErrorObject;
import sonia.blog.wui.ErrorBean;

//~--- JDK imports ------------------------------------------------------------

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import java.text.MessageFormat;

import java.util.Locale;
import java.util.ResourceBundle;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Sebastian Sdorra
 */
@MappingConfig(compressable = true)
public class ErrorMapping extends FilterMapping
{

  /**
   * Method description
   *
   *
   * @return
   */
  public MappingNavigation getMappingNavigation()
  {
    return null;
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param request
   * @param response
   * @param param
   *
   * @return
   *
   * @throws IOException
   * @throws ServletException
   */
  @Override
  protected String handleFilterMapping(BlogRequest request,
          BlogResponse response, String[] param)
          throws IOException, ServletException
  {
    if ((param != null) && (param.length > 0))
    {
      try
      {
        int code = Integer.parseInt(param[0]);

        handleErrorMapping(request, response, code);
      }
      catch (NumberFormatException ex)
      {
        response.sendError(HttpServletResponse.SC_NOT_FOUND);
      }
    }
    else
    {
      response.sendError(HttpServletResponse.SC_NOT_FOUND);
    }

    return buildTemplateViewId(request, Constants.TEMPLATE_ERROR);
  }

  /**
   * Method description
   *
   *
   * @param request
   * @param resposne
   * @param code
   */
  private void handleErrorMapping(BlogRequest request, BlogResponse resposne,
                                  int code)
  {
    String messageKey = null;
    String description = null;
    ErrorObject errorObject = getErrorObject(request);
    String uri = (errorObject != null)
                 ? errorObject.getRequestUri()
                 : "";

    switch (code)
    {

      /*
       * case HttpServletResponse.SC_BAD_REQUEST :
       * break;
       *
       * case HttpServletResponse.SC_UNAUTHORIZED :
       * break;
       *
       */
      case HttpServletResponse.SC_FORBIDDEN :
        messageKey = "accessDenied";
        description = MessageFormat.format(getMessage(request.getLocale(),
                "accessDeniedMessage"), uri);

        break;

      case HttpServletResponse.SC_NOT_FOUND :
        messageKey = "notFound";
        description = MessageFormat.format(getMessage(request.getLocale(),
                "notFoundMessage"), uri);

        break;

      default :
        messageKey = "unknownError";

        break;
    }

    ErrorBean errorBean = new ErrorBean();

    if (errorObject != null)
    {
      Throwable exception = errorObject.getException();

      if (exception != null)
      {
        errorBean.setStacktrace(getStacktrace(exception));
      }
    }

    errorBean.setMessage(getMessage(request.getLocale(), messageKey));
    errorBean.setDescription(description);
    request.setAttribute(ErrorBean.NAME, errorBean);
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param locale
   *
   * @return
   */
  private ResourceBundle getBundle(Locale locale)
  {
    if (bundle == null)
    {
      bundle = ResourceBundle.getBundle("sonia.blog.resources.message", locale);
    }

    return bundle;
  }

  /**
   * Method description
   *
   *
   * @param request
   *
   * @return
   */
  private ErrorObject getErrorObject(BlogRequest request)
  {
    ErrorObject errorObject = null;
    HttpSession session = request.getSession();

    if (session != null)
    {
      errorObject = (ErrorObject) session.getAttribute(ErrorObject.NAME);
      session.removeAttribute(ErrorObject.NAME);
    }

    return errorObject;
  }

  /**
   * Method description
   *
   *
   * @param locale
   * @param key
   *
   * @return
   */
  private String getMessage(Locale locale, String key)
  {
    return getBundle(locale).getString(key);
  }

  /**
   * Method description
   *
   *
   * @param throwable
   *
   * @return
   */
  private String getStacktrace(Throwable throwable)
  {
    StringWriter stringWriter = new StringWriter();
    PrintWriter writer = new PrintWriter(stringWriter);

    try
    {
      throwable.printStackTrace(writer);
    }
    finally
    {
      writer.close();
    }

    return stringWriter.toString();
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private ResourceBundle bundle;
}
