/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.image;

//~--- non-JDK imports --------------------------------------------------------

import sonia.util.ServiceLocator;

/**
 *
 * @author sdorra
 */
public class ImageHandler
{

  /** Field description */
  private static ImageFileHandler fileHandler;

  /** Field description */
  private static ImageStreamHandler streamHandler;

  //~--- constructors ---------------------------------------------------------

  /**
   * Constructs ...
   *
   */
  private ImageHandler() {}

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  public static ImageFileHandler getImageFileHandler()
  {
    if (fileHandler == null)
    {
      fileHandler = ServiceLocator.locateService(ImageFileHandler.class,
              new DefaultImageHandler());
    }

    return fileHandler;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public static ImageStreamHandler getImageStreamHandler()
  {
    if (streamHandler == null)
    {
      streamHandler = ServiceLocator.locateService(ImageStreamHandler.class,
              new DefaultImageHandler());
    }

    return streamHandler;
  }
}
