/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.logging;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogContext;
import sonia.blog.api.app.Constants;

import sonia.logging.FileHandler;

//~--- JDK imports ------------------------------------------------------------

import java.util.logging.LogManager;

/**
 *
 * @author sdorra
 */
public class LoggingHandler extends FileHandler
{

  /**
   * Method description
   *
   *
   * @param manager
   */
  @Override
  protected void initDirectory(LogManager manager)
  {
    System.out.println("initDirectory");
    directory = BlogContext.getInstance().getResourceManager().getDirectory(
      Constants.RESOURCE_LOG);
  }
}
