/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.util;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogJob;
import sonia.blog.entity.Blog;

import sonia.jobqueue.JobException;

import sonia.util.ExecUtil;

//~--- JDK imports ------------------------------------------------------------

import java.io.File;

import java.text.MessageFormat;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author sdorra
 */
public class ExternalImageResizingJob implements BlogJob
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
   * @param timeout
   * @param blog
   * @param source
   * @param target
   * @param format
   * @param maxWidth
   * @param maxHeight
   */
  public ExternalImageResizingJob(String command, long timeout, Blog blog,
                                  File source, File target, String format,
                                  int maxWidth, int maxHeight)
  {
    this.command = command;
    this.timeout = timeout;
    this.blog = blog;
    this.source = source;
    this.target = target;
    this.format = format;
    this.maxWidth = maxWidth;
    this.maxHeight = maxHeight;
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @throws JobException
   */
  public void excecute() throws JobException
  {
    String cmd = MessageFormat.format(command, source.getAbsolutePath(),
                                      target.getAbsolutePath(), format,
                                      maxWidth, maxHeight);

    try
    {
      int exit = ExecUtil.process(cmd, timeout);
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

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  public Blog getBlog()
  {
    return blog;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String getDescription()
  {
    return "external image resizing";
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String getName()
  {
    return "ExternalImageResize";
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private Blog blog;

  /** Field description */
  private String command;

  /** Field description */
  private String format;

  /** Field description */
  private int maxHeight;

  /** Field description */
  private int maxWidth;

  /** Field description */
  private File source;

  /** Field description */
  private File target;

  /** Field description */
  private long timeout;
}
