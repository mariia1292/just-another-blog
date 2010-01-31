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

import sonia.blog.api.app.BlogContext;
import sonia.blog.api.app.BlogRequest;
import sonia.blog.api.app.BlogResponse;
import sonia.blog.api.mapping.FinalMapping;
import sonia.blog.entity.Attachment;

import sonia.util.Util;

import sonia.web.util.WebUtil;

//~--- JDK imports ------------------------------------------------------------

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Sebastian Sdorra
 */
public abstract class AbstractAttachmentMapping extends FinalMapping
{

  /**
   * Method description
   *
   *
   * @return
   */
  private static Logger logger = Logger.getLogger( AbstractAttachmentMapping.class.getName() );

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   *
   * @param request
   * @param response
   * @param name
   * @param mimeType
   * @param file
   */
  protected void printFile(BlogRequest request, BlogResponse response,
                           String name, String mimeType, File file)
  {
    response.setContentType(mimeType);
    response.setContentLength((int) file.length());
    response.setHeader("Content-Disposition", "filename=\"" + name + "\";");
    response.setDateHeader("Last-Modified", file.lastModified());
    WebUtil.addETagHeader(response, file);

    if (WebUtil.isModified(request, file))
    {
      print(response, file);
    }
    else
    {
      if (logger.isLoggable(Level.FINE))
      {
        logger.fine("send status 304, not modified");
      }

      response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
    }
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   *
   * @param response
   * @param attachment
   *
   * @return
   *
   * @throws IOException
   */
  protected File getFile(BlogResponse response, Attachment attachment)
          throws IOException
  {
    File file =
      BlogContext.getInstance().getResourceManager().getFile(attachment);

    if ((file == null) ||!file.exists() || file.isDirectory())
    {
      response.sendError(HttpServletResponse.SC_NOT_FOUND);
    }

    return file;
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param response
   * @param file
   */
  private void print(BlogResponse response, File file)
  {
    OutputStream out = null;
    InputStream in = null;

    try
    {
      if (logger.isLoggable(Level.FINEST))
      {
        StringBuffer msg = new StringBuffer("print file ");

        msg.append(file.getAbsolutePath());
        logger.finest(msg.toString());
      }

      out = response.getOutputStream();
      in = new FileInputStream(file);
      Util.copy(in, out);
    }
    catch (IOException ex)
    {
      logger.log(Level.SEVERE, null, ex);
    }
    finally
    {
      try
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
      catch (IOException e)
      {
        logger.log(Level.SEVERE, null, e);
      }
    }
  }
}
