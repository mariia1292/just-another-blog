/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.image;

//~--- non-JDK imports --------------------------------------------------------

import sonia.util.ExecUtil;
import sonia.util.Util;

//~--- JDK imports ------------------------------------------------------------

import java.awt.Color;

import java.io.File;
import java.io.IOException;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author sdorra
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
   */
  public ImageMagickFileHandler(String imageMagick)
  {
    this.imageMagick = imageMagick;
  }

  /**
   * Constructs ...
   *
   *
   * @param imageMagick
   * @param timeout
   */
  public ImageMagickFileHandler(String imageMagick, long timeout)
  {
    this.imageMagick = imageMagick;
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
   * @param x1
   * @param y1
   * @param x2
   * @param y2
   * @param width
   * @param height
   *
   * @throws IOException
   */
  public void cropImage(File in, File out, String format, int x1, int y1,
                        int x2, int y2, int width, int height)
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

  //~--- set methods ----------------------------------------------------------

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
   * @throws IOException
   */
  private void excecute(String command) throws IOException
  {
    try
    {
      ExecUtil.process(command, timeout);
    }
    catch (InterruptedException ex)
    {
      throw new IOException(ex.getMessage());
    }
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

    if (logger.isLoggable(Level.FINEST))
    {
      StringBuffer log = new StringBuffer();

      log.append("execute ").append(cmd);
      logger.finest(log.toString());
    }

    return cmd.toString();
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private String imageMagick = "convert";

  /** Field description */
  private long timeout = 10000l;
}
