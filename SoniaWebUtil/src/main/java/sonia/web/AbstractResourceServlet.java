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



package sonia.web;

//~--- non-JDK imports --------------------------------------------------------

import sonia.util.Util;

import sonia.web.util.WebUtil;

//~--- JDK imports ------------------------------------------------------------

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author sdorra
 */
public abstract class AbstractResourceServlet extends HttpServlet
{

  /** Field description */
  private static final long serialVersionUID = 5380464198820377459L;

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
  protected abstract Resource getResource(HttpServletRequest request)
          throws IOException, ServletException;

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
  protected void addAdditionalHeaders(HttpServletRequest request,
          HttpServletResponse response)
          throws IOException, ServletException {}

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
    processAction(request, response);
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
    processAction(request, response);
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
  protected void processAction(HttpServletRequest request,
                               HttpServletResponse response)
          throws ServletException, IOException
  {
    Resource resource = getResource(request);

    if (resource != null)
    {
      String contentType = resource.getContentType();

      if (Util.isEmpty(contentType))
      {
        contentType = WebUtil.DEFAULT_CONTENTTYPE;
      }

      response.setContentType(contentType);

      long size = resource.getSize();

      if (size >= 0)
      {
        response.setContentLength((int) size);
      }

      WebUtil.addLastModifiedHeader(response, resource);
      WebUtil.addETagHeader(response, resource);
      addAdditionalHeaders(request, response);

      if (WebUtil.isModified(request, resource))
      {
        copyResource(response, resource);
      }
      else
      {
        response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
      }
    }
    else
    {
      response.sendError(HttpServletResponse.SC_NOT_FOUND);
    }
  }

  /**
   * Method description
   *
   *
   * @param response
   * @param resource
   *
   * @throws IOException
   */
  private void copyResource(HttpServletResponse response, Resource resource)
          throws IOException
  {
    OutputStream out = null;
    InputStream in = null;

    try
    {
      out = response.getOutputStream();
      in = resource.getInputStream();
      Util.copy(in, out);
    }
    finally
    {
      if (out != null)
      {
        out.close();
      }

      if (in != null)
      {
        in.close();
      }
    }
  }
}
