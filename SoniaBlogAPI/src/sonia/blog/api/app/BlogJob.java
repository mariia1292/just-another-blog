/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.api.app;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.entity.Blog;

import sonia.jobqueue.Job;

/**
 *
 * @author sdorra
 */
public abstract class BlogJob extends Job
{

  /**
   * Method description
   *
   *
   * @return
   */
  public abstract Blog getBlog();

  /**
   * Method description
   *
   *
   * @return
   */
  public abstract String getDescription();

  /**
   * Method description
   *
   *
   * @return
   */
  public abstract String getName();
}
