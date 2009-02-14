/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.blog.api.dao;

//~--- non-JDK imports --------------------------------------------------------

import sonia.blog.entity.Blog;

//~--- JDK imports ------------------------------------------------------------

import java.util.List;

/**
 *
 * @author sdorra
 */
public interface BlogDAO extends GenericDAO<Blog>
{

  /**
   * Method description
   *
   *
   * @return
   */
  public List<Blog> findAllActives();

  /**
   * Method description
   *
   *
   * @param start
   * @param max
   *
   * @return
   */
  public List<Blog> findAllActives(int start, int max);

  /**
   * Method description
   *
   *
   *
   * @param identifier
   *
   * @return
   */
  public Blog findByIdentifier(String identifier);
}
