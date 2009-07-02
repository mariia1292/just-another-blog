/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.image;

/**
 *
 * @author sdorra
 */
public class DefaultImageStreamHandlerTest extends ImageStreamHandlerTestBase
{

  /** Field description */
  public static final String FORMAT = "png";

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  @Override
  protected String getFormat()
  {
    return FORMAT;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  @Override
  protected ImageStreamHandler getImageStreamHandler()
  {
    return new DefaultImageHandler();
  }
}
