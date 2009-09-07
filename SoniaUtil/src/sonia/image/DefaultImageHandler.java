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

//~--- JDK imports ------------------------------------------------------------

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.imageio.ImageIO;

/**
 *
 * @author Sebastian Sdorra
 */
public class DefaultImageHandler implements ImageStreamHandler, ImageFileHandler
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
   * @param x
   * @param y
   * @param width
   * @param height
   *
   * @throws IOException
   */
  public void cropImage(InputStream in, OutputStream out, String format, int x,
                        int y, int width, int height)
          throws IOException
  {
    BufferedImage image = ImageIO.read(in);

    ImageIO.write(image.getSubimage(x, y, width, height), format, out);
  }

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
    FileInputStream fis = null;
    FileOutputStream fos = null;

    try
    {
      fis = new FileInputStream(in);
      fos = new FileOutputStream(out);
      cropImage(fis, fos, format, x, y, width, height);
    }
    finally
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
    FileInputStream fis = null;
    FileOutputStream fos = null;

    try
    {
      fis = new FileInputStream(in);
      fos = new FileOutputStream(out);
      scaleImage(fis, fos, format, background, maxWidth, maxHeight);
    }
    finally
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
    FileInputStream fis = null;
    FileOutputStream fos = null;

    try
    {
      fis = new FileInputStream(in);
      fos = new FileOutputStream(out);
      scaleImage(fis, fos, format, maxWidth, maxHeight);
    }
    finally
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
   * @param width
   * @param height
   *
   * @throws IOException
   */
  public void scaleImageFix(File in, File out, String format, int width,
                            int height)
          throws IOException
  {
    FileInputStream fis = null;
    FileOutputStream fos = null;

    try
    {
      fis = new FileInputStream(in);
      fos = new FileOutputStream(out);
      scaleImageFix(fis, fos, format, width, height);
    }
    finally
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
   * @param maxHeight
   *
   * @throws IOException
   */
  public void scaleImageMaxHeight(File in, File out, String format,
                                  int maxHeight)
          throws IOException
  {
    FileInputStream fis = null;
    FileOutputStream fos = null;

    try
    {
      fis = new FileInputStream(in);
      fos = new FileOutputStream(out);
      scaleImageMaxHeight(fis, fos, format, maxHeight);
    }
    finally
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
    FileInputStream fis = null;
    FileOutputStream fos = null;

    try
    {
      fis = new FileInputStream(in);
      fos = new FileOutputStream(out);
      scaleImageMaxWidth(fis, fos, format, maxWidth);
    }
    finally
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
  public Dimension getDimension(InputStream in) throws IOException
  {
    BufferedImage inputImage = ImageIO.read(in);
    int width = inputImage.getWidth();
    int height = inputImage.getHeight();

    return new Dimension(width, height);
  }

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
    FileInputStream fis = null;

    try
    {
      fis = new FileInputStream(in);
      d = getDimension(fis);
    }
    finally
    {
      if (fis != null)
      {
        fis.close();
      }
    }

    return d;
  }

  //~--- methods --------------------------------------------------------------

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
