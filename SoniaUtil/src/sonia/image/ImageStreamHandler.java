/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.image;

//~--- non-JDK imports --------------------------------------------------------


//~--- JDK imports ------------------------------------------------------------

import java.awt.Color;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 *
 * @author sdorra
 */
public interface ImageStreamHandler
{


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
  public  void cropImage(InputStream in, OutputStream out, String format, int x1,
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
  public  void scaleImage(InputStream in, OutputStream out, String format,
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
  public  void scaleImage(InputStream in, OutputStream out, String format,
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
  public  void scaleImageFix(InputStream in, OutputStream out, String format,
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
  public  void scaleImageMaxHeight(InputStream in, OutputStream out, String format,
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
  public  void scaleImageMaxWidth(InputStream in, OutputStream out, String format,
          int maxWidth)
          throws IOException;
}
