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
import sonia.blog.api.dao.AttachmentDAO;
import sonia.blog.api.mapping.FinalMapping;
import sonia.blog.entity.Attachment;
import sonia.blog.entity.Blog;
import sonia.blog.entity.Role;
import sonia.blog.util.ImageResizingJob;

import sonia.config.Config;

import sonia.util.ImageUtil;
import sonia.util.Util;

//~--- JDK imports ------------------------------------------------------------

import java.awt.Dimension;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author sdorra
 */
public class AttachmentMapping extends FinalMapping
{

  /** Field description */
  private static Logger logger =
    Logger.getLogger(AttachmentMapping.class.getName());

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
    boolean found = true;

    if ((param != null) && (param.length > 0))
    {
      try
      {
        long id = Long.parseLong(param[0]);
        AttachmentDAO attachmentDAO =
          BlogContext.getDAOFactory().getAttachmentDAO();
        Blog blog = request.getCurrentBlog();
        Attachment attachment = attachmentDAO.get(id);

        if (((attachment != null) && attachment.isBlog(blog))
            && (((attachment.getEntry() != null)
                 && attachment.getEntry()
                   .isPublished()) || ((attachment.getPage() != null)
                                       && attachment.getPage()
                                         .isPublished()) || request
                                           .isUserInRole(Role.ADMIN) || request
                                           .isUserInRole(Role.AUTHOR)))
        {
          printAttachment(request, response, attachment);
        }
        else
        {
          found = false;
        }
      }
      catch (NumberFormatException ex)
      {
        logger.log(Level.FINE, null, ex);
        found = false;
      }
    }
    else
    {
      found = false;
    }

