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



package sonia.blog.util;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogContext;
import sonia.blog.entity.Blog;

import sonia.image.ImageFileHandler;

import sonia.util.Util;

//~--- JDK imports ------------------------------------------------------------

import java.io.File;
import java.io.IOException;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Sebastian Sdorra
 */
public class ImageHandlerJob implements Runnable
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
   * @param type
   * @param format
   * @param color
   * @param width
   * @param height
   * @param x
   * @param y
   */
  public ImageHandlerJob(Blog blog, File in, File out, String type,
                         String format, String color, int width, int height,
                         int x, int y)
  {
    this.blog = blog;
    this.in = in;
    this.out = out;
    this.type = type;
    this.format = format;
    this.color = color;
    this.width = width;
    this.height = height;
    this.x = x;
    this.y = y;
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   */
  public void run()
  {
    if (!out.exists())
    {
      try
      {
        ImageFileHandler imageHandler =
          BlogContext.getInstance().getImageFileHandler();

        if (logger.isLoggable(Level.FINEST))
        {
          StringBuffer msg = new StringBuffer();

          msg.append("resize image ").append(in.getPath());
          msg.append(" to file ").append(out.getPath());
          logger.finest(msg.toString());
        }

        Thread.sleep(3l * 1000l);

        if (Util.isBlank(type) || type.equalsIgnoreCase("relative"))
        {
          printRelativeImage(imageHandler);
        }
        else if (type.equalsIgnoreCase("thumb"))
        {
          printThumbImage(imageHandler);
        }
        else if (type.equalsIgnoreCase("fix"))
        {
          printFixImage(imageHandler);
        }
        else if (type.equalsIgnoreCase("crop"))
        {
          printCropImage(imageHandler);
        }
      }
      catch (Exception ex)
      {
        logger.log(Level.SEVERE, null, ex);

        if (!out.delete())
        {
          StringBuffer log = new StringBuffer();

          log.append("could not delete broken image ").append(out.getPath());
          logger.severe(log.toString());
        }

        logger.log(Level.SEVERE, null, ex);
      }
    }
    else if (logger.isLoggable(Level.FINE))
    {
      StringBuffer msg = new StringBuffer();

      msg.append("output file ").append(out.getPath());
      msg.append(" allready exists");
      logger.fine(msg.toString());
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
   * @param imageHandler
   *
   * @throws IOException
   */
  private void printCropImage(ImageFileHandler imageHandler) throws IOException
  {
    imageHandler.cropImage(in, out, format, x, y, width, height);
  }

  /**
   * Method description
   *
   *
   *
   * @param imageHandler
   *
   * @throws IOException
   */
  private void printFixImage(ImageFileHandler imageHandler) throws IOException
  {
    if ((width < 0) || (height < 0))
    {
      throw new IllegalArgumentException("width or height are null");
    }

    imageHandler.scaleImageFix(in, out, format, width, height);
  }

  /**
   * Method description
   *
   *
   * @param imageHandler
   *
   * @throws IOException
   */
  private void printRelativeImage(ImageFileHandler imageHandler)
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

    imageHandler.scaleImage(in, out, format, width, height);
  }

  /**
   * Method description
   *
   *
   * @param imageHandler
   *
   * @throws IOException
   */
  private void printThumbImage(ImageFileHandler imageHandler) throws IOException
  {
    if (width < 0)
    {
      width = blog.getThumbnailWidth();
    }

    if (height < 0)
    {
      height = blog.getThumbnailHeight();
    }

    imageHandler.scaleImage(in, out, format, width, height);
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
  private File in;

  /** Field description */
  private File out;

  /** Field description */
  private String type;

  /** Field description */
  private int width;

  /** Field description */
  private int x;

  /** Field description */
  private int y;
}
