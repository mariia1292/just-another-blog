/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.api.dao;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.api.util.PageNavigation;
import sonia.blog.entity.Blog;
import sonia.blog.entity.Page;

//~--- JDK imports ------------------------------------------------------------

import java.util.List;

/**
 *
 * @author sdorra
 */
public interface PageDAO extends GenericDAO<Page>
{

  /**
   * Method description
   *
   *
   * @param blog
   *
   * @return
   */
  public long count(Blog blog);

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param id
   * @param blog
   * @param published
   *
   * @return
   */
  public Page get(Long id, Blog blog, boolean published);

  /**
   * Method description
   *
   *
   * @param blog
   * @param published
   *
   * @return
   */
  public List<? extends PageNavigation> getAllRoot(Blog blog,
          boolean published);

  /**
   * Method description
   *
   *
   * @param blog
   *
   * @return
   */
  public List<? extends PageNavigation> getAllRoot(Blog blog);

  /**
   * Method description
   *
   *
   * @param parent
   * @param published
   *
   * @return
   */
  public List<? extends PageNavigation> getChildren(Page parent,
          boolean published);

  /**
   * Method description
   *
   *
   * @param parent
   * @param published
   *
   * @return
   */
  public List<? extends PageNavigation> getChildren(PageNavigation parent,
          boolean published);

  /**
   * Method description
   *
   *
   * @param parent
   *
   * @return
   */
  public List<? extends PageNavigation> getChildren(PageNavigation parent);

  /**
   * Method description
   *
   *
   * @param parent
   *
   * @return
   */
  public List<? extends PageNavigation> getChildren(Page parent);

  /**
   * Method description
   *
   *
   * @param parent
   *
   * @return
   */
  public List<Page> getPageChildren(Page parent);

  /**
   * Method description
   *
   *
   * @param parent
   * @param published
   *
   * @return
   */
  public List<Page> getPageChildren(Page parent, boolean published);

  /**
   * Method description
   *
   *
   * @param blog
   *
   * @return
   */
  public List<Page> getRootPages(Blog blog);

  /**
   * Method description
   *
   *
   * @param blog
   * @param published
   *
   * @return
   */
  public List<Page> getRootPages(Blog blog, boolean published);
}