    if (!found)
    {
      response.sendError(HttpServletResponse.SC_NOT_FOUND);
    }
  }

  /**
   * Method description
   *
   *
   * @param source
   * @param target
   *
   * @throws IOException
   */
  private void copy(File source, File target) throws IOException
  {
    InputStream in = null;
    OutputStream out = null;

    try
    {
      in = new FileInputStream(source);
      out = new FileOutputStream(target);
      Util.copy(in, out);
    }
    finally
    {
      if (in != null)
      {
        in.close();
      }

      if (out != null)
      {
        out.close();
      }
    }
  }

  /**
   * Method description
   *
   *
   *
   *
   * @param blog
   * @param source
   * @param target
   * @param maxWidth
   * @param maxHeight
   */
  private void createImage(Blog blog, File source, File target, int maxWidth,
                           int maxHeight)
  {
    try
    {
      Dimension d = ImageUtil.getDimension(new FileInputStream(source));

      if ((d.getWidth() < maxWidth) && (d.getHeight() < maxHeight))
      {
        copy(source, target);
      }
      else
      {
        double width = d.getWidth();
        double height = d.getHeight();
        double ratio = height / width;

        if ((maxWidth > 0) && (width > maxWidth))
        {
          width = maxWidth;
          height = width * ratio;
        }

        ratio = width / height;

        if ((maxHeight > 0) && (height > maxHeight))
        {
          height = maxHeight;
          width = height * ratio;
        }

        resizeImage(blog, source, target, (int) width, (int) height);
      }
    }
    catch (Exception ex)
    {
      throw new RuntimeException(ex);
    }
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
  private void printAttachment(BlogRequest request, BlogResponse response,
                               Attachment attachment)
          throws IOException
  {
    File file = getFile(attachment);

    if (file.exists())
    {
      if (isImage(attachment) && (request.getParameter("orginal") == null))
      {
        printImage(request, response, attachment.getName(), file);
      }
      else
      {
        printFile(response, attachment.getName(), attachment.getMimeType(),
                  file);
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
   * @param name
   * @param mimeType
   * @param file
   */
  private void printFile(BlogResponse response, String name, String mimeType,
                         File file)
  {
    response.setContentType(mimeType);
    response.setContentLength((int) file.length());
    response.setHeader("Content-Disposition", "filename=\"" + name + "\";");
    response.setDateHeader("Last-Modified", file.lastModified());

    OutputStream out = null;
    InputStream in = null;

    try
    {
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

  /**
   * Method description
   *
   *
   * @param request
   * @param response
   * @param name
   * @param file
   */
  private void printImage(BlogRequest request, BlogResponse response,
                          String name, File file)
  {
    int maxWidth = 0;
    int maxHeight = 0;
    Blog blog = request.getCurrentBlog();

    if (request.getParameter("thumb") != null)
    {
      maxWidth = blog.getThumbnailWidth();
      maxHeight = blog.getThumbnailHeight();
    }
    else
    {
      maxWidth = blog.getImageWidth();
      maxHeight = blog.getImageHeight();
    }

    printImage(response, name, file, blog, maxWidth, maxHeight);
  }

  /**
   * Method description
   *
   *
   * @param response
   * @param name
   * @param file
   * @param blog
   * @param maxWidth
   * @param maxHeight
   */
  private void printImage(BlogResponse response, String name, File file,
                          Blog blog, int maxWidth, int maxHeight)
  {
    File image = getFile(file, blog, maxWidth, maxHeight);

    if (!image.exists())
    {
      if ((maxWidth == 0) && (maxHeight == 0))
      {
        image = file;
      }
      else
      {
        createImage(blog, file, image, maxWidth, maxHeight);
      }
    }

    printFile(response, name, mimeType, image);
  }

  /**
   * Method description
   *
   *
   *
   * @param blog
   * @param source
   * @param target
   * @param width
   * @param height
   *
   * @throws IOException
   */
  private void resizeImage(Blog blog, File source, File target, int width,
                           int height)
          throws IOException
  {
    BlogJob job = new ImageResizingJob(blog, source, target, format, width,
                                       height);

    BlogContext.getInstance().getJobQueue().processs(job);

    /*
     * InputStream in = null;
     * OutputStream out = null;
     *
     * try
     * {
     * if (logger.isLoggable(Level.INFO))
     * {
     *   logger.info("resize image " + source.getName() + " (resolution "
     *               + width + "x" + height + ")");
     * }
     *
     * in = new FileInputStream(source);
     * out = new FileOutputStream(target);
     * ImageUtil.resize(in, out, format, width, height);
     * }
     * finally
     * {
     * if (in != null)
     * {
     *   in.close();
     * }
     *
     * if (out != null)
     * {
     *   out.close();
     * }
     * }
     */
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param file
   * @param blog
   * @param maxWidth
   * @param maxHeigt
   *
   * @return
   */
  private File getFile(File file, Blog blog, int maxWidth, int maxHeigt)
  {
    File resourceDir =
      BlogContext.getInstance().getResourceManager().getDirectory(
          Constants.RESOURCE_ATTACHMENT, blog);
    File imageDir = new File(resourceDir,
                             Constants.RESOURCE_IMAGE + File.separator
                             + maxWidth + "x" + maxHeigt);

    if (!imageDir.exists())
    {
      if (!imageDir.mkdirs())
      {
        throw new RuntimeException("cant create directory "
                                   + imageDir.getPath());
      }
    }

    return new File(imageDir, file.getName() + "." + extension);
  }

  /**
   * Method description
   *
   *
   * @param attachment
   *
   * @return
   */
  private File getFile(Attachment attachment)
  {
    return BlogContext.getInstance().getResourceManager().getFile(attachment);
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
  @Config(Constants.CONFIG_IMAGEEXTENSION)
  private String extension = Constants.DEFAULT_IMAGE_EXTENSION;

  /** Field description */
  @Config(Constants.CONFIG_IMAGEFORMAT)
  private String format = Constants.DEFAULT_IMAGE_FORMAT;

  /** Field description */
  @Config(Constants.CONFIG_IMAGEMIMETYPE)
  private String mimeType = Constants.DEFAULT_IMAGE_MIMETYPE;
}
