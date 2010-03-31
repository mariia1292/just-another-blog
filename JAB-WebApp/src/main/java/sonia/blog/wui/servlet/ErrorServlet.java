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



package sonia.blog.wui.servlet;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.util.ErrorObject;

import sonia.util.Util;

//~--- JDK imports ------------------------------------------------------------

import java.io.IOException;
import java.io.PrintWriter;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Sebastian Sdorra
 */
public class ErrorServlet extends HttpServlet
{

  /** Field description */
  private static final String ATTRIBUTE_EXCEPTION =
    "javax.servlet.error.exception";

  /** Field description */
  private static final String ATTRIBUTE_MESSAGE = "javax.servlet.error.message";

  /** Field description */
  private static final String ATTRIBUTE_REQUESTURI =
    "javax.servlet.error.request_uri";

  /** Field description */
  private static final String ATTRIBUTE_STATUSCODE =
    "javax.servlet.error.status_code";

  /** Field description */
  private static Logger logger = Logger.getLogger(ErrorServlet.class.getName());

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param request
   * @param response
   *
   * @throws IOException
   * @throws ServletException
   */
  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
          throws ServletException, IOException
  {
    processRequest(request, response);
  }

  /**
   * Method description
   *
   *
   * @param request
   * @param response
   *
   * @throws IOException
   * @throws ServletException
   */
  @Override
  protected void doPost(HttpServletRequest request,
                        HttpServletResponse response)
          throws ServletException, IOException
  {
    processRequest(request, response);
  }

  /**
   * Method description
   *
   *
   * @param request
   * @param statusCode
   *
   * @return
   */
  private ErrorObject createErrorObject(HttpServletRequest request,
          int statusCode)
  {
    String message = (String) request.getAttribute(ATTRIBUTE_MESSAGE);
    Throwable exception = (Throwable) request.getAttribute(ATTRIBUTE_EXCEPTION);
    String requestUri = (String) request.getAttribute(ATTRIBUTE_REQUESTURI);

    return new ErrorObject(statusCode, message, exception, requestUri);
  }

  /**
   * Method description
   *
   *
   * @param object
   */
  private void logError(ErrorObject object)
  {
    if (object != null)
    {
      StringBuffer buffer = new StringBuffer();
      String message = object.getMessage();

      if (Util.isEmpty(message))
      {
        buffer.append("error: ").append(object.getStatusCode());
      }
      else
      {
        buffer.append(message);
      }

      Level l = Level.FINER;

      if (object.getStatusCode() >= 500)
      {
        l = Level.SEVERE;
      }

      Throwable throwable = object.getException();

      if (throwable != null)
      {
        logger.log(l, buffer.toString(), throwable);
      }
      else
      {
        logger.log(l, buffer.toString());
      }
    }
  }

  /**
   * Method description
   *
   *
   * @param request
   * @param response
   *
   * @throws IOException
   * @throws ServletException
   */
  private void processRequest(HttpServletRequest request,
                              HttpServletResponse response)
          throws ServletException, IOException
  {
    int errorCode = getErrorCode(request);
    ErrorObject errorObject = createErrorObject(request, errorCode);

    logError(errorObject);

    String uri = errorObject.getRequestUri();

    if ((uri != null)
        && (uri.endsWith(".jab") || uri.endsWith(".html")
            || uri.endsWith(".xhtml") || uri.endsWith(".jsp")))
    {
      request.getSession(true).setAttribute(ErrorObject.NAME, errorObject);

      StringBuffer path = new StringBuffer();

      path.append(request.getContextPath()).append("/error/").append(errorCode);
      path.append(".jab");
      response.sendRedirect(path.toString());
    }
    else
    {
      response.setContentType("text/plain");

      PrintWriter writer = response.getWriter();

      try
      {
        if (Util.isNotEmpty(errorObject.getMessage()))
        {
          writer.println(errorObject.getMessage());
        }
        else
        {
          writer.append("error: ").println(errorObject.getStatusCode());
        }
      }
      finally
      {
        writer.close();
      }
    }
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param request
   *
   * @return
   */
  private int getErrorCode(HttpServletRequest request)
  {
    int code = 500;
    Integer errorCode = (Integer) request.getAttribute(ATTRIBUTE_STATUSCODE);

    if (errorCode != null)
    {
      code = errorCode.intValue();
    }

    return code;
  }
}
