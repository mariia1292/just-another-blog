/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.util;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogContext;
import sonia.blog.api.app.BlogJob;
import sonia.blog.entity.Blog;

import sonia.image.ImageHandler;

import sonia.jobqueue.JobException;

import sonia.util.Util;

//~--- JDK imports ------------------------------------------------------------

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author sdorra
 */
public class ImageHandlerJob implements BlogJob
{

  /** Field description */
  public static final String DESCRIPTION = "handles images";

  /** Field description */
  public static final String NAME = "ImageHandlerJob";

  /** Field description */
  private static final long serialVersionUID = 1734002367508123460L;

  /** Field description */
  private static Logger logger =
    Logger.getLogger(ImageHandlerJob.class.getName());

  //~--- constructors ---------------------------------------------------------

  /**
   * Constructs ...
   *
   *
   * @param blog
   * @param in
   * @param out
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
   */
  public ImageHandlerJob(Blog blog, File in, File out, Long id, String type,
                         String format, String color, int width, int height,
                         int x1, int x2, int y1, int y2)
  {
    this.blog = blog;
    this.in = in;
    this.out = out;
    this.id = id;
    this.type = type;
    this.format = format;
    this.color = color;
    this.width = width;
    this.height = height;
    this.x1 = x1;
    this.x2 = x2;
    this.y1 = y1;
    this.y2 = y2;
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
    FileInputStream fis = null;
    FileOutputStream fos = null;

    try
    {
      fis = new FileInputStream(in);
      fos = new FileOutputStream(out);

      ImageHandler imageHandler = BlogContext.getInstance().getImageHandler();

      if (Util.isBlank(type) || type.equalsIgnoreCase("relative"))
      {
        printRelativeImage(imageHandler, fis, fos);
      }
      else if (type.equalsIgnoreCase("thumb"))
      {
        printThumbImage(imageHandler, fis, fos);
      }
      else if (type.equalsIgnoreCase("fix"))
      {
        printFixImage(imageHandler, fis, fos);
      }
    }
    catch (Exception ex)
    {
      logger.log(Level.SEVERE, null, ex);
      if ( ! out.delete() )
      {
        StringBuffer log = new StringBuffer();
        log.append("could not delete broken image ").append( out.getPath() );
        logger.severe( log.toString() );
      }

      throw new JobException(ex);
    }
    finally
    {
      try
      {
        if (fis != null)
        {
          fis.close();
        }

        if (fos != null)
        {
          fos.close();
        }
      }
      catch (IOException ex)
      {
        logger.log(Level.SEVERE, null, ex);
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
    return DESCRIPTION;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String getName()
  {
    return NAME;
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   *
   * @param imageHandler
   * @param fis
   * @param fos
   *
   * @throws IOException
   */
  private void printFixImage(ImageHandler imageHandler, FileInputStream fis,
                             FileOutputStream fos)
          throws IOException
  {
    if ((width < 0) || (height < 0))
    {
      throw new IllegalArgumentException("width or height are null");
    }

    imageHandler.scaleImageFix(fis, fos, format, width, height);
  }

  /**
   * Method description
   *
   *
   * @param imageHandler
   * @param fis
   * @param fos
   *
   * @throws IOException
   */
  private void printRelativeImage(ImageHandler imageHandler,
                                  FileInputStream fis, FileOutputStream fos)
          throws IOException
  {
    if (width < 0)
    {
      width = blog.getImageWidth();
    }

    if (height < 0)
    {
      height = blog.getImageHeight();
    }

    imageHandler.scaleImage(fis, fos, format, width, height);
  }

  /**
   * Method description
   *
   *
   * @param imageHandler
   * @param fis
   * @param fos
   *
   * @throws IOException
   */
  private void printThumbImage(ImageHandler imageHandler, FileInputStream fis,
                               FileOutputStream fos)
          throws IOException
  {
    if (width < 0)
    {
      width = blog.getThumbnailWidth();
    }

    if (height < 0)
    {
      height = blog.getThumbnailHeight();
    }

    imageHandler.scaleImage(fis, fos, format, width, height);
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private Blog blog;

  /** Field description */
  private String color;

  /** Field description */
  private String format;

  /** Field description */
  private int height;

  /** Field description */
  private Long id;

  /** Field description */
  private File in;

  /** Field description */
  private File out;

  /** Field description */
  private String type;

  /** Field description */
  private int width;

  /** Field description */
  private int x1;

  /** Field description */
  private int x2;

  /** Field description */
  private int y1;

  /** Field description */
  private int y2;
}
