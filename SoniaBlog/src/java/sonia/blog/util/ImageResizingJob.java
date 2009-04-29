/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.util;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogJob;
import sonia.blog.entity.Blog;

import sonia.jobqueue.JobException;

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

/**
 *
 * @author sdorra
 */
public class ImageResizingJob implements BlogJob
{

  /** Field description */
  private static final long serialVersionUID = 1697766349549868524L;

  /** Field description */
  private static Logger logger =
    Logger.getLogger(ImageResizingJob.class.getName());

  //~--- constructors ---------------------------------------------------------

  /**
   * Constructs ...
   *
   *
   * @param blog
   * @param source
   * @param target
   * @param format
   * @param width
   * @param height
   */
  public ImageResizingJob(Blog blog, File source, File target, String format,
                          int width, int height)
  {
    this.blog = blog;
    this.source = source;
    this.target = target;
    this.format = format;
    this.maxWidth = width;
    this.maxHeight = height;
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @throws JobException
   */
  public void excecute() throws JobException
  {
    if (!target.exists())
    {
      InputStream in = null;
      OutputStream out = null;

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

          if (logger.isLoggable(Level.INFO))
          {
            StringBuffer log = new StringBuffer();

            log.append("resize image ").append(source.getName());
            log.append(" (resolution ").append((int) width);
            log.append("x").append((int) height).append(")");
            logger.info(log.toString());
          }

          in = new FileInputStream(source);
          out = new FileOutputStream(target);
          ImageUtil.resize(in, out, format, (int) width, (int) height);
        }
      }
      catch (Exception ex)
      {
        throw new JobException(ex);
      }
      finally
      {
        try
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
        catch (Exception ex)
        {
          logger.log(Level.WARNING, null, ex);
        }
      }
    }
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  public Blog getBlog()
  {
    return blog;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String getDescription()
  {
    return "resize image";
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String getName()
  {
    return "ImageResize";
  }

  //~--- methods --------------------------------------------------------------

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

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  protected Blog blog;

  /** Field description */
  protected String format;

  /** Field description */
  protected int maxHeight;

  /** Field description */
  protected int maxWidth;

  /** Field description */
  protected File source;

  /** Field description */
  protected File target;
}
