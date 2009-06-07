/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.api.search;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.app.BlogSession;
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
   *
   * @param session, maust be a Admin- or a GlobalAdminSession
   * @param blog
   *
   */
  public void reIndex(BlogSession session, Blog blog);

  /**
   * Method description
   *
   *
   *
   * @param blog
   * @param search
   *
   * @return
   *
   * @throws SearchException
   */
  public List<SearchEntry> search(Blog blog, String search)
          throws SearchException;

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @return
   */
  public boolean isReIndexable();
}
