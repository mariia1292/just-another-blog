/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.jmx;

//~--- JDK imports ------------------------------------------------------------

import java.util.Date;

/**
 *
 * @author sdorra
 */
public interface SessionInformationMBean
{

  /**
   * Method description
   *
   *
   * @return
   */
  public String getAttachmentDirectorySize();

  /**
   * Method description
   *
   *
   * @return
   */
  public String getImageDirectorySize();

  /**
   * Method description
   *
   *
   * @return
   */
  public String getIndexDirectorySize();

  /**
   * Method description
   *
   *
   * @return
   */
  public long getOpenSessions();

  /**
   * Method description
   *
   *
   * @return
   */
  public String getResourceDirectorySize();

  /**
   * Method description
   *
   *
   * @return
   */
  public String getRuntime();

  /**
   * Method description
   *
   *
   * @return
   */
  public Date getStartTime();

  /**
   * Method description
   *
   *
   * @return
   */
  public long getTotalAttachments();

  /**
   * Method description
   *
   *
   * @return
   */
  public long getTotalBlogs();

  /**
   * Method description
   *
   *
   * @return
   */
  public long getTotalCategories();

  /**
   * Method description
   *
   *
   * @return
   */
  public long getTotalComments();

  /**
   * Method description
   *
   *
   * @return
   */
  public long getTotalEntries();

  /**
   * Method description
   *
   *
   * @return
   */
  public long getTotalPages();

  /**
   * Method description
   *
   *
   * @return
   */
  public long getTotalSessions();

  /**
   * Method description
   *
   *
   * @return
   */
  public long getTotalTags();

  /**
   * Method description
   *
   *
   * @return
   */
  public long getTotalUsers();
}
