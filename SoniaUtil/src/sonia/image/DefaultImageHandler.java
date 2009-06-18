/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.image;

//~--- JDK imports ------------------------------------------------------------

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


import javax.imageio.ImageIO;

/**
 *
 * @author sdorra
 */
public class DefaultImageHandler extends ImageHandler
{

  /** Field description */
  public static int DEFAULT_SCALETYPE = Image.SCALE_SMOOTH;

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
  @Override
  public void cropImage(InputStream in, OutputStream out, String format,
                        int x1, int y1, int x2, int y2, int width, int height)
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
  @Override
  public void scaleImage(InputStream in, OutputStream out, String format,
                         Color background, int maxWidth, int maxHeight)
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
  @Override
  public void scaleImage(InputStream in, OutputStream out, String format,
                         int maxWidth, int maxHeight)
          throws IOException
  {
    BufferedImage image = ImageIO.read(in);
    double width = image.getWidth();
    double height = image.getHeight();

    if ((width < maxWidth) && (height < maxHeight))
    {
      ImageIO.write(image, format, out);
    }
    else
    {
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

      scale(image, out, format, (int) width, (int) height);
    }
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
  @Override
  public void scaleImageFix(InputStream in, OutputStream out, String format,
                            int width, int height)
          throws IOException
  {
    BufferedImage inputImage = ImageIO.read(in);

    scale(inputImage, out, format, width, height);
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
  @Override
  public void scaleImageMaxHeight(InputStream in, OutputStream out,
                                  String format, int maxHeight)
          throws IOException
  {
    BufferedImage inputImage = ImageIO.read(in);
    double ratio = (double) inputImage.getWidth()
                   / (double) inputImage.getHeight();
    int width = (int) (maxHeight * ratio);

    scale(inputImage, out, format, width, maxHeight);
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
  @Override
  public void scaleImageMaxWidth(InputStream in, OutputStream out,
                                 String format, int maxWidth)
          throws IOException
  {
    BufferedImage inputImage = ImageIO.read(in);
    double ratio = (double) inputImage.getHeight()
                   / (double) inputImage.getWidth();
    int height = (int) (maxWidth * ratio);

    scale(inputImage, out, format, maxWidth, height);
  }

  /**
   * Method description
   *
   *
   * @param inputImage
   * @param out
   * @param format
   * @param width
   * @param height
   *
   * @throws IOException
   */
  private void scale(BufferedImage inputImage, OutputStream out, String format,
                     int width, int height)
          throws IOException
  {
    Image scaledImage = inputImage.getScaledInstance(width, height,
                          Image.SCALE_SMOOTH);
    BufferedImage outputImage = new BufferedImage(width, height,
                                  BufferedImage.TYPE_INT_RGB);
    Graphics g = outputImage.getGraphics();

    g.drawImage(scaledImage, 0, 0, width, height, null);
    g.dispose();
    ImageIO.write(outputImage, format, out);
  }
}
