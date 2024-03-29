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
import sonia.blog.api.app.Constants;
import sonia.blog.api.app.Context;
import sonia.blog.api.app.ResourceManager;
import sonia.blog.entity.Attachment;
import sonia.blog.util.ImageHandlerJob;

import sonia.config.Config;

import sonia.util.Util;

import sonia.web.util.WebUtil;

//~--- JDK imports ------------------------------------------------------------

import java.io.File;
import java.io.IOException;

import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Sebastian Sdorra
 */
public class ImageMapping extends AbstractAttachmentMapping
{

  /** Field description */
  private static final Logger logger =
    Logger.getLogger(ImageMapping.class.getName());

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param request
   * @param response
   * @param file
   */
  @Override
  protected void addAdditionalHeaderds(BlogRequest request,
          BlogResponse response, File file)
  {
    WebUtil.addStaticCacheControls(request, response, WebUtil.TIME_YEAR);
  }

  /**
   * Method description
   *
   *
   * @param request
   * @param response
   * @param param
   *
   * @throws IOException
   * @throws ServletException
   */
  @Override
  protected void handleFinalMapping(BlogRequest request, BlogResponse response,
                                    String[] param)
          throws IOException, ServletException
  {
    if ((param != null) && (param.length > 0))
    {
      try
      {
        long id = Long.parseLong(param[0]);
        Attachment attachment = getAttachment(request, id);

        if ((attachment != null) && isImage(attachment))
        {
          printImage(request, response, attachment);
        }
        else
        {
          response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
      }
      catch (NumberFormatException ex)
      {
        StringBuffer msg = new StringBuffer();

        msg.append(param[0]).append(" is not a number");

        if (logger.isLoggable(Level.FINEST))
        {
          logger.log(Level.FINEST, msg.toString(), ex);
        }

        response.sendError(HttpServletResponse.SC_BAD_REQUEST, msg.toString());
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
   *
   * @param request
   * @param response
   * @param attachment
   *
   * @throws IOException
   */
  private void printDirect(BlogRequest request, BlogResponse response,
                           Attachment attachment)
          throws IOException
  {
    File file = getFile(response, attachment);

    printFile(request, response, attachment.getName(),
              attachment.getMimeType(), file);
  }

  /**
   * Method description
   *
   *
   * @param request
   * @param response
   * @param attachment
   *
   * @throws IOException
   */
  private void printImage(BlogRequest request, BlogResponse response,
                          Attachment attachment)
          throws IOException
  {
    String type = request.getParameter("type");
    int width = getIntParameter(request, "width");
    int height = getIntParameter(request, "height");
    int x = getIntParameter(request, "x");
    int y = getIntParameter(request, "y");
    String color = request.getParameter("color");

    if (Util.hasContent(type) && type.equals("orginal"))
    {
      printDirect(request, response, attachment);
    }
    else
    {
      File out = getOutputFile(request, attachment.getId(), type, format,
                               color, width, height, x, y);

      if (out.exists())
      {
        printFile(request, response, attachment.getName(), mimeType, out);
      }
      else
      {
        ImageHandlerJob job = new ImageHandlerJob(request.getCurrentBlog(),
                                getFile(response, attachment), out, type,
                                format, color, width, height, x, y);
        Future<Boolean> future =
          BlogContext.getInstance().getThreadPoolExecutor().submit(job,
            Boolean.TRUE);

        try
        {
          if (future.get())
          {
            if (out.exists())
            {
              printFile(request, response, attachment.getName(), mimeType, out);
            }
            else
            {
              response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
          }
          else
          {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
          }
        }
        catch (Exception ex)
        {
          logger.log(Level.SEVERE, null, ex);
          response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
      }
    }
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param request
   * @param id
   *
   * @return
   */
  private Attachment getAttachment(BlogRequest request, long id)
  {
    return BlogContext.getDAOFactory().getAttachmentDAO().get(
        request.getCurrentBlog(), id);
  }

  /**
   * Method description
   *
   *
   *
   * @param id
   * @param type
   * @param format
   * @param color
   * @param width
   * @param height
   * @param x
   * @param y
   *
   * @return
   */
  private String getFileName(Long id, String type, String format, String color,
                             int width, int height, int x, int y)
  {
    StringBuffer nameBuffer = new StringBuffer();

    nameBuffer.append(id).append("_");

    if (format != null)
    {
      nameBuffer.append(format);
    }
    else
    {
      nameBuffer.append("-");
    }

    nameBuffer.append("_");

    if (type != null)
    {
      nameBuffer.append(type);
    }
    else
    {
      nameBuffer.append("-");
    }

    nameBuffer.append("_");

    if (color != null)
    {
      nameBuffer.append(color);
    }
    else
    {
      nameBuffer.append("-");
    }

    nameBuffer.append("_").append(width).append("_").append(height).append("_");
    nameBuffer.append(x).append("_").append(y);

    return nameBuffer.toString();
  }

  /**
   * Method description
   *
   *
   * @param request
   * @param name
   *
   * @return
   */
  private int getIntParameter(BlogRequest request, String name)
  {
    int result = -1;

    try
    {
      String value = request.getParameter(name);

      if (Util.hasContent(value))
      {
        result = Integer.parseInt(value);
      }
    }
    catch (NumberFormatException ex)
    {
      if (logger.isLoggable(Level.FINEST))
      {
        logger.log(Level.FINEST, null, ex);
      }
    }

    return result;
  }

  /**
   * Method description
   *
   *
   * @param request
   * @param id
   * @param type
   * @param format
   * @param color
   * @param width
   * @param height
   * @param x
   * @param y
   *
   * @return
   */
  private File getOutputFile(BlogRequest request, Long id, String type,
                             String format, String color, int width,
                             int height, int x, int y)
  {
    File directory = resourceManager.getDirectory(Constants.RESOURCE_IMAGE,
                       request.getCurrentBlog(), true);
    String name = getFileName(id, type, format, color, width, height, x, y);

    return new File(directory, name);
  }

  /**
   * Method description
   *
   *
   * @param attachment
   *
   * @return
   */
  private boolean isImage(Attachment attachment)
  {
    return attachment.getMimeType().toLowerCase().startsWith("image");
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  @Config(Constants.CONFIG_IMAGEMIMETYPE)
  private String mimeType = Constants.DEFAULT_IMAGE_MIMETYPE;

  /** Field description */
  @Config(Constants.CONFIG_IMAGEFORMAT)
  private String format = Constants.DEFAULT_IMAGE_FORMAT;

  /** Field description */
  @Context
  private ResourceManager resourceManager;
}
