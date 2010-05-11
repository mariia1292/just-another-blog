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



package sonia.blog.app;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogContext;

import sonia.web.AbstractResourceServlet;
import sonia.web.FileResource;
import sonia.web.Resource;
import sonia.web.util.WebUtil;

//~--- JDK imports ------------------------------------------------------------

import java.io.File;
import java.io.IOException;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author sdorra
 */
public class WebResourceServlet extends AbstractResourceServlet
{

  /** Field description */
  private static final long serialVersionUID = 5914982360733897598L;

  /** Field description */
  private static Logger logger =
    Logger.getLogger(WebResourceServlet.class.getName());

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @throws ServletException
   */
  @Override
  public void init() throws ServletException
  {
    int revision = BlogContext.getInstance().getBlogVersion().getRevision();

    prefix = new StringBuffer("/resources/rev").append(revision).append(
      "/").toString();
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
  protected void addAdditionalHeaders(HttpServletRequest request,
          HttpServletResponse response)
          throws IOException, ServletException
  {
    WebUtil.addStaticCacheControls(request, response, WebUtil.TIME_YEAR);
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param request
   *
   * @return
   *
   * @throws IOException
   * @throws ServletException
   */
  @Override
  protected Resource getResource(HttpServletRequest request)
          throws IOException, ServletException
  {
    Resource resource = null;
    String requestUri =
      request.getRequestURI().substring(request.getContextPath().length());

    if (requestUri.startsWith(prefix))
    {
      requestUri = new StringBuffer("/resources/").append(
        requestUri.substring(prefix.length())).toString();
    }

    File file = new File(getServletContext().getRealPath(requestUri));

    if (file.exists())
    {
      resource = new FileResource(file);
    }
    else if (logger.isLoggable(Level.FINE))
    {
      StringBuffer log = new StringBuffer("resource ").append(file.getPath());

      log.append(" not found");
      logger.fine(log.toString());
    }

    return resource;
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private String prefix;
}
