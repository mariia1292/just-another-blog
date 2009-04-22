/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.util;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogConfiguration;
import sonia.blog.api.app.BlogContext;
import sonia.blog.api.app.Constants;
import sonia.blog.api.app.InstallationListener;

//~--- JDK imports ------------------------------------------------------------

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author sdorra
 */
public class ImageInstallationListener implements InstallationListener
{

  /** Field description */
  private static Logger logger =
    Logger.getLogger(ImageInstallationListener.class.getName());

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param ctx
   */
  public void afterInstallation(BlogContext ctx)
  {

    // do nothing
  }

  /**
   * Method description
   *
   *
   * @param ctx
   */
  public void beforeInstallation(BlogContext ctx)
  {
    BlogConfiguration config = ctx.getConfiguration();
    String method = "internal";

    try
    {
      Process p = Runtime.getRuntime().exec("convert --help");
      int result = p.waitFor();

      if ((result == 1) || (result == 0))
      {
        method = "external";
      }
    }
    catch (Exception ex)
    {
      logger.finest(ex.getMessage());
    }

    if (logger.isLoggable(Level.INFO))
    {
      StringBuffer log = new StringBuffer();

      log.append("configure image converter to ").append(method);
      logger.info(log.toString());
    }

    config.set(Constants.CONFIG_RESIZE_IMAGE, method);
  }
}
