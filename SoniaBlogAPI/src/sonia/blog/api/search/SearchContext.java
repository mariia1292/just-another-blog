/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.api.search;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.entity.Blog;

//~--- JDK imports ------------------------------------------------------------

import java.util.List;

/**
 *
 * @author sdorra
 */
public interface SearchContext
{

  /**
   * Method description
   *
   *
   * @param blog
   */
  public void reIndex(Blog blog);

  /**
   * Method description
   *
   *
   *
   * @param blog
   * @param search
   *
   * @return
   */
  public List<SearchEntry> search(Blog blog, String search) throws SearchException;

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  public boolean isLocked();

  /**
   * Method description
   *
   *
   * @return
   */
  public boolean isReIndexable();
}
