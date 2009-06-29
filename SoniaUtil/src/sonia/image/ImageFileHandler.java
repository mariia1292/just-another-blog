/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.image;

//~--- JDK imports ------------------------------------------------------------

import java.awt.Color;

import java.io.File;
import java.io.IOException;

/**
 *
 * @author sdorra
 */
public interface ImageFileHandler
{

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
   *
   * @throws IOException
   */
  public void cropImage(File in, File out, String format, int x, int y,
                        int width, int height)
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
  public void scaleImage(File in, File out, String format, Color background,
                         int maxWidth, int maxHeight)
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
  public void scaleImage(File in, File out, String format, int maxWidth,
                         int maxHeight)
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
  public void scaleImageFix(File in, File out, String format, int width,
                            int height)
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
  public void scaleImageMaxHeight(File in, File out, String format,
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
  public void scaleImageMaxWidth(File in, File out, String format, int maxWidth)
          throws IOException;
}
