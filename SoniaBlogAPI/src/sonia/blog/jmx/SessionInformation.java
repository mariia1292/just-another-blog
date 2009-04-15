/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.jmx;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogContext;

import sonia.util.Util;

//~--- JDK imports ------------------------------------------------------------

import java.io.File;

import java.util.Date;

/**
 *
 * @author sdorra
 */
public class SessionInformation implements SessionInformationMBean
{

  /**
   * Constructs ...
   *
   *
   * @param startTime
   */
  public SessionInformation(Date startTime)
  {
    this.startTime = startTime;
  }

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   */
  public synchronized void addSession()
  {
    this.openSessions++;
    this.totalSessions++;
  }

  /**
   * Method description
   *
   */
  public synchronized void removeSession()
  {
    this.openSessions--;
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  public int getOpenSessions()
  {
    return openSessions;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String getResourceDirectorySize()
  {
    File resourceDir =
      BlogContext.getInstance().getResourceManager().getResourceDirectory();
    long size = Util.getLength(resourceDir);

    return Util.formatSize(size);
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String getRuntime()
  {
    long time = startTime.getTime();
    long runtime = System.currentTimeMillis() - time;

    return Util.formatTime(runtime);
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public Date getStartTime()
  {
    return startTime;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public long getTotalAttachments()
  {
    return BlogContext.getDAOFactory().getAttachmentDAO().count();
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public long getTotalBlogs()
  {
    return BlogContext.getDAOFactory().getBlogDAO().count();
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public long getTotalCategories()
  {
    return BlogContext.getDAOFactory().getCategoryDAO().count();
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public long getTotalComments()
  {
    return BlogContext.getDAOFactory().getCommentDAO().count();
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public long getTotalEntries()
  {
    return BlogContext.getDAOFactory().getEntryDAO().count();
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public int getTotalSessions()
  {
    return totalSessions;
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public long getTotalTags()
  {
    return BlogContext.getDAOFactory().getTagDAO().count();
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private int openSessions = 0;

  /** Field description */
  private Date startTime;

  /** Field description */
  private int totalSessions = 0;
}
