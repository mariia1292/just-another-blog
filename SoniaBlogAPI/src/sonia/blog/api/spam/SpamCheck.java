/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.api.spam;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogRequest;
import sonia.blog.entity.Comment;

/**
 *
 * @author sdorra
 */
public interface SpamCheck
{

  /**
   * Method description
   *
   *
   * @param request
   * @param comment
   *
   * @return
   */
  public boolean isSpam(BlogRequest request, Comment comment);
}
