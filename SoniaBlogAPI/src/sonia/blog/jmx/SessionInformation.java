/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.jmx;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogContext;
import sonia.blog.api.app.Constants;

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
    if (openSessions > 0)
    {
      this.openSessions--;
    }
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  public String getAttachmentDirectorySize()
  {
    long size = 0;
    File attachments = new File(getResourceDir(),
                                Constants.RESOURCE_ATTACHMENT);

    if (attachments.exists())
    {
      for (File dir : attachments.listFiles())
      {
        size += Util.getLength(new File(dir, Constants.RESOURCE_ENTRIES));
        size += Util.getLength(new File(dir, Constants.RESOURCE_PAGES));
      }
    }

    return Util.formatSize((double) size);
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String getImageDirectorySize()
  {
    long size = 0;
    File attachments = new File(getResourceDir(),
                                Constants.RESOURCE_ATTACHMENT);

    if (attachments.exists())
    {
      for (File dir : attachments.listFiles())
      {
        size += Util.getLength(new File(dir, Constants.RESOURCE_IMAGE));
      }
    }

    return Util.formatSize((double) size);
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String getIndexDirectorySize()
  {
    long size = Util.getLength(new File(getResourceDir(),
                  Constants.RESOURCE_INDEX));

    return Util.formatSize((double) size);
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public long getOpenSessions()
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
    long size = Util.getLength(getResourceDir());

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
  public long getTotalPages()
  {
    return BlogContext.getDAOFactory().getPageDAO().count();
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public long getTotalSessions()
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

  /**
   * Method description
   *
   *
   * @return
   */
  public long getTotalUsers()
  {
    return BlogContext.getDAOFactory().getUserDAO().count();
  }

  /**
   * Method description
   *
   *
   * @return
   */
  public String getVersion()
  {
    return BlogContext.getInstance().getVersion();
  }

  /**
   * Method description
   *
   *
   * @return
   */
  private File getResourceDir()
  {
    if (resourceDir == null)
    {
      resourceDir =
        BlogContext.getInstance().getResourceManager().getResourceDirectory();
    }

    return resourceDir;
  }

  //~--- fields ---------------------------------------------------------------

  /** Field description */
  private long openSessions = 0;

  /** Field description */
  private File resourceDir;

  /** Field description */
  private Date startTime;

  /** Field description */
  private long totalSessions = 0;
}
