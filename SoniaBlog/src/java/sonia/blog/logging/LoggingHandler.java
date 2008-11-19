/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.logging;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogContext;

import sonia.logging.FileHandler;

//~--- JDK imports ------------------------------------------------------------

import java.io.File;

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

    File resourceDir = BlogContext.getInstance().getResourceDirectory();

    directory = new File(resourceDir, "logs");
  }
}
