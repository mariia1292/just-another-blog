/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.mapping;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogContext;
import sonia.blog.api.app.BlogJob;
import sonia.blog.api.app.BlogRequest;
import sonia.blog.api.app.BlogResponse;
import sonia.blog.api.app.Constants;
import sonia.blog.api.app.Context;
import sonia.blog.api.app.ResourceManager;
import sonia.blog.entity.Attachment;
import sonia.blog.util.ImageHandlerJob;

import sonia.config.Config;


import sonia.jobqueue.JobQueue;

import sonia.plugin.service.Service;

import sonia.security.encryption.Encryption;

import sonia.util.Util;

//~--- JDK imports ------------------------------------------------------------

import java.io.File;
import java.io.IOException;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author sdorra
 */
public class ImageMapping extends AbstractAttachmentMapping
{

  /** Field description */
  private static Logger logger = Logger.getLogger(ImageMapping.class.getName());

  //~--- methods --------------------------------------------------------------

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
        if (logger.isLoggable(Level.FINEST))
        {
          logger.log(Level.FINEST, null, ex);
        }

        response.sendError(HttpServletResponse.SC_BAD_REQUEST);
      }
    }
    else
    {
      response.sendError(HttpServletResponse.SC_NOT_FOUND);
    }
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  @Override
  protected Logger getLogger()
  {
    return logger;
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param response
   * @param attachment
   *
   * @throws IOException
   */
  private void printDirect(BlogResponse response, Attachment attachment)
          throws IOException
  {
    File file = getFile(response, attachment);

    printFile(response, attachment.getName(), attachment.getMimeType(), file);
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
    int x1 = getIntParameter(request, "x1");
    int x2 = getIntParameter(request, "x1");
    int y1 = getIntParameter(request, "x1");
    int y2 = getIntParameter(request, "x1");
    String color = request.getParameter("color");

    if ( Util.hasContent(type) && type.equals("orginal") )
    {
      printDirect(response, attachment);
    }
    else
    {
      File out = getOutputFile(request, attachment.getId(), type, format,
                               color, width, height, x1, x2, y1, y2);

      if (out.exists())
      {
        printFile(response, attachment.getName(), mimeType, out);
      }
      else
      {
        ImageHandlerJob job = new ImageHandlerJob(request.getCurrentBlog(),
                                getFile(response, attachment), out,
                                attachment.getId(), type, format, color, width,
                                height, x1, x2, y1, y2);

        queue.processs(job);

        if (out.exists())
        {
          printFile(response, attachment.getName(), mimeType, out);
        }
        else
        {
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
    return BlogContext.getDAOFactory().getAttachmentDAO().findByBlogAndId(
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
   * @param x1
   * @param x2
   * @param y1
   * @param y2
   *
   * @return
   */
  private String getFileName(Long id, String type, String format, String color,
                             int width, int height, int x1, int x2, int y1,
                             int y2)
  {
    String name = null;
    StringBuffer nameBuffer = new StringBuffer();

    nameBuffer.append(id).append(type).append(format);

    if (color != null)
    {
      nameBuffer.append(color);
    }

    nameBuffer.append(width).append(height).append(x1);
    nameBuffer.append(x2).append(y1).append(y2);

    if (encryption != null)
    {
      name = encryption.encrypt(nameBuffer.toString());
    }
    else
    {
      name = nameBuffer.toString();
    }

    return name;
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
   * @param x1
   * @param x2
   * @param y1
   * @param y2
   *
   * @return
   */
  private File getOutputFile(BlogRequest request, Long id, String type,
                             String format, String color, int width,
                             int height, int x1, int x2, int y1, int y2)
  {
    File directory = resourceManager.getDirectory(Constants.RESOURCE_IMAGE,
                       request.getCurrentBlog(), true);
    String name = getFileName(id, type, format, color, width, height, x1, x2,
                              y1, y2);

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
  @Service(Constants.SERVCIE_ENCRYPTION)
  private Encryption encryption;

  /** Field description */
  @Config(Constants.CONFIG_IMAGEMIMETYPE)
  private String mimeType = Constants.DEFAULT_IMAGE_MIMETYPE;

  /** Field description */
  @Config(Constants.CONFIG_IMAGEFORMAT)
  private String format = Constants.DEFAULT_IMAGE_FORMAT;

  /** Field description */
  @Context
  private JobQueue<BlogJob> queue;

  /** Field description */
  @Context
  private ResourceManager resourceManager;
}