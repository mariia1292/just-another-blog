/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.util;

//~--- JDK imports ------------------------------------------------------------

import java.awt.Dimension;
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
public class ImageUtil
{

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
  public static void resize(InputStream in, OutputStream out, String format,
                            int width, int height)
          throws IOException
  {
    BufferedImage inputImage = ImageIO.read(in);

    resize(inputImage, out, format, width, height);
  }

  /**
   * Method description
   *
   *
   * @param in
   * @param out
   * @param format
   * @param height
   *
   * @throws IOException
   */
  public static void resizeKeepAspectRatioByHeight(InputStream in,
          OutputStream out, String format, int height)
          throws IOException
  {
    BufferedImage inputImage = ImageIO.read(in);
    double ratio = (double) inputImage.getWidth()
                   / (double) inputImage.getHeight();
    int width = (int) (height * ratio);

    resize(inputImage, out, format, width, height);
  }

  /**
   * Method description
   *
   *
   * @param in
   * @param out
   * @param format
   * @param width
   *
   * @throws IOException
   */
  public static void resizeKeepAspectRatioByWidth(InputStream in,
          OutputStream out, String format, int width)
          throws IOException
  {
    BufferedImage inputImage = ImageIO.read(in);
    double ratio = (double) inputImage.getHeight() / (double) inputImage.getWidth();
    int height = (int) (width * ratio);

    resize(inputImage, out, format, width, height);
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
  private static void resize(BufferedImage inputImage, OutputStream out,
                             String format, int width, int height)
          throws IOException
  {
    Image scaledImage = inputImage.getScaledInstance(width, height,
                          Image.SCALE_SMOOTH);
    BufferedImage outputImage = new BufferedImage(width, height,
                                  BufferedImage.TYPE_INT_RGB);

    outputImage.getGraphics().drawImage(scaledImage, 0, 0, width, height, null);
    ImageIO.write(outputImage, format, out);
  }
  
  public static int getWidth( InputStream in ) throws IOException
  {
    BufferedImage img = ImageIO.read(in);
    return img.getWidth();
  }
  
  public static int getHeight( InputStream in ) throws IOException
  {
    BufferedImage img = ImageIO.read(in);
    return img.getHeight();
  }
  
  public static Dimension getDimension( InputStream in ) throws IOException {
    BufferedImage img = ImageIO.read(in);
    return new Dimension( img.getWidth(), img.getHeight() );
  }
  
}
