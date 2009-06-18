/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.image;

//~--- non-JDK imports --------------------------------------------------------

import sonia.util.ServiceLocator;

//~--- JDK imports ------------------------------------------------------------

import java.awt.Color;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 *
 * @author sdorra
 */
public abstract class ImageHandler
{

  /** Field description */
  private static ImageHandler instance;

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  public static ImageHandler getInstance()
  {
    if (instance == null)
    {
      instance = ServiceLocator.locateService(ImageHandler.class,
              new DefaultImageHandler());
    }

    return instance;
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
   *
   * @throws IOException
   */
  public abstract void cropImage(InputStream in, OutputStream out, String format, int x1,
                                 int y1, int x2, int y2, int width, int height)
          throws IOException;

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
   *
   * @throws IOException
   */
  public abstract void scaleImage(InputStream in, OutputStream out, String format,
                                  Color background, int maxWidth, int maxHeight)
          throws IOException;

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
   *
   * @throws IOException
   */
  public abstract void scaleImage(InputStream in, OutputStream out, String format,
                                  int maxWidth, int maxHeight)
          throws IOException;

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
   *
   * @throws IOException
   */
  public abstract void scaleImageFix(InputStream in, OutputStream out, String format,
                                     int width, int height)
          throws IOException;

  /**
   * Method description
   *
   *
   * @param in
   * @param out
   * @param format
   * @param maxHeight
   *
   *
   * @throws IOException
   */
  public abstract void scaleImageMaxHeight(InputStream in, OutputStream out, String format,
          int maxHeight)
          throws IOException;

  /**
   * Method description
   *
   *
   * @param in
   * @param out
   * @param format
   * @param maxWidth
   *
   *
   * @throws IOException
   */
  public abstract void scaleImageMaxWidth(InputStream in, OutputStream out, String format,
          int maxWidth)
          throws IOException;
}
