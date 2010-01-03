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

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

//~--- JDK imports ------------------------------------------------------------

import java.awt.Dimension;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 *
 * @author Sebastian Sdorra
 */
public abstract class ImageStreamHandlerTestBase
{

  /**
   * Method description
   *
   *
   * @return
   */
  protected abstract String getFormat();

  /**
   * Method description
   *
   *
   * @return
   */
  protected abstract ImageStreamHandler getImageStreamHandler();

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   */
  @Test
  public void tearDownTest()
  {
    if ((outputFile != null) && outputFile.exists())
    {
      assertTrue(outputFile.delete());
    }
  }

  /**
   * Method description
   *
   */
  @Test
  public void testCropImage()
  {
    ImageStreamHandler ish = getImageStreamHandler();

    assertNotNull(ish);

    try
    {
      ish.cropImage(input, output, getFormat(), 0, 0, 180, 60);

      Dimension d = ish.getDimension(getInputStreamOfOutputFile());

      assertTrue(d.width == 180);
      assertTrue(d.height == 60);
    }
    catch (IOException ex)
    {
      fail(ex.getLocalizedMessage());
    }
  }

  /**
   * Method description
   *
   */
  @Test
  public void testGetDimension()
  {
    ImageStreamHandler ish = getImageStreamHandler();

    assertNotNull(ish);

    try
    {
      Dimension d = ish.getDimension(input);

      assertTrue(d.width == 360);
      assertTrue(d.height == 120);
    }
    catch (IOException ex)
    {
      fail(ex.getLocalizedMessage());
    }
  }

  /**
   * Method description
   *
   */
  @Test
  public void testScaleImageFix()
  {
    ImageStreamHandler ish = getImageStreamHandler();

    assertNotNull(ish);

    try
    {
      ish.scaleImageFix(input, output, getFormat(), 32, 32);

      Dimension d = ish.getDimension(getInputStreamOfOutputFile());

      assertTrue(d.width == 32);
      assertTrue(d.height == 32);
    }
    catch (IOException ex)
    {
      fail(ex.getLocalizedMessage());
    }
  }

  /**
   * Method description
   *
   */
  @Test
  public void testScaleImageHeight()
  {
    ImageStreamHandler ish = getImageStreamHandler();

    assertNotNull(ish);

    try
    {
      ish.scaleImage(input, output, getFormat(), 320, 60);

      Dimension d = ish.getDimension(getInputStreamOfOutputFile());

      assertTrue(d.width == 180);
      assertTrue(d.height == 60);
    }
    catch (IOException ex)
    {
      fail(ex.getLocalizedMessage());
    }
  }

  /**
   * Method description
   *
   */
  @Test
  public void testScaleImageMaxHeight()
  {
    ImageStreamHandler ish = getImageStreamHandler();

    assertNotNull(ish);

    try
    {
      ish.scaleImageMaxHeight(input, output, getFormat(), 60);

      Dimension d = ish.getDimension(getInputStreamOfOutputFile());

      assertTrue(d.width == 180);
      assertTrue(d.height == 60);
    }
    catch (IOException ex)
    {
      fail(ex.getLocalizedMessage());
    }
  }

  /**
   * Method description
   *
   */
  @Test
  public void testScaleImageMaxWidth()
  {
    ImageStreamHandler ish = getImageStreamHandler();

    assertNotNull(ish);

    try
    {
      ish.scaleImageMaxWidth(input, output, getFormat(), 180);

      Dimension d = ish.getDimension(getInputStreamOfOutputFile());

      assertTrue(d.width == 180);
      assertTrue(d.height == 60);
    }
    catch (IOException ex)
    {
      fail(ex.getLocalizedMessage());
    }
  }

  /**
   * Method description
   *
   */
  @Test
  public void testScaleImageWidth()
  {
    ImageStreamHandler ish = getImageStreamHandler();

    assertNotNull(ish);

    try
    {
      ish.scaleImage(input, output, getFormat(), 180, 180);

      Dimension d = ish.getDimension(getInputStreamOfOutputFile());

      assertTrue(d.width == 180);
      assertTrue(d.height == 60);
    }
    catch (IOException ex)
    {
      fail(ex.getLocalizedMessage());
    }
  }

  //~--- set methods ----------------------------------------------------------

  /**
   * Method description
   *
   */
  @Before
  public void setUpTest()
  {
    input = ImageStreamHandlerTestBase.class.getResourceAsStream(
      "/sonia/image/image-1.jpg");

    try
    {
      outputFile = File.createTempFile("sonia-junit-out-", ".tmp");
      output = new FileOutputStream(outputFile);
    }
    catch (IOException ex)
    {
      fail(ex.getLocalizedMessage());
    }
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   *
   * @throws FileNotFoundException
   */
  private InputStream getInputStreamOfOutputFile() throws FileNotFoundException
  {
    return new FileInputStream(outputFile);
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private InputStream input;

  /** Field description */
  private OutputStream output;

  /** Field description */
  private File outputFile;
}
