/**
 * Copyright (c) 2009, Sebastian Sdorra
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 3. Neither the name of JAB; nor the names of its
 *    contributors may be used to endorse or promote products derived from this
 *    software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE REGENTS OR CONTRIBUTORS BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * http://kenai.com/projects/jab
 *
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
 * @author Sebastian Sdorra
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
    File images = new File(getResourceDir(), Constants.RESOURCE_IMAGE);

    size = Util.getLength(images);

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
  public long getPluginCount()
  {
    return BlogContext.getInstance().getPluginContext().getPlugins().size();
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
  public long getServiceCount()
  {
    return BlogContext.getInstance().getServiceRegistry().getServiceCount();
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
  public Integer getVersion()
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
