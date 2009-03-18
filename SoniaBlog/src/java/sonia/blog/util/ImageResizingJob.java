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

//~--- JDK imports ------------------------------------------------------------

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author sdorra
 */
public class ImageResizingJob extends BlogJob
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
    this.width = width;
    this.height = height;
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
        if (logger.isLoggable(Level.INFO))
        {
          logger.info("resize image " + source.getName() + " (resolution "
                      + width + "x" + height + ")");
        }

        in = new FileInputStream(source);
        out = new FileOutputStream(target);
        ImageUtil.resize(in, out, format, width, height);
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

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private Blog blog;

  /** Field description */
  private String format;

  /** Field description */
  private int height;

  /** Field description */
  private File source;

  /** Field description */
  private File target;

  /** Field description */
  private int width;
}
