/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.image;

//~--- non-JDK imports --------------------------------------------------------

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sonia.util.Util;

import static org.junit.Assert.*;

//~--- JDK imports ------------------------------------------------------------

import java.awt.Dimension;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 *
 * @author sdorra
 */
public abstract class ImageFileHandlerTestBase
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
  protected abstract ImageFileHandler getImageFileHandler();

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   */
  @After
  public void tearDownTest()
  {
    if ((input != null) && input.exists())
    {
      assertTrue(input.delete());
    }

    if ((output != null) && output.exists())
    {
      assertTrue(output.delete());
    }
  }

  /**
   * Method description
   *
   */
  @Test
  public void testCropImage()
  {
    ImageFileHandler ifh = getImageFileHandler();

    assertNotNull(ifh);

    try
    {
      ifh.cropImage(input, output, getFormat(), 0, 0, 180, 60);

      Dimension d = ifh.getDimension(output);

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
    ImageFileHandler ifh = getImageFileHandler();

    assertNotNull(ifh);

    try
    {
      Dimension d = ifh.getDimension(input);

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
    ImageFileHandler ifh = getImageFileHandler();

    assertNotNull(ifh);

    try
    {
      ifh.scaleImageFix(input, output, getFormat(), 32, 32);

      Dimension d = ifh.getDimension(output);

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
    ImageFileHandler ifh = getImageFileHandler();

    assertNotNull(ifh);

    try
    {
      ifh.scaleImage(input, output, getFormat(), 320, 60);

      Dimension d = ifh.getDimension(output);

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
    ImageFileHandler ifh = getImageFileHandler();

    assertNotNull(ifh);

    try
    {
      ifh.scaleImageMaxHeight(input, output, getFormat(), 60);

      Dimension d = ifh.getDimension(output);

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
    ImageFileHandler ifh = getImageFileHandler();

    assertNotNull(ifh);

    try
    {
      ifh.scaleImageMaxWidth(input, output, getFormat(), 180);

      Dimension d = ifh.getDimension(output);

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
    ImageFileHandler ifh = getImageFileHandler();

    assertNotNull(ifh);

    try
    {
      ifh.scaleImage(input, output, getFormat(), 180, 180);

      Dimension d = ifh.getDimension(output);

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
    try
    {
      input = File.createTempFile("sonia-junit-in-", ".tmp");
      assertNotNull(input);
      createInputFile();
      output = File.createTempFile("sonia-junit-out-", ".tmp");
      assertNotNull(output);
    }
    catch (IOException ex)
    {
      fail(ex.getLocalizedMessage());
    }
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   */
  private void createInputFile()
  {
    InputStream in = null;
    FileOutputStream fos = null;

    try
    {
      in = ImageFileHandlerTestBase.class.getResourceAsStream(
        "/sonia/image/image-1.jpg");
      fos = new FileOutputStream(input);
      Util.copy(in, fos);
    }
    catch (IOException ex)
    {
      fail(ex.getLocalizedMessage());
    }
    finally
    {
      try
      {
        if (in != null)
        {
          in.close();
        }

        if (fos != null)
        {
          fos.close();
        }
      }
      catch (IOException ex)
      {
        fail(ex.getLocalizedMessage());
      }
    }

    assertTrue(input.length() > 0);
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  protected File input;

  /** Field description */
  protected File output;
}
