/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.util;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.entity.Blog;

import sonia.jobqueue.JobException;

//~--- JDK imports ------------------------------------------------------------

import java.io.File;

import java.text.MessageFormat;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author sdorra
 */
public class ExternalImageResizingJob extends ImageResizingJob
{

  /** Field description */
  private static final long serialVersionUID = 5166348410051664725L;

  /** Field description */
  private static Logger logger =
    Logger.getLogger(ExternalImageResizingJob.class.getName());

  //~--- constructors ---------------------------------------------------------

  /**
   * Constructs ...
   *
   *
   * @param command
   * @param blog
   * @param source
   * @param target
   * @param format
   * @param width
   * @param height
   */
  public ExternalImageResizingJob(String command, Blog blog, File source,
                                  File target, String format, int width,
                                  int height)
  {
    super(blog, source, target, format, width, height);
    this.command = command;
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @throws JobException
   */
  @Override
  public void excecute() throws JobException
  {
    String cmd = MessageFormat.format(command, source.getAbsolutePath(),
                                      target.getAbsolutePath(), format, width,
                                      height);

    if (logger.isLoggable(Level.FINER))
    {
      StringBuffer log = new StringBuffer();

      log.append("excecute command \"").append(cmd);
      log.append("\" to resize image");
    }

    try
    {
      Process process = Runtime.getRuntime().exec(cmd);
      int exit = process.waitFor();
      Level level = (exit == 0)
                    ? Level.FINE
                    : Level.WARNING;

      if (logger.isLoggable(level))
      {
        StringBuffer log = new StringBuffer();

        log.append("command \"").append(cmd).append("\" exit code = ");
        log.append(exit);
        logger.log(level, log.toString());
      }
    }
    catch (Exception ex)
    {
      logger.log(Level.SEVERE, null, ex);
    }
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private String command;
}
