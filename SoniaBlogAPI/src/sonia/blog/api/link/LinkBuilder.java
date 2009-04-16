/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.api.link;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogRequest;
import sonia.blog.entity.Blog;
import sonia.blog.entity.PermaObject;

/**
 *
 * @author sdorra
 */
public interface LinkBuilder
{

  /**
   * Method description
   *
   *
   * @param blog
   * @param link
   *
   * @return
   */
  public String buildLink(Blog blog, String link);

  /**
   * Method description
   *
   *
   * @param request
   * @param link
   *
   * @return
   */
  public String buildLink(BlogRequest request, String link);

  /**
   * Method description
   *
   *
   * @param request
   * @param object
   *
   * @return
   */
  public String buildLink(BlogRequest request, PermaObject object);

  /**
   * Method description
   *
   *
   * @param request
   */
  public void init(BlogRequest request);

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  public boolean isInit();
}
