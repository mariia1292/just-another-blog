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



package sonia.image;

//~--- non-JDK imports --------------------------------------------------------

import sonia.util.ExecUtil;
import sonia.util.Util;

//~--- JDK imports ------------------------------------------------------------

import java.awt.Color;
import java.awt.Dimension;

import java.io.File;
import java.io.IOException;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Sebastian Sdorra
 */
public class ImageMagickFileHandler implements ImageFileHandler
{

  /** Field description */
  private static Logger logger =
    Logger.getLogger(ImageMagickFileHandler.class.getName());

  //~--- constructors ---------------------------------------------------------

  /**
   * Constructs ...
   *
   */
  public ImageMagickFileHandler() {}

  /**
   * Constructs ...
   *
   *
   * @param imageMagick
   * @param identify
   */
  public ImageMagickFileHandler(String imageMagick, String identify)
  {
    this.imageMagick = imageMagick;
    this.identify = identify;
  }

  /**
   * Constructs ...
   *
   *
   * @param imageMagick
   * @param identify
   * @param timeout
   */
  public ImageMagickFileHandler(String imageMagick, String identify,
                                long timeout)
  {
    this.imageMagick = imageMagick;
    this.identify = identify;
    this.timeout = timeout;
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param in
   * @param out
   * @param format
   * @param x
   * @param y
   * @param width
   * @param height
   *
   * @throws IOException
   */
  public void cropImage(File in, File out, String format, int x, int y,
                        int width, int height)
          throws IOException
  {
    StringBuffer cmd = new StringBuffer();

    cmd.append(imageMagick).append(" ").append(in.getPath()).append(" -crop ");
    cmd.append(x).append("x").append(y).append("+").append(width).append("+");
    cmd.append(height).append(" ").append(out.getPath());
    excecute(cmd.toString());
  }

  /**
   * Method description
   *
   *
   * @param in
   * @param out
   * @param format
   * @param background
   * @param maxWidth
   * @param maxHeight
   *
   * @throws IOException
   */
  public void scaleImage(File in, File out, String format, Color background,
                         int maxWidth, int maxHeight)
          throws IOException
  {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  /**
   * Method description
   *
   *
   * @param in
   * @param out
   * @param format
   * @param maxWidth
   * @param maxHeight
   *
   * @throws IOException
   */
  public void scaleImage(File in, File out, String format, int maxWidth,
                         int maxHeight)
          throws IOException
  {
    excecute(getScaleCommand(in, out, format, maxWidth, maxHeight, false));
  }

  /**
   * Method description
   *
   *
   * @param in
   * @param out
   * @param format
   * @param width
   * @param height
   *
   * @throws IOException
   */
  public void scaleImageFix(File in, File out, String format, int width,
                            int height)
          throws IOException
  {
    excecute(getScaleCommand(in, out, format, width, height, true));
  }

  /**
   * Method description
   *
   *
   * @param in
   * @param out
   * @param format
   * @param maxHeight
   *
   * @throws IOException
   */
  public void scaleImageMaxHeight(File in, File out, String format,
                                  int maxHeight)
          throws IOException
  {
    excecute(getScaleCommand(in, out, format, -1, maxHeight, false));
  }

  /**
   * Method description
   *
   *
   * @param in
   * @param out
   * @param format
   * @param maxWidth
   *
   * @throws IOException
   */
  public void scaleImageMaxWidth(File in, File out, String format, int maxWidth)
          throws IOException
  {
    excecute(getScaleCommand(in, out, format, maxWidth, -1, false));
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param in
   *
   * @return
   *
   * @throws IOException
   */
  public Dimension getDimension(File in) throws IOException
  {
    Dimension d = null;
    StringBuffer cmd = new StringBuffer();

    cmd.append(identify).append(" -format %wx%h ");
    cmd.append(in.getAbsoluteFile());

    String dimensionString = excecute(cmd.toString());
    int x = dimensionString.indexOf("x");

    if (x > 0)
    {
      try
      {
        int width = Integer.parseInt(dimensionString.substring(0, x));
        int height = Integer.parseInt(dimensionString.substring(x + 1));

        d = new Dimension(width, height);
      }
      catch (NumberFormatException ex)
      {
        throw new IOException(ex.getMessage());
      }
    }
    else
    {
      throw new IOException("identify command returned " + dimensionString);
    }

    return d;
  }

  //~--- set methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param identify
   */
  public void setIdentify(String identify)
  {
    this.identify = identify;
  }

  /**
   * Method description
   *
   *
   * @param imageMagick
   */
  public void setImageMagick(String imageMagick)
  {
    this.imageMagick = imageMagick;
  }

  /**
   * Method description
   *
   *
   * @param timeout
   */
  public void setTimeout(long timeout)
  {
    this.timeout = timeout;
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param command
   *
   *
   * @return
   * @throws IOException
   */
  private String excecute(String command) throws IOException
  {
    String output = null;

    if (logger.isLoggable(Level.FINEST))
    {
      StringBuffer log = new StringBuffer();

      log.append("execute ").append(command);
      logger.finest(log.toString());
    }

    try
    {
      output = ExecUtil.processWithOutput(command, timeout);

      if (logger.isLoggable(Level.FINEST))
      {
        StringBuffer log = new StringBuffer();

        log.append("command result: ").append(output);
        logger.finest(log.toString());
      }
    }
    catch (InterruptedException ex)
    {
      throw new IOException(ex.getMessage());
    }

    return output;
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param in
   * @param out
   * @param format
   * @param width
   * @param height
   * @param fix
   *
   * @return
   */
  private String getScaleCommand(File in, File out, String format, int width,
                                 int height, boolean fix)
  {
    StringBuffer cmd = new StringBuffer();

    cmd.append(imageMagick).append(" -resize ");

    if (width > 0)
    {
      cmd.append(width);
    }

    if (height > 0)
    {
      cmd.append("x");
      cmd.append(height);
    }

    if (fix)
    {
      cmd.append("!");
    }

    cmd.append(" ").append(in.getAbsolutePath()).append(" ");

    if (Util.hasContent(format))
    {
      cmd.append(format).append(":");
    }

    cmd.append(out.getAbsolutePath());

    return cmd.toString();
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private String identify = "identify";

  /** Field description */
  private String imageMagick = "convert";

  /** Field description */
  private long timeout = 10000l;
}
