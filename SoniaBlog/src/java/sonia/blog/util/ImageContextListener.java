/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.util;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogConfiguration;
import sonia.blog.api.app.BlogContext;
import sonia.blog.api.app.Constants;

import sonia.image.ImageFileHandler;
import sonia.image.ImageHandler;
import sonia.image.ImageMagickFileHandler;

import sonia.util.ExecUtil;
import sonia.util.Util;

//~--- JDK imports ------------------------------------------------------------

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 *
 * @author sdorra
 */
public class ImageContextListener implements ServletContextListener
{

  /** Field description */
  private static Logger logger =
    Logger.getLogger(ImageContextListener.class.getName());

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param sce
   */
  public void contextDestroyed(ServletContextEvent sce)
  {

    // do nothing
  }

  /**
   * Method description
   *
   *
   * @param sce
   */
  public void contextInitialized(ServletContextEvent sce)
  {
    ImageFileHandler imageHandler = null;
    BlogContext ctx = BlogContext.getInstance();
    BlogConfiguration config = ctx.getConfiguration();

    System.out.println("----");

    for (String key : config.keySet())
    {
      System.out.println(key + ": " + config.getString(key));
    }

    System.out.println(config.getString(Constants.CONFIG_RESIZE_IMAGE));

    try
    {
      if (Util.isBlank(config.getString(Constants.CONFIG_RESIZE_IMAGE)))
      {
        int result = ExecUtil.process("convert --help", 3000l);

        if ((result == 1) || (result == 0))
        {
          imageHandler = new ImageMagickFileHandler();
        }
      }
      else
      {
        imageHandler = (ImageFileHandler) Class.forName(
          config.getString(Constants.CONFIG_RESIZE_IMAGE)).newInstance();

        if (imageHandler instanceof ImageMagickFileHandler)
        {
          ImageMagickFileHandler imfh = (ImageMagickFileHandler) imageHandler;
          String path = config.getString(Constants.CONFIG_COMMAND_RESIZE_IMAGE);

          if (Util.hasContent(path))
          {
            imfh.setImageMagick(path);
          }

          Long timeout = config.getLong(Constants.CONFIG_COMMAND_TIMEOUT);

          if (timeout != null)
          {
            imfh.setTimeout(timeout);
          }
        }
      }
    }
    catch (Exception ex)
    {
      if (logger.isLoggable(Level.WARNING))
      {
        logger.log(Level.WARNING, null, ex);
      }
    }

    if (imageHandler == null)
    {
      imageHandler = ImageHandler.getImageFileHandler();
    }

    if (logger.isLoggable(Level.INFO))
    {
      StringBuffer log = new StringBuffer();

      log.append("configure image converter to ").append(
          imageHandler.getClass().getName());
      logger.info(log.toString());
    }

    ctx.setImageHandler(imageHandler);
  }
}
